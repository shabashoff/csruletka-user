package com.csruletka.config.rest

import com.csruletka.client.SteamApiClient
import com.csruletka.client.SteamOpenidClient
import com.google.gson.GsonBuilder
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Factory
class RestConfig {
    @Singleton
    fun steamOpenidClient(): SteamOpenidClient =
        Retrofit.Builder()
            .baseUrl("https://steamcommunity.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(simpleClient())
            .build()
            .create(SteamOpenidClient::class.java)

    @Singleton
    fun steamApiClient(): SteamApiClient =
        Retrofit.Builder()
            .baseUrl("https://api.steampowered.com/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setFieldNamingStrategy { it.name.lowercase() }.create()
                )
            )
            .client(simpleClient())
            .build()
            .create(SteamApiClient::class.java)

    private fun simpleClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(
                HttpLoggingInterceptor().also {
                    it.setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
}