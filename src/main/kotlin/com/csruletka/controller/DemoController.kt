package com.csruletka.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import retrofit2.http.POST

@Controller("user")
@Secured(SecurityRule.IS_AUTHENTICATED)
class DemoResource {
    @Get("info")
    fun getGreetings(): String {
        return "Hello %s from demo application"
    }
}