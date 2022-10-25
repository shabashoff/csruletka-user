package com.csruletka.service

import com.csruletka.client.SteamClient
import com.csruletka.dto.steam.SteamLoginRequest
import jakarta.inject.Singleton


@Singleton
class UserService(
    private val steamClient: SteamClient
) {
    private val steamAuthMap: Map<String, String> = mapOf(
        "openid.ns" to "http://specs.openid.net/auth/2.0",
        "openid.mode" to "check_authentication",
        "openid.op_endpoint" to "https://steamcommunity.com/openid/login",
        "openid.op_endpoint" to "https://steamcommunity.com/openid/login",
    )

    suspend fun loginUser(steamLoginRequest: SteamLoginRequest) {
        println(
            steamClient.openidLogin(
                steamAuthMap.toMutableMap().also {
                    it["openid.claimed_id"] = steamLoginRequest.identity
                    it["openid.identity"] = steamLoginRequest.identity
                    it["openid.sig"] = steamLoginRequest.sig
                    it["openid.signed"] = steamLoginRequest.signed
                    it["openid.return_to"] = steamLoginRequest.returnTo
                    it["openid.response_nonce"] = steamLoginRequest.responseNonce
                }
            )
        )
    }
}