package com.example.a2week

import androidx.room.*

@Dao
interface AlbumDao {
    @Insert
    fun insertAlbum(album: AlbumData)

    @Query("SELECT * FROM AlbumTable")
    fun getAllAlbums(): List<AlbumData>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbumById(id: Int): AlbumData

    @Query("SELECT * FROM AlbumTable WHERE title = :title")
    fun getAlbumByTitle(title: String): List<AlbumData>

    @Query("SELECT * FROM AlbumTable WHERE singer = :singer")
    fun getSongBySinger(singer: String): List<AlbumData>

    @Update
    fun updateAlbum(album: AlbumData)
}