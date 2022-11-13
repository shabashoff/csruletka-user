package com.csruletka.service

import com.github.steam.api.SteamID
import com.github.steam.api.TradeOffer
import com.github.steam.api.TradeUser
import com.github.steam.api.enumeration.EAppID
import com.github.steam.api.enumeration.EContextID
import jakarta.inject.Singleton


@Singleton
class SteamTradeService {




    fun sendOffer(){
        TradeUser.addCookie("steamMachineAuth76561198094451823", "088DA54E45FAF32DA3E75C5CC6DBE61800AA4F46", true)
        TradeUser.addCookie("steamLogin", "nik16011", false)
        TradeUser.addCookie("steamLoginSecure", "76561198094451823%7C%7CeyAidHlwIjogIkpXVCIsICJhbGciOiAiRWREU0EiIH0.eyAiaXNzIjogInI6MEMwRl8yMTc4RDY5Ml9BMzQ4QyIsICJzdWIiOiAiNzY1NjExOTgwOTQ0NTE4MjMiLCAiYXVkIjogWyAid2ViIiBdLCAiZXhwIjogMTY2ODM2OTg5MCwgIm5iZiI6IDE2NTk2NDM0ODYsICJpYXQiOiAxNjY4MjgzNDg2LCAianRpIjogIjE1MkVfMjE5NjBCNERfRkJEOTQiLCAib2F0IjogMTY2NjU0MjY0OCwgInJ0X2V4cCI6IDE2ODQ1MDM0MzYsICJwZXIiOiAwLCAiaXBfc3ViamVjdCI6ICIxNDYuMTU4LjY3LjI0OSIsICJpcF9jb25maXJtZXIiOiAiMTQ2LjE1OC42Ny4yNDkiIH0.4zZs9_FhtcIfwxRJSY1-ws0TgE-NvG5I2lQSWSLqWaw0s9Cy3HQ5d5c2x0rhlkCsWkcgo-1BxWzLUjAtGi5-Cw", false)
        TradeUser.addCookie("steamCountry", "KZ%7C9b4c99f651d1735ac1f3f591d4f9b3cc", false)

        val steam = TradeUser("C1F16DE7E69F7DE3D9F14EB09306F95A", "nik16011", "Ptktysqvbh1997")

        val tradeOffer = steam.makeOffer(SteamID(76561199004803730))

        val themInv = tradeOffer.getTheirInventory(EAppID.CSGO, EContextID.BACKPACK)

        tradeOffer.addItemsToReceive(themInv[1])
        tradeOffer.send()
    }

}