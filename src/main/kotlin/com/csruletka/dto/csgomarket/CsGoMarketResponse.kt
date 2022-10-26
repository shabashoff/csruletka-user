package com.csruletka.dto.csgomarket


data class CsGoMarketResponse(
    var success: Boolean? = null,
    var time: Long? = null,
    var currency: String? = null,
    var items: List<CsGoMarketItem>? = null
)