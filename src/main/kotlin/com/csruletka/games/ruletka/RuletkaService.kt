package com.csruletka.games.ruletka

import com.csruletka.dto.games.ruletka.GameCommand
import com.csruletka.dto.games.ruletka.SkinsInGame
import com.csruletka.dto.user.SteamItem
import io.micronaut.websocket.WebSocketSession
import jakarta.inject.Singleton
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

private var skinsInGame: MutableList<SkinsInGame> = Collections.synchronizedList(arrayListOf())

@Singleton
class RuletkaService(
    private val wsSessions: ConcurrentHashMap<String, WebSocketSession> = ConcurrentHashMap()
) {


    init {
        fixedRateTimer(
            "RuletkaWS-timer",
            true,
            0L,
            1000L
        ) {
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
        skins: List<SteamItem>
    ) {
        val skinsSum = skins.sumOf { it.amount!! * it.price!! }
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

    private fun startRuletka() {
        val winner = (abs(random.nextInt()) % (curTicketFrom.get() - 1)) + 1
        var winUser: SkinsInGame? = null
        for (skins in skinsInGame) {
            if (skins.ticketsFrom <= winner && skins.ticketsTo >= winner) {
                winUser = skins
            }
        }

        curTicketFrom.set(DEFAULT_TICKET_FROM)
        timerBeforeStart = DEFAULT_TIME_TO_START
        skinsInGame.clear()

        sendMessage(GameCommand("winner", winner))
        sendMessage(GameCommand("winnUser", winUser))
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