package com.csruletka.dto.steam.inventory

import com.google.gson.annotations.SerializedName


data class Descriptions(
    var appId: Int? = null,
    var classId: String? = null,
    var instanceId: String? = null,
    var currency: String? = null,
    var descriptions: List<Descriptions>? = null,
    var tradable: Int? = null,
    var name: String? = null,
    var type: String? = null,
    var commodity: Int? = null,
    var tags: List<Tags>? = null,
    var marketable: Int? = null,
    @SerializedName("icon_url")
    var iconUrl: String? = null,
    @SerializedName("name_color")
    var nameColor: String? = null,
    @SerializedName("market_name")
    var marketName: String? = null,
    @SerializedName("market_hash_name")
    var marketHashName: String? = null,
    @SerializedName("background_color")
    var backgroundColor: String? = null,
    @SerializedName("market_tradable_restriction")
    var marketTradableRestriction: Int? = null,
    @SerializedName("market_buy_country_restriction")
    var marketBuyCountryRestriction: String? = null
)