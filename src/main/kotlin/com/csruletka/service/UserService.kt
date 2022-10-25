package com.csruletka.service

import com.csruletka.client.SteamApiClient
import com.csruletka.client.SteamOpenidClient
import com.csruletka.dto.steam.SteamLoginRequest
import com.csruletka.dto.steam.SteamUserInfo
import com.csruletka.dto.user.User
import com.csruletka.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Singleton
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

private val logger = LoggerFactory.getLogger(UserService::class.java)

const val API_KEY = "C1F16DE7E69F7DE3D9F14EB09306F95A"
const val JSON = "json"

private val objectMapper: ObjectMapper = ObjectMapper()

@Singleton
class UserService(
    private val steamOpenidClient: SteamOpenidClient,
    private val steamApiClient: SteamApiClient,
    private val userRepository: UserRepository,
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

    fun getUserInfoById(id: String): Mono<SteamUserInfo> = userRepository.findById(id).map { objectMapper.readValue(it.steamInfo, SteamUserInfo::class.java) }


    private suspend fun updateUserData(userId: String) {
        logger.info("Start update user data $userId")

        val steamPlayersResponse = steamApiClient.getUserSummary(
            API_KEY,
            JSON,
            userId
        )

        println(
            steamPlayersResponse
        )

        userRepository.update(
            User().apply {
                id = userId
                steamInfo = objectMapper.writeValueAsString(steamPlayersResponse.response?.players?.get(0))
            }
        ).onErrorComplete {
            logger.error(it.message)
            true
        }.awaitSingle()
    }

    private fun getResponseResult(resp: String) = resp.contains("true")

    private fun getUserId(userUrl: String) = userUrl.substring(userUrl.lastIndexOf('/') + 1)
}