package com.csruletka.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class DemoResource {
    @Get
    fun getGreetings(): String {
        return "Hello %s from demo application"
    }
}