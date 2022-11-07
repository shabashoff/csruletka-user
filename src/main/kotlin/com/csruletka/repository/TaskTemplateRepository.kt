package com.csruletka.repository

import com.csruletka.dto.task.TaskTemplate
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.reactive.ReactorCrudRepository

@Repository
interface TaskTemplateRepository : ReactorCrudRepository<TaskTemplate, Long> {
}