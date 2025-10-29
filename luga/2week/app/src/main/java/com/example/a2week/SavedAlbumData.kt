package com.example.a2week

data class SavedAlbumData(
    val img: Int,
    val title: String,
    val singer: String,
    val detail: String,
    var isPlaying: Boolean = false
)
