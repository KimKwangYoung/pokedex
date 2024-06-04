package com.kky.pokedex.model

import com.google.gson.annotations.SerializedName

data class AbilityResponse(
    @SerializedName("flavor_text_entries") val flavorTexts: List<FlavorText>
)
