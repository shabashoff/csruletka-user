package com.csruletka.dto.steam

data class SteamLoginRequest(
    var identity: String,
    var sig: String,
    var signed: String,
    var returnTo: String,
    var responseNonce: String,
    var assocHandle: String
)