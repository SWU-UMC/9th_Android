package com.example.week2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivityAlbumBinding


class AlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val title = intent.getStringExtra("title") ?: "제목 없음"
        val singer = intent.getStringExtra("singer") ?: "가수 정보 없음"

        binding.tvSongTitle.text = title
        binding.tvArtistName.text = singer


        binding.albumBackIb.setOnClickListener {
            finish()
        }
    }
}