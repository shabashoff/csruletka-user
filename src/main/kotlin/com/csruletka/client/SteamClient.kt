package com.csruletka.client

import com.csruletka.dto.steam.inventory.SteamApiInventoryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface SteamClient {
    @GET("/inventory/{profileId}/{gameId}/2?l=english&count=5000")
    suspend fun getInventory(
        @Path("profileId") profileId: String,
        @Path("gameId") gameId: String
    ): SteamApiInventoryResponse
}