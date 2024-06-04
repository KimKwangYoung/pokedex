package com.kky.pokedex.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kky.pokedex.local.dao.LikeDao
import com.kky.pokedex.local.entity.Like

@Database(entities = [Like::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao(): LikeDao
}