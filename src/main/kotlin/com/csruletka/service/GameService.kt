package com.csruletka.service

import com.csruletka.dto.games.ruletka.RuletkaHistory
import com.csruletka.dto.user.UserInventoryItem
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
    private val csGoPriceService: CsGoPriceService,
    private val ruletkaHistoryRepository: RuletkaHistoryRepository,
    private val userRepository: UserRepository
) {
    suspend fun ruletkaAddSkins(userId: String, skins: List<UserItemToAddDto>) {
        val user = csGoPriceService.addPriceToUser(
            userRepository.findById(userId).awaitSingle()
        )

        val inventoryMap = user.inventory.associateBy { it.id }?.toMutableMap()
        val skinsToSend = arrayListOf<UserInventoryItem>()


        skins.forEach {
            if (inventoryMap.contains(it.id)) {
                skinsToSend.add(inventoryMap.remove(it.id)!!)
            } else {
                throw IllegalArgumentException("Cannot find item in user skins")
            }
        }

        userRepository.update(
            user.also {
                it.inventory = inventoryMap.values.toMutableList()
            }
        ).awaitSingle()

        ruletkaService.addSkins(
            user.id!!,
            user.steamInfo!!.personaName!!,
            user.steamInfo!!.avatarMedium!!,
            skinsToSend
        )
    }

    fun getRuletkaHistory(): Flux<RuletkaHistory> = ruletkaHistoryRepository.findAll()

}