package com.example.a2week

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedAlbumTable")
data class SavedAlbumData(
    @PrimaryKey(autoGenerate = true)

    val img: Int,
    val title: String,
    val singer: String,
    val detail: String,
    var isPlaying: Boolean = false
)
