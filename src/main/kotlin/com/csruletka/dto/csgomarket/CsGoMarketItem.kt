package com.csruletka.dto.csgomarket

import com.google.gson.annotations.SerializedName


data class CsGoMarketItem(
    @SerializedName("market_hash_name")
    var marketHashName: String? = null,
    var volume: Int? = null,
    var price: Double? = null
)