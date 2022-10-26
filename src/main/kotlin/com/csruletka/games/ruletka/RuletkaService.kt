package com.csruletka.games.ruletka

import io.micronaut.websocket.WebSocketSession
import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap


@Singleton
class RuletkaService(
    private val wsSessions: ConcurrentHashMap<String, WebSocketSession> = ConcurrentHashMap()
) {
    fun addSession(webSocketSession: WebSocketSession) {
        wsSessions[webSocketSession.id] = webSocketSession

        sendMessage("users: " + wsSessions.count())
    }

    fun removeSession(webSocketSession: WebSocketSession) {
        wsSessions.remove(webSocketSession.id)

        sendMessage("users: " + wsSessions.count())
    }


    private fun sendMessage(obj: Any){
        wsSessions.forEach {
            it.value.sendAsync(obj)
        }
    }
}