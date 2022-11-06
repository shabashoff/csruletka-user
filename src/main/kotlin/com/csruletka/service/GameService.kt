package com.csruletka.service

import com.csruletka.dto.games.ruletka.RuletkaHistory
import com.csruletka.dto.user.UserItemToAddDto
import com.csruletka.games.ruletka.RuletkaService
import com.csruletka.repository.RuletkaHistoryRepository
import com.csruletka.repository.UserRepository
import jakarta.inject.Singleton
import kotlinx.coroutines.reactor.awaitSingle
import reactor.core.publisher.Flux

@Singleton
class GameService(
    private val ruletkaService: RuletkaService,
    private val ruletkaHistoryRepository: RuletkaHistoryRepository,
    private val userRepository: UserRepository
) {
    suspend fun ruletkaAddSkins(userId: String, skins: List<UserItemToAddDto>) {
        val user = userRepository.findById(userId).awaitSingle()

        ruletkaService.addSkins(
            user.id!!,
            user.steamInfo!!.personaName!!,
            user.steamInfo!!.avatarMedium!!,
            user.steamInfo!!.inventory!!
        )
    }

    fun getRuletkaHistory(): Flux<RuletkaHistory> = ruletkaHistoryRepository.findAll()

}