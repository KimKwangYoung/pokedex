package com.example.first_week_mission.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokeApiClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val api: PokeApiService

    init {
        val okHttpClient = buildClient()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(PokeApiService::class.java)
    }

    private fun buildClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(logging)

        return builder.build()
    }
}