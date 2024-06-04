package com.example.first_week_mission.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_week_mission.local.entity.Like
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {
    @Query("SELECT * FROM `like`")
    suspend fun getLikes(): List<Like>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLike(like: Like)

    @Delete
    suspend fun deleteLike(like: Like)
}