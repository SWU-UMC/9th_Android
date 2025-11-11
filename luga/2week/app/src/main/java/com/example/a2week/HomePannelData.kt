package com.example.a2week

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomePannelData(
    val title: String,
    val songs: List<AlbumData>
) : Parcelable