package com.example.week2

import java.util.ArrayList

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,


    var date: String? = null,
    var type: String? = null,


    var Songs: ArrayList<Song> = ArrayList()
)