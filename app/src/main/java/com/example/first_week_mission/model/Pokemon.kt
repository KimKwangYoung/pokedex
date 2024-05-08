package com.example.first_week_mission.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val names: List<PokemonName>,
    @SerializedName("flavor_text_entries") val descriptions: List<Description>
)

data class PokemonName(
    val name: String,
    val language: Language
)

data class Description(
    @SerializedName("flavor_text") val description: String,
    val language: Language
)

data class PokemonForm(
    @SerializedName("sprites") val images: PokemonImage,
    val types: List<PokemonTypes>
)

data class PokemonImage(
    @SerializedName("front_default") val defaultImage: String,
)

data class PokemonTypes(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String,
)
