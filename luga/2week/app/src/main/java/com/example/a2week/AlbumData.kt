package com.example.a2week

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumData(
    val img: Int,
    val title: String,
    val singer: String,
    var isPlaying: Boolean = false
) : Parcelable
