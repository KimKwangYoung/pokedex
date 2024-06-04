package com.kky.pokedex.service

import com.kky.pokedex.model.AbilityResponse
import com.kky.pokedex.model.Pokemon
import com.kky.pokedex.model.PokemonForm
import com.kky.pokedex.model.PokemonResponse
import com.kky.pokedex.model.StatResponse
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