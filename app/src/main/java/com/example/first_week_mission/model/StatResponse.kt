package com.example.first_week_mission.model

data class StatResponse(
    val name: String,
    val names: List<StatName>
)

data class StatName(
    val language: Info,
    val name: String
)
