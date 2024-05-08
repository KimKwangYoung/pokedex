package com.example.first_week_mission.ui.model

data class PokemonUiModel(
    val id: Int,
    val name: String,
    val description: String,
    val type: List<String>,
    val like: Boolean,
    val imageUrl: String
)
