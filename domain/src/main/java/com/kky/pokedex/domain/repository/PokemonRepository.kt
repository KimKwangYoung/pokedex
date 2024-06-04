package com.kky.pokedex.domain.repository

import com.kky.pokedex.domain.model.Pokemon
import com.kky.pokedex.domain.model.PokemonDetail

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