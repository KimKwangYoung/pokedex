package com.example.first_week_mission.domain.model

sealed interface Pokemon {
    val id: Int
    val name: String
    val description: String
    val type: List<String>
    val like: Boolean
    val imageUrl: String
}