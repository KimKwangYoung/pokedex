package com.kky.pokedex.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kky.pokedex.local.entity.Like

@Dao
interface LikeDao {
    @Query("SELECT * FROM `like`")
    suspend fun getLikes(): List<Like>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLike(like: Like)

    @Delete
    suspend fun deleteLike(like: Like)
}