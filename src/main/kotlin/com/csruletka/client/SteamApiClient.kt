package com.csruletka.client

import com.csruletka.dto.steam.SteamPlayersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamApiClient {

    @GET("/ISteamUser/GetPlayerSummaries/v2/")
    suspend fun getUserSummary(
        @Query("key") apiKey: String,
        @Query("format") format: String,
        @Query("steamids") steamId: String
    ): SteamPlayersResponse
}