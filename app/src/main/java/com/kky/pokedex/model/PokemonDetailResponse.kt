package com.kky.pokedex.model

import com.google.gson.annotations.SerializedName

internal data class PokemonResponse(
    val id: Long,
    val stats: List<StatInfo>,
    val abilities: List<AbilityInfo>,
    val weight: Int,
    val height: Int
)

internal data class StatInfo(
    @SerializedName("base_stat") val baseStat:Int,
    @SerializedName("stat") val stat: Info
)

internal data class AbilityInfo(
    val ability: Info
)