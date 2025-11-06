package com.example.a2week

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a2week.databinding.ActivitySongBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SongActivity : AppCompatActivity(), SongManager.OnPlaybackStateChangeListener {
    lateinit var binding: ActivitySongBinding
    private var updateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SongManager.addListener(this)

        // 현재 재생 중인 곡 정보
        val song = SongManager.currentSong?: Song("Lilac", "IU")
        val resId = R.raw.music_lilac

        if(SongManager.currentSong == null) { SongManager.init(this, resId, song) }

        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer

        // 좌측 상단 버튼
        binding.songDownIb.setOnClickListener { finish() }

        // 음악 재생 상태에 따른 버튼 이미지 변화
        binding.songMiniplayerIv.setOnClickListener { SongManager.play() }
        binding.songPauseIv.setOnClickListener { SongManager.pause() }
        binding.songPreviousIv.setOnClickListener { resetPlayerProgress() }
        binding.songNextIv.setOnClickListener { resetPlayerProgress() }

        startUpdatingSeekBar()
    }

    override fun onPlay(){
        runOnUiThread {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }

    override fun onPausePlayback(){
        runOnUiThread {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    override fun onPlaybackStateChanged(position: Int, duration: Int){
        runOnUiThread {
            binding.songProgressbarSb.max = duration
            binding.songProgressbarSb.progress = position

            val posSec = position / 1000
            val durSec = duration / 1000
            binding.songStartTimeTv.text = String.format("%02d:%02d", posSec / 60, posSec % 60)
            binding.songEndTimeTv.text = String.format("%02d:%02d", durSec / 60, durSec % 60)
        }
    }

    private fun startUpdatingSeekBar(){
        if(updateJob != null) return
        updateJob = lifecycleScope.launch{
            while (isActive) {
                if(SongManager.isPlaying) SongManager.updateProgress()
                delay(1000)
            }
        }
    }

    private fun stopUpdatingSeekBar(){
        updateJob?.cancel()
        updateJob = null
    }

    private fun resetPlayerProgress() {
        SongManager.seekTo(0)
        binding.songProgressbarSb.progress = 0
        binding.songStartTimeTv.text = "00:00"
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdatingSeekBar()
        SongManager.removeListener(this)
    }
}