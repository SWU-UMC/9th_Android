package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    private var song:Song = Song()
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val returnedTitle = result.data?.getStringExtra("RETURNED_TITLE")

            if (!returnedTitle.isNullOrEmpty()) {
                Toast.makeText(this, "SongActivity에서 돌아온 제목: $returnedTitle", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        inputDummySongs()


        val initialSong = Song("Butter", "방탄소년단 (BTS)")
        binding.miniPlayer.tvTitle.text = initialSong.title
        binding.miniPlayer.tvArtist.text = initialSong.singer

        binding.miniPlayer.mainPlayer.setOnClickListener {
            val currentSong = Song(
                binding.miniPlayer.tvTitle.text.toString(),
                binding.miniPlayer.tvArtist.text.toString()
            )


            val songIdToSave: Int = 0

            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songIdToSave)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", currentSong.title)
                putExtra("singer", currentSong.singer)
            }

            resultLauncher.launch(intent)
        }


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

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0){
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        updateMiniPlayer(song)
    }

    fun updateMiniPlayer(song: Song) {

        binding.miniPlayer.tvTitle.text = song.title
        binding.miniPlayer.tvArtist.text = song.singer

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
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )



        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

}