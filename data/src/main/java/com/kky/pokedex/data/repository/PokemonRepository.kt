package com.kky.pokedex.data.repository

import com.kky.pokedex.domain.model.Pokemon
import com.kky.pokedex.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun flowPokemon(): Flow<List<Pokemon>>

    fun flowPokemonDetail(id: Int): Flow<PokemonDetail>

    fun setShowOnlyLike(showOnlyLike: Boolean)

    suspend fun loadPokemon()

    suspend fun like(id: Int)

    suspend fun unlike(id: Int)
}