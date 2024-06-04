package com.example.first_week_mission.model

import com.google.gson.annotations.SerializedName

data class FlavorText(
    @SerializedName("flavor_text") val flavorText: String,
    val language: Info
)
