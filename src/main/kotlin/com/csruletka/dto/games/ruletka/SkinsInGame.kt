package com.csruletka.dto.games.ruletka

import com.csruletka.dto.user.SteamItem

data class SkinsInGame(
    val userId: String,
    val priceSum: Double,
    val ticketsCount: Int,
    val skins: List<SteamItem>
)