package com.kky.pokedex.network.model

import com.google.gson.annotations.SerializedName

data class PokemonFormResponse(
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