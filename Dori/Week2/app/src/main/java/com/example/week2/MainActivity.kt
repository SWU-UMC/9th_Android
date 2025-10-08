package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

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

        // MiniPlayer의 초기 곡 정보 설정 (MiniPlayer.mainPlayer 레이아웃을 ActivityMainBinding이 include한다고 가정)
        val initialSong = Song("Butter", "방탄소년단 (BTS)") // 초기값 설정
        binding.miniPlayer.tvTitle.text = initialSong.title
        binding.miniPlayer.tvArtist.text = initialSong.singer

        binding.miniPlayer.mainPlayer.setOnClickListener {
            val currentSong = Song(binding.miniPlayer.tvTitle.text.toString(), binding.miniPlayer.tvArtist.text.toString())

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


    fun updateMiniPlayer(title: String, singer: String) {

        binding.miniPlayer.tvTitle.text = title
        binding.miniPlayer.tvArtist.text = singer

        Toast.makeText(this, "MiniPlayer Updated: $title - $singer", Toast.LENGTH_SHORT).show()
    }
}