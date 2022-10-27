package com.csruletka.dto.user

class SteamUserInfo(
    var steamId: String? = null,
    var communityVisibilityState: Int? = null,
    var profileState: Int? = null,
    var personaName: String? = null,
    var commentPermission: Int? = null,
    var profileUrl: String? = null,
    var avatar: String? = null,
    var avatarMedium: String? = null,
    var avatarFull: String? = null,
    var avatarHash: String? = null,
    var lastLogoff: Int? = null,
    var personaState: Int? = null,
    var realName: String? = null,
    var primaryClanId: String? = null,
    var timeCreated: Int? = null,
    var personaStateFlags: Int? = null,
    var gameExtraInfo: String? = null,
    var gameId: String? = null,
    var locCountryCode: String? = null,
    var locStateCode: String? = null,
    var locCityId: Int? = null,
    var inventoryPriceAmount: Double? = null,
    var inventory: List<SteamItem>? = null
) {
    fun calcPrice() {
        inventoryPriceAmount = inventory?.sumOf { it.price!! * it.amount!! }
    }
}
