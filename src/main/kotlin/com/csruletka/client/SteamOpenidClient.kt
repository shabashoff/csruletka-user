package com.csruletka.client

import retrofit2.http.GET
import retrofit2.http.QueryMap

interface SteamOpenidClient {
    @GET("/openid/login")
    suspend fun openidLogin(@QueryMap options: Map<String, String>): String
}