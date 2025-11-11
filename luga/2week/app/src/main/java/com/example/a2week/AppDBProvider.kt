package com.example.a2week

import android.content.Context
import androidx.room.Room

object AppDBProvider {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase{
        if(instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "musicDB"
            )
                .allowMainThreadQueries()
                .build()
        }
        return instance!!
    }
}