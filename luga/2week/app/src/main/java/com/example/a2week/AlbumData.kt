package com.example.a2week

data class AlbumData(
    val img: Int,
    val title: String,
    val singer: String,
    var isPlaying: Boolean = false
)
