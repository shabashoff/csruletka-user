package com.csruletka.dto.games.ruletka

import com.csruletka.dto.user.UserInventoryItem

data class SkinsInGame(
    val userId: String,
    val userName: String,
    val avatar: String,
    val priceSum: Double,
    val ticketsFrom: Int,
    val ticketsTo: Int,
    val skins: List<UserInventoryItem>
)