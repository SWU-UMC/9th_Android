package com.example.a2week

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

// 싱글톤 관리자
object SongManager{
    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false
    var isPlaying = false
        private set
    var currentSong: Song? = null
        private set

    private var lastPos: Int = 0

    interface OnPlaybackStateChangeListener{
        fun onPlay()
        fun onPausePlayback()
        fun onPlaybackStateChanged(position: Int, duration: Int)
    }

    private val listeners = mutableListOf<OnPlaybackStateChangeListener>()

    fun addListener(listener: OnPlaybackStateChangeListener){
        if(!listeners.contains(listener)) listeners.add(listener)
    }

    fun removeListener(listener: OnPlaybackStateChangeListener){
        listeners.remove(listener)
    }

    fun init(context: Context, resId: Int, song: Song) {
        try {
            val isSameSong = (currentSong?.title == song.title) && (currentSong?.singer == song.singer)
            currentSong = song
            if(!isSameSong) lastPos = 0
            isPrepared = false

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                val afd = context.resources.openRawResourceFd(resId)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()

                setOnPreparedListener { mp ->
                    isPrepared = true
                    mp.seekTo(lastPos)
                    listeners.forEach { listener -> listener.onPlaybackStateChanged(0, mp.duration) }
                }
                setOnCompletionListener {
                    this@SongManager.isPlaying = false
                    lastPos = 0
                    listeners.forEach { it.onPausePlayback() }
                }

                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("SongManager", "Init failed: ${e.message}")
        }
    }


    fun play(){
        mediaPlayer?.let{ mp ->
            if(!isPrepared){
                mp.setOnPreparedListener { preparedMp ->
                    isPrepared = true
                    preparedMp.start()
                    isPlaying = true
                    listeners.forEach { it.onPlay() }
                    listeners.forEach { listener -> listener.onPlaybackStateChanged(preparedMp.currentPosition, preparedMp.duration) }
                }
            }
            else{
                try{
                    mp.start()
                    isPlaying = true
                    listeners.forEach { it.onPlay() }
                } catch(e:IllegalStateException){
                    Log.e("SongManager", "Play failed: ${e.message}")
                }
            }
        }
    }

    fun pause(){
        mediaPlayer?.let {
            try{
                it.pause()
                lastPos = it.currentPosition
                isPlaying = false
                listeners.forEach{ it.onPausePlayback() }
            } catch (e: IllegalStateException){
                Log.e("SongManager", "Pause failed: ${e.message}")
            }
        }
    }

    fun seekTo(position: Int){
        mediaPlayer?.let{
            try{
                it.seekTo(position)
                lastPos = position
                listeners.forEach { listener -> listener.onPlaybackStateChanged(position, it.duration) }
            } catch (e: IllegalStateException){
                Log.e("SongManager", "Seek failed: ${e.message}")
            }
        }
    }

    fun updateProgress() {
        mediaPlayer?.let {
            lastPos = it.currentPosition
            listeners.forEach { listener -> listener.onPlaybackStateChanged(lastPos, it.duration) }
        }
    }

    fun release(){
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
        isPlaying = false
        lastPos = 0
    }
}