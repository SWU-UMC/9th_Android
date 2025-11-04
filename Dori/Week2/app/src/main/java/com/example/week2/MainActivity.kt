package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var song:Song = Song()


    private val SONG_PREFERENCE = "song"
    private val CURRENT_SECOND_KEY = "currentSecond"
    private val IS_PLAYING_KEY = "isPlaying"


    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBar = object : Runnable {
        override fun run() {


            val currentSecond = MusicService.getCurrentPosition() / 1000
            val isPlaying = MusicService.isPlaying


            if (song.playTime > 0) {
                val progress = (currentSecond * 1000 / song.playTime)
                binding.miniPlayer.miniPlayerSeekBar.progress = progress
            } else {
                binding.miniPlayer.miniPlayerSeekBar.progress = 0
            }


            if (isPlaying) {
                binding.miniPlayer.btnPlayArrow2.setImageResource(R.drawable.btn_play_pause)
            } else {
                binding.miniPlayer.btnPlayArrow2.setImageResource(R.drawable.btn_play_arrow)
            }



            if (isPlaying) {
                handler.postDelayed(this, 1000)
            }
        }
    }


    private fun startMiniPlayerTimer() {
        handler.removeCallbacks(updateSeekBar)
        handler.post(updateSeekBar)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val returnedTitle = result.data?.getStringExtra("RETURNED_TITLE")

            if (!returnedTitle.isNullOrEmpty()) {
                Toast.makeText(this, "SongActivity에서 돌아온 제목: $returnedTitle", Toast.LENGTH_LONG).show()
            }


            if (MusicService.currentSong != null) {
                song = MusicService.currentSong!!
            }

            updateMiniPlayer(song)
            startMiniPlayerTimer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()


        val initialSongId = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE).getInt("songId", 1)
        val songDB = SongDatabase.getInstance(this)!!
        song = songDB.songDao().getSong(initialSongId)


        if (MusicService.currentSong == null) {
            MusicService.currentSong = song
        } else {
            song = MusicService.currentSong!!
        }

        binding.miniPlayer.tvTitle.text = song.title
        binding.miniPlayer.tvArtist.text = song.singer

        binding.miniPlayer.mainPlayer.setOnClickListener {


            val editor = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.putInt(CURRENT_SECOND_KEY, MusicService.getCurrentPosition() / 1000)
            editor.putBoolean(IS_PLAYING_KEY, MusicService.isPlaying)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)

            resultLauncher.launch(intent)
        }


        binding.miniPlayer.btnPlayArrow2.setOnClickListener {

            MusicService.togglePlayPause(this)


            startMiniPlayerTimer()
        }


        binding.miniPlayer.miniPlayerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                handler.removeCallbacks(updateSeekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    if (song.playTime > 0) {
                        val newProgressRatio = it.progress.toFloat() / it.max.toFloat()
                        val newSecond = (song.playTime * newProgressRatio).toInt()


                        MusicService.setAndPlay(this@MainActivity, song, newSecond)


                        val editor = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE).edit()
                        editor.putInt(CURRENT_SECOND_KEY, newSecond)
                        editor.putBoolean(IS_PLAYING_KEY, true)
                        editor.apply()

                        song.second = newSecond
                    }


                    startMiniPlayerTimer()
                }
            }
        })


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainer, HomeFragment())
                .commit()
        }


        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.lookerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, LockerFragment())
                        .commit()
                    true
                }

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, HomeFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }


    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences(SONG_PREFERENCE, MODE_PRIVATE)
        val songId = spf.getInt("songId",1)

        val songDB = SongDatabase.getInstance(this)!!
        song = songDB.songDao().getSong(songId)

        val currentSecond = spf.getInt(CURRENT_SECOND_KEY, 0)
        val isPlayingState = spf.getBoolean(IS_PLAYING_KEY, false)

        MusicService.setAndPlay(this, song, currentSecond)
        if (!isPlayingState) MusicService.pause()
        Log.d("song ID", song.id.toString())
        updateMiniPlayer(song)
        startMiniPlayerTimer()
    }

    fun updateMiniPlayer(song: Song) {

        binding.miniPlayer.tvTitle.text = song.title
        binding.miniPlayer.tvArtist.text = song.singer

        binding.miniPlayer.miniPlayerSeekBar.max = 1000


        val currentPosition = MusicService.getCurrentPosition() / 1000
        if (song.playTime > 0) {
            binding.miniPlayer.miniPlayerSeekBar.progress = (currentPosition * 1000 / song.playTime)
        } else {
            binding.miniPlayer.miniPlayerSeekBar.progress = 0
        }


        if (MusicService.isPlaying) {
            binding.miniPlayer.btnPlayArrow2.setImageResource(R.drawable.btn_play_pause)
        } else {
            binding.miniPlayer.btnPlayArrow2.setImageResource(R.drawable.btn_play_arrow)
        }

        Toast.makeText(this, "MiniPlayer Updated: ${song.title} - ${song.singer}", Toast.LENGTH_SHORT).show()
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                1,
                212,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                2,
                180,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )


        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }


    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateSeekBar)
    }

}