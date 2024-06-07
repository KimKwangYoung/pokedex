package com.kky.pokedex.network.model

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesResponse(
    val id: Int,
    val names: List<PokemonName>,
    @SerializedName("flavor_text_entries") val descriptions: List<FlavorText>
)

data class PokemonName(
    val name: String,
    val language: Info
)
