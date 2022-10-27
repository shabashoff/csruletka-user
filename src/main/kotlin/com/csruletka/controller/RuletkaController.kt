package com.csruletka.controller

import com.csruletka.service.GameService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("game/ruletka")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RuletkaController(
    private val gameService: GameService
) {
    @Post("skin")
    suspend fun getUserInfo(principal: Principal) {
        gameService.ruletkaAddSkins(principal.name)
    }
}