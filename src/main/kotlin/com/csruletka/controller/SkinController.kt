package com.csruletka.controller

import com.csruletka.service.UserService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("skins")
@Secured(SecurityRule.IS_AUTHENTICATED)
class SkinController(
    private val userService: UserService
) {
    @Get("info")
    fun getSkinsInfo(principal: Principal){

    }
}