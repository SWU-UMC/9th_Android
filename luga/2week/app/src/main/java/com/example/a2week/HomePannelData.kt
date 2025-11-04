package com.example.a2week

import java.io.Serializable

data class HomePannelData(
    val title: String,
    val songs: List<AlbumData>
): Serializable