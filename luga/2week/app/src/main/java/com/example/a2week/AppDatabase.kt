package com.example.a2week

import androidx.room.*

@Database(entities = [AlbumData::class, Song::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun songDao(): SongDao
}