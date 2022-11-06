package com.csruletka.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("task")
@Secured(SecurityRule.IS_AUTHENTICATED)
class TaskController(
) {
    @Get("all")
    fun getAll(principal: Principal){
    }

    @Post("start")
    fun start(principal: Principal){
    }
}