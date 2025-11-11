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

        // db
        val db = AppDBProvider.getInstance(this)
        val songs = db.songDao().getAllSongs()

        // songId 불러오기
        val sharedPreferences = getSharedPreferences("songPrefs", MODE_PRIVATE)
        val savedSongId = sharedPreferences.getInt("songId", -1)

        val currentSong = SongManager.currentSong?: run{
            val song = if(savedSongId != -1) songs.find{it.id == savedSongId}?: songs[0] else songs[0]
            SongManager.init(this, song.music, song)
            song
        }
        var nowPos = songs.indexOfFirst { it.id == currentSong.id }

        updateUI(currentSong)

        // 좌측 상단 버튼
        binding.songDownIb.setOnClickListener { finish() }

        // 음악 재생 상태에 따른 버튼 이미지 변화
        binding.songMiniplayerIv.setOnClickListener { SongManager.play() }
        binding.songPauseIv.setOnClickListener { SongManager.pause() }

        // 이전, 다음 음악 이동 기능 추가
        binding.songPreviousIv.setOnClickListener {
            nowPos = if(nowPos - 1 < 0) {songs.size - 1} else nowPos - 1
            val prevSong = songs[nowPos]
            SongManager.changeSong(this, prevSong)
            updateUI(prevSong)
            saveCurrentSongId(prevSong.id)
            resetPlayerProgress()
        }
        binding.songNextIv.setOnClickListener {
            nowPos = (nowPos + 1) % songs.size
            val nextSong = songs[nowPos]
            SongManager.changeSong(this, nextSong)
            updateUI(nextSong)
            saveCurrentSongId(nextSong.id)
            resetPlayerProgress()
        }

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

    private fun updateUI(song: Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer

        if(SongManager.isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    private fun saveCurrentSongId(id: Int) {
        val sharedPreferences = getSharedPreferences("songPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putInt("songId", id).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdatingSeekBar()
        SongManager.removeListener(this)
    }
}