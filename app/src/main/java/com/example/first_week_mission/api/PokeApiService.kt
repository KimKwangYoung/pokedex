package com.example.first_week_mission.api

import com.example.first_week_mission.model.Pokemon
import com.example.first_week_mission.model.PokemonForm
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon-species/{path}")
    suspend fun getPokemonInfo(
        @Path("path") id: Int
    ): Pokemon

    @GET("pokemon-form/{path}")
    suspend fun getPokemonForm(
        @Path("path") id: Int,
    ): PokemonForm
}