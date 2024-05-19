package com.example.first_week_mission.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_week_mission.local.entity.Like

@Dao
interface LikeDao {
    @Query("SELECT * FROM `like`")
    fun getLikes()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLike(like: Like)

    @Delete
    fun deleteLike(like: Like)
}