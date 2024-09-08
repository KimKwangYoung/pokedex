package com.kky.pokedex.main.navigation

import kotlinx.serialization.Serializable

interface Route {
    @Serializable
    data object Main : Route

    @Serializable
    data class Detail(val id: Int) : Route
}
