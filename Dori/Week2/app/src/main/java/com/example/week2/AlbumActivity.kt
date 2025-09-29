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


        val backButton = binding.albumBackIb

        backButton.setOnClickListener {
            finish()
        }
    }
}