package com.example.first_week_mission.repository

import com.example.first_week_mission.domain.model.Pokemon
import com.example.first_week_mission.domain.model.PokemonDetail
import kotlinx.coroutines.flow.StateFlow

interface PokemonRepository {

    val dataFlow: StateFlow<List<Pokemon>>
    suspend fun loadPokemon()

    suspend fun getPokemonDetail(id: Int): PokemonDetail

    suspend fun like(id: Int)

    suspend fun unlike(id: Int)
}