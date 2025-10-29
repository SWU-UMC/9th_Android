package com.example.a2week

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.a2week.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false
    private var currentPosition = 0
    private var totalDuration = 300 // 3분 음악이라 가정

    private var progressAnimator: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 제목 & 가수명 세팅
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        // 좌측 상단 버튼
        binding.songDownIb.setOnClickListener {
            val resultIntent = Intent().apply{
                putExtra("albumTitle", binding.songMusicTitleTv.text.toString())
            }
            setResult(RESULT_OK, resultIntent) // 결과 코드 & 데이터 설정
            finish() // MainActivity로 전환
        }

        // 음악 재생 상태에 따른 버튼 이미지 변화
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPreviousIv.setOnClickListener { resetProgress() }
        binding.songNextIv.setOnClickListener { resetProgress() }

    }

    fun setPlayerStatus(isPlaying : Boolean){
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            startProgress()
            startTimer()
        }
        else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            stopProgress()
            handler.removeCallbacks(updateTime)
        }
    }

    private fun startProgress(){
        val totalWidth = binding.songProgressbarBackgroundView.width
        progressAnimator = ValueAnimator.ofInt(
            (currentPosition * totalWidth) / totalDuration,
            totalWidth
        ).apply {
            duration = (totalDuration - currentPosition) * 1000L
            addUpdateListener { animator ->
                val newWidth = animator.animatedValue as Int
                val params = binding.songProgressbarView.layoutParams
                params.width = newWidth
                binding.songProgressbarView.layoutParams = params
            }
            start()
        }
    }

    private fun stopProgress() {
        progressAnimator?.cancel()
    }

    private fun startTimer() {
        handler.post(updateTime)
    }

    private val updateTime = object : Runnable {
        override fun run() {
            if (isPlaying) {
                if (currentPosition < totalDuration) {
                    currentPosition++
                    binding.songStartTimeTv.text = formatTime(currentPosition)
                    handler.postDelayed(this, 1000)
                } else {
                    // 끝나면 자동 정지
                    setPlayerStatus(false)
                    currentPosition = 0
                    binding.songStartTimeTv.text = "00:00"
                    resetProgress()
                }
            }
        }
    }

    private fun resetProgress() {
        stopProgress()
        currentPosition = 0
        binding.songStartTimeTv.text = "00:00"
        val params = binding.songProgressbarView.layoutParams
        params.width = 0
        binding.songProgressbarView.layoutParams = params
        if (isPlaying) startProgress()
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTime)
        stopProgress()
    }
}