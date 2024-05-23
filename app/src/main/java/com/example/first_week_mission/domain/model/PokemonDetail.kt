package com.example.first_week_mission.domain.model

data class PokemonDetail(
    private val pokemonSummary: PokemonSummary,
    val height: Float,
    val weight: Int,
    val stat: List<Stat>,
    val ability: List<String>,
    override val like: Boolean
): Pokemon by pokemonSummary


