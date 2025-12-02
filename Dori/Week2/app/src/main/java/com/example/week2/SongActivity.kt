package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var albumTitle: String? = null
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0


    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBar = object : Runnable {
        override fun run() {
            if (MusicService.currentSong == null) return

            val currentSong = MusicService.currentSong!!
            val currentMills = MusicService.getCurrentPosition()
            val totalMills = MusicService.getDuration()


            if (totalMills > 0) {
                val progress = ((currentMills.toFloat() / totalMills.toFloat()) * 1000).toInt()
                binding.seekBar.progress = progress
            }

            //타이머
            val currentSecond = currentMills / 1000
            val totalSecond = totalMills / 1000
            binding.tvCurrentTime.text = String.format("%02d:%02d", currentSecond / 60, currentSecond % 60)
            binding.tvTotalTime.text = String.format("%02d:%02d", totalSecond / 60, totalSecond % 60)


            if (MusicService.isPlaying) {
                binding.btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
                handler.postDelayed(this, 50)
            } else {
                binding.btnPlay.setImageResource(R.drawable.btn_miniplayer_play)
            }


            if (currentMills >= totalMills && totalMills > 0) {

                handler.removeCallbacks(this)

            }
        }
    }

    private fun startTimer() {
        handler.removeCallbacks(updateSeekBar)
        handler.post(updateSeekBar)
    }


    private val SONG_PREFERENCE = "song"
    private val CURRENT_SECOND_KEY = "currentSecond"
    private val IS_PLAYING_KEY = "isPlaying"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initClickListener()
        initSeekBarListener()

        albumTitle = intent.getStringExtra("title")
    }

    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE)
        val songId = spf.getInt("songId", 1)

        val songDB = SongDatabase.getInstance(this)!!


        var currentSongFromDB = songDB.songDao().getSong(songId)


        if (currentSongFromDB == null) {
            // DB에 있는 모든 노래를 가져와서 그 중 첫 번째 곡을 쓴다
            val allSongs = songDB.songDao().getSongs()

            if (allSongs.isNotEmpty()) {
                currentSongFromDB = allSongs[0]


                val editor = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE).edit()
                editor.putInt("songId", currentSongFromDB.id)
                editor.apply()
            } else {

                Toast.makeText(this, "노래 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }


        if (MusicService.currentSong?.id != currentSongFromDB!!.id) {
            MusicService.setAndPlay(this, currentSongFromDB!!, 0, true)
        }

        setPlayer(currentSongFromDB!!)
        startTimer()

        Log.d("SongActivity_DEBUG", "로드된 곡 ID: ${currentSongFromDB!!.id}")
    }


    private fun initSeekBarListener() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && MusicService.currentSong != null) {
                    val totalSecond = MusicService.getDuration() / 1000
                    val newProgressRatio = progress.toFloat() / 1000f
                    val newSecond = (totalSecond * newProgressRatio).toInt()
                    binding.tvCurrentTime.text = String.format("%02d:%02d", newSecond / 60, newSecond % 60)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                handler.removeCallbacks(updateSeekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    if (MusicService.currentSong == null) return

                    val currentSong = MusicService.currentSong!!
                    val totalSecond = MusicService.getDuration() / 1000
                    val newProgressRatio = it.progress.toFloat() / 1000f
                    val newSecond = (totalSecond * newProgressRatio).toInt()


                    MusicService.setAndPlay(this@SongActivity, currentSong, newSecond, true)


                    startTimer()
                }
            }
        })
    }

    private fun initClickListener(){
        binding.btnPlay.setOnClickListener {
            togglePlayPause()
        }

        binding.songDownIb.setOnClickListener {
            handler.removeCallbacks(updateSeekBar)

            returnResultToMainActivity()
            finish()
        }

        binding.btnNext.setOnClickListener {
            moveSong(+1)
        }

        binding.btnPrevious.setOnClickListener {
            moveSong(-1)
        }

        binding.ivLike.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }


    private fun togglePlayPause() {

        MusicService.togglePlayPause(this)
        startTimer()
    }


    private fun setLike(isLike: Boolean){

        songs[nowPos].isLike = !isLike


        songDB.songDao().update(songs[nowPos])


        if (!isLike){
            binding.ivLike.setImageResource(R.drawable.ic_my_like_on)
            Toast.makeText(this, "보관함에 담았습니다.", Toast.LENGTH_SHORT).show()
        } else {
            binding.ivLike.setImageResource(R.drawable.ic_my_like_off)
            Toast.makeText(this, "보관함에서 삭제했습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.clear() // 기존 리스트 비우고
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0 // 못 찾으면 0번
    }

    private fun setPlayer(song: Song){

        binding.tvSongTitle.text = song.title
        binding.tvArtistName.text = song.singer
        binding.ivAlbumCover.setImageResource(song.coverImg ?: R.drawable.img_album_exp)


        val totalSecond = MusicService.getDuration() / 1000


        binding.tvTotalTime.text = String.format("%02d:%02d", totalSecond / 60, totalSecond % 60)
        binding.seekBar.max = 1000




        if (MusicService.isPlaying) {
            binding.btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
        } else {
            binding.btnPlay.setImageResource(R.drawable.btn_miniplayer_play)
        }

        if (song.isLike) {
            binding.ivLike.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.ivLike.setImageResource(R.drawable.ic_my_like_off)
        }
    }






    private fun moveSong(direct: Int){
        if (songs.isEmpty()) return

        nowPos = getPlayingSongPosition(MusicService.currentSong?.id ?: 1)
        nowPos += direct

        if (nowPos >= songs.size){
            nowPos = 0
            Toast.makeText(this,"마지막 곡입니다.",Toast.LENGTH_SHORT).show()
        }
        if (nowPos < 0){
            nowPos = songs.size - 1
        }

        val nextSong = songs[nowPos]
        MusicService.setAndPlay(this, nextSong, 0, true)
        setPlayer(nextSong)

        val editor = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE).edit()
        editor.putInt("songId", nextSong.id)
        editor.apply()
    }



    private fun returnResultToMainActivity() {
        albumTitle?.let { title ->
            val resultIntent = Intent().apply {
                putExtra("RETURNED_TITLE", title)
            }
            setResult(RESULT_OK, resultIntent)
        }
        if (albumTitle == null) {
            setResult(RESULT_CANCELED)
        }


        val editor = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE).edit()
        editor.putInt("songId", MusicService.currentSong?.id ?: 1)
        editor.putInt(CURRENT_SECOND_KEY, MusicService.getCurrentPosition() / 1000)
        editor.putBoolean(IS_PLAYING_KEY, MusicService.isPlaying)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBar)


    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateSeekBar)


    }
}