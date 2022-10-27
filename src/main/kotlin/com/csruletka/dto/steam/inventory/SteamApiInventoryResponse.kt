package com.csruletka.dto.steam.inventory

import com.google.gson.annotations.SerializedName

data class SteamApiInventoryResponse(
    var assets: List<Assets>? = null,
    var descriptions: List<Descriptions>? = null,
    @SerializedName("total_inventory_count")
    var totalInventoryCount: Int? = null,
    var success: Int? = null,
    var rwgrsn: Int? = null
)