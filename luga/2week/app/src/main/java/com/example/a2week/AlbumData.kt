package com.example.a2week

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "AlbumTable")
@Parcelize
data class AlbumData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    val img: Int,
    val title: String,
    val singer: String,
    var isPlaying: Boolean = false
) : Parcelable
