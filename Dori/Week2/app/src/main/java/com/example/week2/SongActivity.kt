package com.example.week2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivitySongBinding


class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var albumTitle: String? = null


    private var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val title = intent.getStringExtra("title")
        val singer = intent.getStringExtra("singer")


        binding.tvSongTitle.text = title
        binding.tvArtistName.text = singer

        albumTitle = title


        binding.btnPlay.setOnClickListener {
            togglePlayPause()
        }

        binding.songDownIb.setOnClickListener {
            returnResultToMainActivity()
            finish()
        }
    }

    private fun togglePlayPause() {
        isPlaying = !isPlaying

        if (isPlaying) {

            binding.btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
        } else {

            binding.btnPlay.setImageResource(R.drawable.btn_miniplayer_play)
        }
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
    }
}