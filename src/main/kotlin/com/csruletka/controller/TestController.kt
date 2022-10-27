package com.csruletka.controller

import com.csruletka.service.CsGoPriceService
import com.csruletka.service.UserService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("test")
@Secured(SecurityRule.IS_ANONYMOUS)
class TestController(
    private val userService: UserService
) {
    @Get("load")
    suspend fun getUserInfo() {
        userService.updateUserData("76561198094451823")
    }
}