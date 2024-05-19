package com.example.first_week_mission.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "like")
data class Like(
    @PrimaryKey(autoGenerate = false)
    val id: Int
)
