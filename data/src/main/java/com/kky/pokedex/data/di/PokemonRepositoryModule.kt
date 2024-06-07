package com.kky.pokedex.data.di

import com.kky.pokedex.data.repository.PokemonRepositoryImpl
import com.kky.pokedex.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface PokemonRepositoryModule {
    @Singleton
    @Binds
    fun bindPokemonRepository(pokemonRepositoryImpl: PokemonRepositoryImpl): PokemonRepository
}