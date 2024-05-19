package com.example.first_week_mission.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.first_week_mission.local.dao.LikeDao
import com.example.first_week_mission.local.entity.Like

@Database(entities = [Like::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao(): LikeDao
}