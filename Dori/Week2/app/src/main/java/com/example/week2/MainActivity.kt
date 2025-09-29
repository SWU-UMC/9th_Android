package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts



data class Song(val title: String, val singer: String)
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

        val song = Song(binding.miniPlayer.tvTitle.text.toString(), binding.miniPlayer.tvArtist.text.toString())


        binding.miniPlayer.mainPlayer.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", song.title)
                putExtra("singer", song.singer)
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
                        .replace(R.id.main_fragmentContainer, Locker())
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
}