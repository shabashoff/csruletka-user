package com.csruletka.controller

import com.csruletka.service.SteamTradeService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("test")
@Secured(SecurityRule.IS_ANONYMOUS)
class TestController(
    private val steamTradeService: SteamTradeService
) {
    @Get("load")
    fun getUserInfo() {
        steamTradeService.sendOffer()
    }
}