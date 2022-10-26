package com.csruletka.games.ruletka

import com.csruletka.service.UserService
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(UserService::class.java)

@Secured(SecurityRule.IS_ANONYMOUS)
@ServerWebSocket(value = "/ws/game/ruletka")
class RuletkaWS(
    private val ruletkaService: RuletkaService
) {
    @OnOpen
    fun onOpen(
        session: WebSocketSession?
    ): Publisher<String>? {
        logger.info("Open session: $session")
        ruletkaService.addSession(session!!)
        return null
    }


    @OnMessage
    fun onMessage(message:String, session: WebSocketSession?): Publisher<String>? {
        logger.info("Message: $message ")
        return null
    }

    @OnClose
    fun onClose(
        session: WebSocketSession?
    ): Publisher<String>? {
        logger.info("Close session: $session")
        ruletkaService.removeSession(session!!)
        return null
    }

}