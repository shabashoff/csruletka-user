package com.csruletka.dto.user

import java.time.LocalDateTime

class UserInventoryItem (
    var id: String,
    var steamId: String,
    var marketHashName: String? = null,
    var iconUrl: String? = null,
    var price: Double? = null,
    var lockedUntil: LocalDateTime
)