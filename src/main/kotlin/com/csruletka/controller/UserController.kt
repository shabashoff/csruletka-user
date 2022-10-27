package com.csruletka.controller

import com.csruletka.dto.user.SteamUserInfo
import com.csruletka.service.UserService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import reactor.core.publisher.Mono
import java.security.Principal

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserController(
    private val userService: UserService
) {
    @Get("info")
    fun getUserInfo(principal: Principal): Mono<SteamUserInfo> = userService.getUserInfoById(principal.name)
}