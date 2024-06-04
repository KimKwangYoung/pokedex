package com.example.first_week_mission.service

import com.example.first_week_mission.model.AbilityResponse
import com.example.first_week_mission.model.Pokemon
import com.example.first_week_mission.model.PokemonForm
import com.example.first_week_mission.model.PokemonResponse
import com.example.first_week_mission.model.StatResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface PokemonApiService {
    @GET("pokemon-species/{id}")
    suspend fun getPokemonInfo(
        @Path("id") id: Int
    ): Pokemon

    @GET("pokemon-form/{id}")
    suspend fun getPokemonForm(
        @Path("id") id: Int,
    ): PokemonForm

    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): PokemonResponse

    @GET("ability/{name}")
    suspend fun getAbility(
        @Path("name") name: String
    ): AbilityResponse

    @GET("stat/{name}")
    suspend fun getStat(
        @Path("name") name: String
    ): StatResponse
}