package com.csruletka.dto.user

import com.google.gson.annotations.SerializedName

class SteamItem(
    var id: String? = null,
    var marketHashName: String? = null,
    var iconUrl: String? = null,
    var amount: Int? = null,
    var price: Double? = null,
)
