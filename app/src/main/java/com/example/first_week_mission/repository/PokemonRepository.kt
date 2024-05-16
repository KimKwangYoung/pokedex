package com.example.first_week_mission.repository

import com.example.first_week_mission.ui.model.PokemonUiModel

interface PokemonRepository {
    suspend fun loadPokemon(fromID: Int, toID: Int): List<PokemonUiModel>
}