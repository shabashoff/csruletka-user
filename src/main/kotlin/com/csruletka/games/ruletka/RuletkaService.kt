package com.csruletka.games.ruletka

import com.csruletka.dto.games.ruletka.SkinsInGame
import io.micronaut.websocket.WebSocketSession
import jakarta.inject.Singleton
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs

const val DEFAULT_TIME_TO_START = 60

private val random = Random()

private var timerBeforeStart: Int = DEFAULT_TIME_TO_START

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
                sendMessage("time: " + timerBeforeStart--)

                if (timerBeforeStart <= 0) {
                    startRuletka()
                    timerBeforeStart = DEFAULT_TIME_TO_START
                }
            }

            sendMessage("time: $timerBeforeStart")
        }
    }

    fun addSession(webSocketSession: WebSocketSession) {
        wsSessions[webSocketSession.id] = webSocketSession

        skinsInGame.forEach { webSocketSession.sendAsync(it) }
        sendMessage("users: " + wsSessions.count())
    }

    fun removeSession(webSocketSession: WebSocketSession) {
        wsSessions.remove(webSocketSession.id)

        sendMessage("users: " + wsSessions.count())
    }

    fun addSkins(skinToAdd: SkinsInGame) {
        sendMessage(skinToAdd)

        skinsInGame.add(skinToAdd)
    }

    private fun startRuletka() {
        val winner = abs(random.nextInt()) % skinsInGame.sumOf { it.ticketsCount }

        timerBeforeStart = DEFAULT_TIME_TO_START
        skinsInGame.clear()

        sendMessage("winner: $winner")
    }

    private fun isReadyForGame(): Boolean {
        if (timerBeforeStart != DEFAULT_TIME_TO_START) return true

        return skinsInGame.size > 2
    }

    private fun sendMessage(obj: Any) {
        wsSessions.forEach {
            it.value.sendAsync(obj)
        }
    }
}