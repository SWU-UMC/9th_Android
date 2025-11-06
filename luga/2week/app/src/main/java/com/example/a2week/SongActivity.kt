package com.example.a2week

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a2week.databinding.ActivitySongBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    private var mediaPlayer: MediaPlayer? = null
    private var updateJob: Job? = null

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

    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying

        mediaPlayer?.let { player ->
            if(isPlaying){
                if(!player.isPlaying){
                    player.start()
                    startUpdatingSeekBar()
                }
                binding.songMiniplayerIv.visibility = View.GONE
                binding.songPauseIv.visibility = View.VISIBLE
            }else{
                if(player.isPlaying){
                    player.pause()
                }
                stopUpdatingSeekBar()
                binding.songMiniplayerIv.visibility = View.VISIBLE
                binding.songPauseIv.visibility = View.GONE
            }
        } ?: run{
            mediaPlayer = MediaPlayer.create(this, R.raw.music_lilac)
        }
    }

    private fun startUpdatingSeekBar(){
        if(updateJob != null) return
        updateJob = lifecycleScope.launch{
            while (isActive && mediaPlayer != null) {
                try {
                    val mp = mediaPlayer ?: break
                    if (mp.isPlaying) {
                        val currentPos = mp.currentPosition
                        binding.songProgressbarSb.progress = currentPos
                        val seconds = currentPos / 1000
                        binding.songStartTimeTv.text =
                            String.format("%02d:%02d", seconds / 60, seconds % 60)
                    }
                } catch (_: IllegalStateException) { break }
                delay(1000)
            }
        }
    }

    private fun stopUpdatingSeekBar(){
        updateJob?.cancel()
        updateJob = null
    }

    private fun resetPlayerProgress() {
        mediaPlayer?.seekTo(0)
        binding.songStartTimeTv.text = "00:00"
        binding.songProgressbarSb.progress = 0

        if(song.isPlaying){
            mediaPlayer?.start()
        }
    }

    private fun initSong(){
        song = if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                0,
                0,
                false
            )
        }else{
            Song("제목 없음", "가수 없음", 0, 0, false)
        }

        // 파일 디스크립터 사용
        val afd = resources.openRawResourceFd(R.raw.music_lilac)?: return
        mediaPlayer = MediaPlayer().apply{
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()

            setOnPreparedListener { mp ->
                val duration = mp.duration / 1000
                binding.songEndTimeTv.text = String.format("%02d:%02d", duration / 60, duration % 60)
                binding.songProgressbarSb.max = mp.duration
                setPlayerStatus(song.isPlaying)
            }
            setOnCompletionListener {
                setPlayerStatus(false)
                binding.songProgressbarSb.progress = 0
                binding.songStartTimeTv.text = "00:00"
            }
            prepareAsync() // 비동기 로드
        }
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = "00:00"

        mediaPlayer?.let {
            val duration = it.duration / 1000
            binding.songEndTimeTv.text = String.format("%02d:%02d", duration / 60, duration % 60)
            binding.songProgressbarSb.max = it.duration
        }

        setPlayerStatus(song.isPlaying)
    }


    override fun onDestroy() {
        super.onDestroy()
        stopUpdatingSeekBar()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}