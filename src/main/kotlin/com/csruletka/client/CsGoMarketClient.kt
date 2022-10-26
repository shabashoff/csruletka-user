package com.csruletka.client

import com.csruletka.dto.csgomarket.CsGoMarketResponse
import retrofit2.http.GET


interface CsGoMarketClient {
    @GET("/api/v2/prices/RUB.json")
    suspend fun getPricesRub(): CsGoMarketResponse
}