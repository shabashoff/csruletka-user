package com.csruletka.dto.task

data class TaskInProgress(
    val template: TaskTemplate,
    var completed: Boolean
)