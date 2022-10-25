package com.csruletka.config.rest

import com.csruletka.client.SteamClient
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Factory
class RestConfig {
    @Singleton
    fun negativeHttpClient(): SteamClient =
        Retrofit.Builder()
            .baseUrl("https://steamcommunity.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(simpleClient())
            .build()
            .create(SteamClient::class.java)

    private fun simpleClient(): OkHttpClient = OkHttpClient.Builder().build()
}