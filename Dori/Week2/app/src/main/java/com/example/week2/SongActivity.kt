package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        val currentSongFromDB = songDB.songDao().getSong(songId)


        if (MusicService.currentSong?.id != currentSongFromDB.id) {
            MusicService.setAndPlay(this, currentSongFromDB)
        } else {
        }


        setPlayer(currentSongFromDB)
        startTimer()
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


                    MusicService.setAndPlay(this@SongActivity, currentSong, newSecond)


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
    }


    private fun togglePlayPause() {

        MusicService.togglePlayPause(this)
        startTimer()
    }


    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun setPlayer(song: Song){

        binding.tvSongTitle.text = song.title
        binding.tvArtistName.text = song.singer
        binding.ivAlbumCover.setImageResource(song.coverImg!!)


        val totalSecond = MusicService.getDuration() / 1000
        val currentSecond = MusicService.getCurrentPosition() / 1000

        binding.tvTotalTime.text = String.format("%02d:%02d", totalSecond / 60, totalSecond % 60)


        binding.seekBar.max = 1000




        if (MusicService.isPlaying) {
            binding.btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
        } else {
            binding.btnPlay.setImageResource(R.drawable.btn_miniplayer_play)
        }
    }






    private fun moveSong(direct: Int){
        // nowPos를 업데이트
        nowPos = getPlayingSongPosition(MusicService.currentSong?.id ?: 1)

        if (nowPos + direct < 0){
            Toast.makeText(this,"첫 곡입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"마지막 곡입니다.",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct
        val nextSong = songs[nowPos]

        MusicService.setAndPlay(this, nextSong, 0)

        setPlayer(nextSong)
        startTimer()
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
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