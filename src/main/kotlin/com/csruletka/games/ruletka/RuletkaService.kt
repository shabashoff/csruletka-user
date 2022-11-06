package com.csruletka.games.ruletka

import com.csruletka.dto.games.ruletka.GameCommand
import com.csruletka.dto.games.ruletka.RuletkaHistory
import com.csruletka.dto.games.ruletka.SkinsInGame
import com.csruletka.dto.user.UserInventoryItem
import com.csruletka.repository.RuletkaHistoryRepository
import com.csruletka.repository.UserRepository
import io.micronaut.websocket.WebSocketSession
import jakarta.inject.Singleton
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs

const val DEFAULT_TIME_TO_START = 60
const val DEFAULT_TICKET_FROM = 1

private val random = Random()

private var timerBeforeStart: Int = DEFAULT_TIME_TO_START
private var curTicketFrom: AtomicInteger = AtomicInteger(DEFAULT_TICKET_FROM)

private val skinsInGame: MutableList<SkinsInGame> = Collections.synchronizedList(arrayListOf())

private val wsSessions: ConcurrentHashMap<String, WebSocketSession> = ConcurrentHashMap()

@Singleton
class RuletkaService(
    private val ruletkaHistoryRepository: RuletkaHistoryRepository,
    private val userRepository: UserRepository
) {


    init {
        fixedRateTimer(
            "RuletkaWS-timer",
            true,
            0L,
            1000L
        ) {
            runBlocking {
                if (isReadyForGame()) {
                    sendMessage(GameCommand("time", timerBeforeStart--))

                    if (timerBeforeStart <= 0) {
                        startRuletka()
                        timerBeforeStart = DEFAULT_TIME_TO_START
                    }
                }

                sendMessage(GameCommand("time", timerBeforeStart))
            }
        }
    }

    fun addSession(webSocketSession: WebSocketSession) {
        wsSessions[webSocketSession.id] = webSocketSession

        skinsInGame.forEach { webSocketSession.sendAsync(GameCommand("addSkins", it)) }
        sendMessage(GameCommand("users", wsSessions.count()))
    }

    fun removeSession(webSocketSession: WebSocketSession) {
        wsSessions.remove(webSocketSession.id)

        sendMessage(GameCommand("users", wsSessions.count()))
    }

    fun addSkins(
        userId: String,
        userName: String,
        avatar: String,
        skins: List<UserInventoryItem>
    ) {
        val skinsSum = skins.sumOf { it.price!! }
        val ticketsCount = (skinsSum * 10).toInt()
        val ticketFrom = curTicketFrom.getAndAdd(ticketsCount)
        val ticketTo = ticketFrom + ticketsCount - 1

        val skinToAdd = SkinsInGame(
            userId,
            userName,
            avatar,
            skinsSum,
            ticketFrom,
            ticketTo,
            skins
        )

        sendMessage(GameCommand("addSkins", skinToAdd))

        skinsInGame.add(skinToAdd)
    }

    private suspend fun startRuletka() {
        val winner = (abs(random.nextInt()) % (curTicketFrom.get() - 1)) + 1
        var winUser: SkinsInGame? = null
        for (skins in skinsInGame) {
            if (skins.ticketsFrom <= winner && skins.ticketsTo >= winner) {
                winUser = skins
            }
        }

        saveHistory(winner, winUser!!)
        sendSkinsToWinner(winUser)

        curTicketFrom.set(DEFAULT_TICKET_FROM)
        timerBeforeStart = DEFAULT_TIME_TO_START
        skinsInGame.clear()

        sendMessage(GameCommand("winner", winner))
        sendMessage(GameCommand("winnUser", winUser))
    }

    private suspend fun saveHistory(winTicket: Int, winUser: SkinsInGame) {
        ruletkaHistoryRepository.save(
            RuletkaHistory().apply {
                winnerTicket = winTicket
                winner = winUser
                users = skinsInGame.toList()
            }
        ).awaitSingleOrNull()
    }

    private suspend fun sendSkinsToWinner(winUser: SkinsInGame) {
        val user = userRepository.findById(winUser.userId).awaitSingle()

        user.inventory.addAll(skinsInGame.flatMap { it.skins })

        userRepository.save(user).awaitSingleOrNull()
    }

    private fun isReadyForGame(): Boolean {
        if (timerBeforeStart != DEFAULT_TIME_TO_START) return true

        return skinsInGame.size > 1
    }

    private fun <T> sendMessage(gameCommand: GameCommand<T>) {
        wsSessions.forEach {
            it.value.sendAsync(gameCommand)
        }
    }
}