package com.csruletka.dto.steam

import com.csruletka.dto.user.SteamUserInfo


data class SteamPlayers(
    var players: List<SteamUserInfo>? = null,
)
