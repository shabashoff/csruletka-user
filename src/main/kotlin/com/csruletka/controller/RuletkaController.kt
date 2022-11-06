package com.csruletka.controller

import com.csruletka.dto.games.ruletka.RuletkaHistory
import com.csruletka.dto.user.UserItemToAddDto
import com.csruletka.service.GameService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import reactor.core.publisher.Flux
import java.security.Principal

@Controller("game/ruletka")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RuletkaController(
    private val gameService: GameService
) {
    @Post("skin")
    suspend fun addSkins(
        @Body
        skins: List<UserItemToAddDto>,
        principal: Principal
    ) {
        gameService.ruletkaAddSkins(principal.name, skins)
    }

    @Get("history")
    fun getHistory(principal: Principal): Flux<RuletkaHistory> = gameService.getRuletkaHistory()
}