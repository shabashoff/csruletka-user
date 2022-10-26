package com.csruletka.controller

import com.csruletka.service.CsGoPriceService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("test")
@Secured(SecurityRule.IS_ANONYMOUS)
class TestController(
    private val csGoPriceService: CsGoPriceService
) {
    @Get("load")
    fun getUserInfo() {
        csGoPriceService.reloadPrices()
    }
}