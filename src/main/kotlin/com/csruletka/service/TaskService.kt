package com.csruletka.service

import com.csruletka.dto.task.TaskInProgress
import com.csruletka.dto.task.TaskTemplate
import com.csruletka.repository.TaskTemplateRepository
import com.csruletka.repository.UserRepository
import jakarta.inject.Singleton
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull

@Singleton
class TaskService(
    private val userRepository: UserRepository,
    private val taskTemplateRepository: TaskTemplateRepository
) {
    suspend fun startTask(userId: String, taskId: Long): String {
        val user = userRepository.findById(userId).awaitSingle()
        user.tasks.find { taskId == it.template.id }?.let {
            throw IllegalArgumentException("Task already finished or in progress!")
        }
        val task = taskTemplateRepository.findById(taskId).awaitSingle()
        user.tasks.add(
            TaskInProgress(
                task,
                false
            )
        )
        userRepository.update(user).awaitSingleOrNull()

        return generateReferralLink(userId, task)
    }

    fun generateReferralLink(userId: String, task: TaskTemplate): String {
        return "test"
    }
}