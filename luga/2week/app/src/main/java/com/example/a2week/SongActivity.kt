package com.example.a2week

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.a2week.databinding.ActivitySongBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class SongActivity : AppCompatActivity(), SongManager.OnPlaybackStateChangeListener {
    lateinit var binding: ActivitySongBinding
    private var updateJob: Job? = null

    //firebase
    private val firebaseDb = Firebase.database.getReference("likes")
    private var likeListener: ValueEventListener? = null



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SongManager.addListener(this)

        // db
//        val db = AppDBProvider.getInstance(this)
//        val songs = db.songDao().getAllSongs()
        val songs = mutableListOf(
            Song(id = 1, title = "노래1", singer = "가수1", music = R.raw.music_lilac),
            Song(id = 2, title = "노래2", singer = "가수2", music = R.raw.music_lilac),
            Song(id = 3, title = "노래3", singer = "가수3", music = R.raw.music_lilac)
        )

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
        observeLikeStatus(currentSong)

        // 좌측 상단 버튼
        binding.songDownIb.setOnClickListener { finish() }

        // 음악 재생 상태에 따른 버튼 이미지 변화
        binding.songMiniplayerIv.setOnClickListener {
            val song = SongManager.currentSong
            if(song != null){
                // MediaPlayer가 초기화 안 되어있으면 init
                if(!SongManager.isPrepared) SongManager.init(this, song.music, song)
                SongManager.play()
            }
        }

        binding.songPauseIv.setOnClickListener {
            val song = SongManager.currentSong
            if(song != null){
                if(!SongManager.isPrepared) SongManager.init(this, song.music, song)
                SongManager.pause()
            }
        }

        // 이전, 다음 음악 이동 기능 추가
        binding.songPreviousIv.setOnClickListener {
            nowPos = if(nowPos - 1 < 0) {songs.size - 1} else nowPos - 1
            val prevSong = songs[nowPos]
            SongManager.changeSong(this, prevSong)
            updateUI(prevSong)
            saveCurrentSongId(prevSong.id)
            resetPlayerProgress()
            observeLikeStatus(prevSong)
        }
        binding.songNextIv.setOnClickListener {
            nowPos = (nowPos + 1) % songs.size
            val nextSong = songs[nowPos]
            SongManager.changeSong(this, nextSong)
            updateUI(nextSong)
            saveCurrentSongId(nextSong.id)
            resetPlayerProgress()
            observeLikeStatus(nextSong)
        }

        // 하트 버튼
        binding.songLikeIv.setOnClickListener {
            val currentSong = SongManager.currentSong?: return@setOnClickListener
    //        val db = AppDBProvider.getInstance(this)

            currentSong.isLike = !currentSong.isLike
            updateLikeIcon(currentSong.isLike)

            songs[nowPos].isLike = currentSong.isLike
            firebaseDb.child(currentSong.id.toString()).setValue(currentSong.isLike)
    //        db.songDao().updateSong(currentSong)
        }

        startUpdatingSeekBar()
        SongManager.currentSong?.let{updateLikeIcon(it.isLike)}
    }

    // firebase 실시간 연동
    private fun observeLikeStatus(song: Song){
        likeListener?.let{firebaseDb.child(song.id.toString()).removeEventListener(it)}
        likeListener = firebaseDb.child(song.id.toString())
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isLiked = snapshot.getValue(Boolean::class.java) ?: false
                    song.isLike = isLiked
                    updateLikeIcon(isLiked)
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.e("Firebase", "Database error", error.toException())
                }
            })

    }

    private fun updateLikeIcon(isLike: Boolean){
        binding.songLikeIv.setImageResource(
            if (isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
        )
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
        likeListener?.let{firebaseDb.removeEventListener(it)}
    }
}