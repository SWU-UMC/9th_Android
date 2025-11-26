package com.example.week2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var email: String = "",
    var password: String = "",
    var name: String = ""
)