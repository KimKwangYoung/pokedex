package com.kky.pokedex.network.service

import com.kky.pokedex.network.model.AbilityResponse
import com.kky.pokedex.network.model.PokemonSpeciesResponse
import com.kky.pokedex.network.model.PokemonFormResponse
import com.kky.pokedex.network.model.PokemonResponse
import com.kky.pokedex.network.model.StatResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {
    @GET("pokemon-species/{id}")
    suspend fun getPokemonInfo(
        @Path("id") id: Int
    ): PokemonSpeciesResponse

    @GET("pokemon-form/{id}")
    suspend fun getPokemonForm(
        @Path("id") id: Int,
    ): PokemonFormResponse

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