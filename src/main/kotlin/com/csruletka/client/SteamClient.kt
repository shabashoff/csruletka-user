package com.csruletka.client

import com.csruletka.dto.steam.SteamUserInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SteamClient {
    @GET("openid/login")
    suspend fun openidLogin(@QueryMap options: Map<String, String>): String
}