package com.kky.pokedex.domain.model

sealed interface Pokemon {
    val id: Int
    val name: String
    val description: String
    val type: List<String>
    val like: Boolean
    val imageUrl: String
}