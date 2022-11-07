package com.csruletka.controller

import com.csruletka.service.TaskService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("task")
@Secured(SecurityRule.IS_AUTHENTICATED)
class TaskController(
    private val taskService: TaskService
) {
    @Get("all")
    fun getAll(principal: Principal) {
    }

    @Post("start")
    suspend fun start(principal: Principal, id: Long): String = taskService.startTask(principal.name, id)


    //Only for admins
    fun addTask() {

    }
}