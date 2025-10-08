package com.example.a2week

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a2week.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    // SongActivity에서 결과값 받기
    private val songActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val albumTitle = result.data?.getStringExtra("albumTitle")
                if (!albumTitle.isNullOrEmpty()) {
                    Toast.makeText(this, "앨범명 : $albumTitle", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = Song(
            binding.mainMiniplayerTitleTv.text.toString(),
            binding.mainMiniplayerSingerTv.text.toString())

        // SongActivity 실행
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            songActivityLauncher.launch(intent)
        }

        initBottomNavigation()
    }

    // 미니 플레이어 업데이트 메서드
    fun updateMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
    }

    // 하단 네비게이션 바 버튼 별 화면 전환 메서드
    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}