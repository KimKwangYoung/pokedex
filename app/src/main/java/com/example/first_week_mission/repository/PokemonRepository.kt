package com.example.first_week_mission.repository

import com.example.first_week_mission.domain.model.Pokemon
import com.example.first_week_mission.domain.model.PokemonDetail
import kotlinx.coroutines.flow.StateFlow

interface PokemonRepository {
    suspend fun loadPokemon()

    suspend fun getPokemonDetail(id: Int): PokemonDetail

    suspend fun like(id: Int)

    suspend fun unlike(id: Int)

    fun addDataChangeListener(receiver: Any, listener: DataChangeListener)

    fun removeDataChangeListener(receiver: Any)

    fun clear()

    fun interface DataChangeListener {
        fun onDataChanged(data: List<Pokemon>)
    }
}