package com.example.first_week_mission.domain.model

data class PokemonSummary(
    override val id: Int,
    override val name: String,
    override val description: String,
    override val type: List<String>,
    override val like: Boolean,
    override val imageUrl: String
) : Pokemon
