package com.example.a2week

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.a2week.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 제목 & 가수명 세팅
        initSong()
        setPlayer(song)

        // 좌측 상단 버튼
        binding.songDownIb.setOnClickListener {
            finish() // MainActivity로 전환
        }

        // 음악 재생 상태에 따른 버튼 이미지 변화
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPreviousIv.setOnClickListener {
            resetPlayerProgress()
        }

        binding.songNextIv.setOnClickListener {
            resetPlayerProgress()
        }

    }

    private fun resetPlayerProgress(){
        timer.interrupt()
        song.second = 0

        binding.songStartTimeTv.text = "00:00"
        binding.songProgressbarSb.progress = 0

        timer = Timer(song.playTime, song.isPlaying, song.second)
        timer.start()
    }


    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    private fun initSong(){
        // EndTimeTv의 텍스트(03:00) 기준
        val endTimeText = binding.songEndTimeTv.text.toString()
        val playTimeSecond = parseTimeToSeconds(endTimeText)

        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                playTimeSecond,
                intent.getBooleanExtra("isPlaying", false)
            )
        }else{
            song = Song("제목 없음", "가수 없음", 0, playTimeSecond, false)
        }
        startTimer()
    }

    private fun parseTimeToSeconds(time: String): Int{
        val parts = time.split(":")
        if(parts.size != 2) return 0
        val min = parts[0].toIntOrNull() ?: 0
        val sec = parts[1].toIntOrNull() ?: 0
        return min * 60 + sec
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)

        if(song.playTime >0){
            binding.songProgressbarSb.progress = (song.second * 1000 / song.playTime)
        }else{
            binding.songProgressbarSb.progress = 0
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying, song.second)
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true, startSecond: Int = 0):Thread(){
        private var second: Int = startSecond
        private var mills: Float = startSecond * 1000f

        override fun run() {
            super.run()
            try{
                while(true) {
                    if (second >= playTime) {
                        break
                    }
                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressbarSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch(e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다.${e.message}")
            }
        }
    }
}