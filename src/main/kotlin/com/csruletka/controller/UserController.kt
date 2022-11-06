package com.csruletka.controller

import com.csruletka.dto.user.User
import com.csruletka.dto.user.UserItemToAddDto
import com.csruletka.service.UserService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserController(
    private val userService: UserService
) {
    @Get("info")
    suspend fun getUserInfo(principal: Principal): User = userService.getUserInfoById(principal.name)

    @Post("add-skin")
    suspend fun addSkins(
        @Body
        inventory: List<UserItemToAddDto>,
        principal: Principal
    ) {
        userService.addInventory(
            principal.name,
            inventory
        )
    }

    @Post("get-skin")
    suspend fun getSkins(
        @Body
        inventory: List<UserItemToAddDto>,
        principal: Principal
    ) {

    }
}