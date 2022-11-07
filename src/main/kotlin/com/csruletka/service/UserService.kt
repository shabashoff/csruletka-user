package com.csruletka.service

import com.csruletka.client.SteamApiClient
import com.csruletka.client.SteamClient
import com.csruletka.client.SteamOpenidClient
import com.csruletka.dto.steam.SteamLoginRequest
import com.csruletka.dto.user.SteamItem
import com.csruletka.dto.user.User
import com.csruletka.dto.user.UserInventoryItem
import com.csruletka.dto.user.UserItemToAddDto
import com.csruletka.repository.UserRepository
import jakarta.inject.Singleton
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.*

private val logger = LoggerFactory.getLogger(UserService::class.java)

const val API_KEY = "C1F16DE7E69F7DE3D9F14EB09306F95A"
const val JSON = "json"
const val CSGO_GAME_ID = "730"

@Singleton
class UserService(
    private val steamClient: SteamClient,
    private val steamOpenidClient: SteamOpenidClient,
    private val steamApiClient: SteamApiClient,
    private val userRepository: UserRepository,
    private val csGoPriceService: CsGoPriceService,
) {
    private val steamAuthMap: Map<String, String> = mapOf(
        "openid.ns" to "http://specs.openid.net/auth/2.0",
        "openid.mode" to "check_authentication",
        "openid.op_endpoint" to "https://steamcommunity.com/openid/login",
    )

    suspend fun loginUser(steamLoginRequest: SteamLoginRequest): String? {
        val response = steamOpenidClient.openidLogin(
            steamAuthMap.toMutableMap().also {
                it["openid.claimed_id"] = steamLoginRequest.identity
                it["openid.identity"] = steamLoginRequest.identity
                it["openid.sig"] = steamLoginRequest.sig
                it["openid.signed"] = steamLoginRequest.signed
                it["openid.return_to"] = steamLoginRequest.returnTo
                it["openid.assoc_handle"] = steamLoginRequest.assocHandle
                it["openid.response_nonce"] = steamLoginRequest.responseNonce
            }
        )

        val loginSucceed = getResponseResult(response)

        return if (loginSucceed) {
            val userId = getUserId(steamLoginRequest.identity)
            updateUserData(
                userId
            )
            userId
        } else null
    }

    suspend fun getUserInfoById(id: String): User = csGoPriceService.addPriceToUser(
        userRepository.findById(id).awaitSingle()
    )


    suspend fun updateUserData(userId: String) {
        logger.info("Start update user data $userId")

        val steamPlayersResponse = steamApiClient.getUserSummary(
            API_KEY,
            JSON,
            userId
        )

        userRepository.update(
            (userRepository.findById(userId).awaitSingleOrNull() ?: User()).apply {
                id = userId
                steamInfo = steamPlayersResponse.response?.players?.get(0)?.also {
                    it.inventory = getInventory(userId)
                    it.calcPrice()
                }

            }
        ).awaitSingleOrNull()
    }

    suspend fun addInventory(userId: String, userItemToAdd: List<UserItemToAddDto>) {
        val user = userRepository.findById(userId).awaitSingle()

        //TODO: send trade offer


        val mapInventoryId = user.steamInfo?.inventory?.associate { it.id to it }

        userRepository.update(
            user.also {

                it.inventory.addAll(
                    userItemToAdd.map {
                        if (mapInventoryId?.contains(it.id) == true) {
                            val steamItem = mapInventoryId[it.id]!!
                            UserInventoryItem(
                                id = UUID.randomUUID().toString(),
                                steamId = it.id,
                                marketHashName = steamItem.marketHashName,
                                iconUrl = steamItem.iconUrl,
                                lockedUntil = LocalDateTime.now().plusDays(7)
                            )
                        } else {
                            throw IllegalArgumentException("User dont have this skin, update page")
                        }
                    }
                )
            }
        ).awaitSingleOrNull()
    }


    suspend fun getSkins(userId: String, skins: List<UserItemToAddDto>) {
        val user = userRepository.findById(userId).awaitSingle()
        val skins = user.removeSkins(skins.map { it.id })

        userRepository.update(user).awaitSingleOrNull()

        //TODO: send trade offer with skins
    }

    private suspend fun getInventory(userId: String): List<SteamItem> {
        val inventory = steamClient.getInventory(userId, CSGO_GAME_ID)
        val itemsMap = HashMap<String, SteamItem>()

        inventory.assets!!.forEach {
            if (itemsMap.contains(it.classId)) itemsMap[it.classId]!!.amount = itemsMap[it.classId]!!.amount!! + 1
            else itemsMap[it.classId!!] = SteamItem(id = it.classId, amount = it.amount?.toInt())
        }
        inventory.descriptions!!.forEach {
            itemsMap[it.classId]?.let { item ->
                item.marketHashName = it.marketHashName
                item.iconUrl = it.iconUrl
            }
        }
        itemsMap.values.forEach {
            it.marketHashName?.let { name ->
                it.price = csGoPriceService.getPriceByMarketName(name)
            }
        }

        return itemsMap.values.toList()
            .filter { it.price != null }
            .sortedByDescending { it.price }
    }

    private fun getResponseResult(resp: String) = resp.contains("true")

    private fun getUserId(userUrl: String) = userUrl.substring(userUrl.lastIndexOf('/') + 1)
}