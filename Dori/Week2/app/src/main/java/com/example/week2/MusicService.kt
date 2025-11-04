package com.example.week2

import android.content.Context
import android.media.MediaPlayer

object MusicService {
    private var mediaPlayer: MediaPlayer? = null


    var currentSong: Song? = null


    var isPlaying: Boolean = false


    fun setAndPlay(context: Context, song: Song, startSecond: Int = 0) {
        if (currentSong?.id != song.id || mediaPlayer == null) {

            releaseMediaPlayer()
            currentSong = song

            val musicId = context.resources.getIdentifier(song.music, "raw", context.packageName)
            mediaPlayer = MediaPlayer.create(context, musicId)
        }


        val seekPosition = startSecond * 1000
        mediaPlayer?.seekTo(seekPosition)

        // 재생 시작
        mediaPlayer?.start()
        isPlaying = true
    }


    fun togglePlayPause(context: Context) {
        if (mediaPlayer == null && currentSong != null) {

            setAndPlay(context, currentSong!!, getCurrentPosition() / 1000)
            return
        }

        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
        } else {
            mediaPlayer?.start()
            isPlaying = true
        }
    }


    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }


    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }


    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
}