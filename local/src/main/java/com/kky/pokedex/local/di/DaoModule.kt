package com.kky.pokedex.local.di

import com.kky.pokedex.local.AppDatabase
import com.kky.pokedex.local.dao.LikeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideLikeDao(
        database: AppDatabase
    ): LikeDao = database.dao()
}