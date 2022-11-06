package com.csruletka.dto.user

import java.time.LocalDateTime

class UserInventoryItem (
    var id: String,
    var amount: Int,
    var marketHashName: String? = null,
    var iconUrl: String? = null,
    var lockedUntil: LocalDateTime
)