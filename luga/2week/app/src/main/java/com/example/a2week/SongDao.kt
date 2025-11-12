package com.example.a2week

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insertSong(song: Song)

    @Update
    fun updateSong(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getAllSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getSongByAlbum(albumIdx: Int): List<Song>

    @Query("SELECT * FROM SongTable WHERE title = :title")
    fun getSongByTitle(title: String): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSongById(id: Int): Song

    @Query("SELECT * FROM SongTable WHERE singer = :singer")
    fun getSongBySinger(singer: String): List<Song>
}