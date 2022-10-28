package com.csruletka.service

import com.csruletka.dto.games.ruletka.SkinsInGame
import com.csruletka.games.ruletka.RuletkaService
import com.csruletka.repository.UserRepository
import jakarta.inject.Singleton
import kotlinx.coroutines.reactor.awaitSingle

@Singleton
class GameService(
    private val ruletkaService: RuletkaService,
    private val userRepository: UserRepository
) {
    suspend fun ruletkaAddSkins(userId: String) {
        val user = userRepository.findById(userId).awaitSingle()

        ruletkaService.addSkins(
            user.id!!,
            user.steamInfo!!.personaName!!,
            user.steamInfo!!.avatarMedium!!,
            user.steamInfo!!.inventory!!
        )
    }
}