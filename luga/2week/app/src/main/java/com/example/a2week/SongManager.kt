package com.example.a2week

import android.util.Log

object SongManager{
    var song: Song? = null
    var isPlaying: Boolean = false
    var timer: Timer? = null

    fun start(song: Song){
        this.song = song
        this.isPlaying = song.isPlaying

        timer?.interrupt()
        timer = Timer(song.playTime, song.isPlaying, song.second)
        timer?.start()
    }

    fun pause(){
        song?.isPlaying = false
        isPlaying = false
        timer?.isPlaying = false
    }

    fun restart(){
        song?.isPlaying = true
        isPlaying = true
        timer?.isPlaying = true
    }

    fun reset(){
        song?.second = 0
        timer?.interrupt()
        timer = Timer(song?.playTime?: 0, song?.isPlaying?: false, 0)
        timer?.start()
    }
    
    fun getProgress(): Int{
        val song = song?: return 0
        return (song.second * 1000 / song.playTime)
    }

    class Timer(private val playTime: Int, var isPlaying: Boolean = true, startSecond: Int = 0):Thread(){
        var second: Int = startSecond
        private var lastTime = System.currentTimeMillis()

        override fun run() {
            try{
                while(second < playTime) {
                    if (isPlaying) {
                        val now = System.currentTimeMillis()
                        if(now - lastTime >= 1000){
                            second++
                            lastTime += 1000
                            song?.second = second
                        }
                    }
                    sleep(200)
                }
            } catch(e: InterruptedException){
                Log.d("SongManager", "타이머 중단.${e.message}")
            }
        }

        fun stopTimer(){
            timer?.interrupt()
            timer = null
        }
    }
}