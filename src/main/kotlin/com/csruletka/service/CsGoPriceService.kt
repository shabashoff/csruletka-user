package com.csruletka.service

import com.csruletka.client.CsGoMarketClient
import io.micronaut.context.annotation.Context
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

private val priceMap: HashMap<String, Double> = HashMap()

@Context
@Singleton
class CsGoPriceService(
    private val csGoMarketClient: CsGoMarketClient,
) {
    init {
        reloadPrices()
    }

    fun getPrice(marketHashName: String) = priceMap[marketHashName]


    private fun reloadPrices() {
        runBlocking {
            val pricesRub = csGoMarketClient.getPricesRub()

            println(pricesRub.success)
            println(pricesRub.time)
            println(pricesRub.items!!.size)
            println(pricesRub.items!![0])

            pricesRub.items!!.forEach {
                priceMap[it.marketHashName!!] = it.price!!
            }
        }
    }
}