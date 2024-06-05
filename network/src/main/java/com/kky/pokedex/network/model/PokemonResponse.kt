package com.kky.pokedex.network.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val id: Long,
    val stats: List<StatInfo>,
    val abilities: List<AbilityInfo>,
    val weight: Int,
    val height: Int
)

data class StatInfo(
    @SerializedName("base_stat") val baseStat:Int,
    @SerializedName("stat") val stat: Info
)

data class AbilityInfo(
    val ability: Info
)