package com.example.a2week

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongListTable")
data class SongListData(
    @PrimaryKey(autoGenerate = true)

    val order: String,
    val title: String,
    val singer: String
)
