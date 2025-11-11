package com.example.a2week

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.a2week.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),SongManager.OnPlaybackStateChangeListener {

    lateinit var binding : ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var progressTask : Runnable? = null
    private var splashFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !splashFinished }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.postDelayed({splashFinished = true}, 2000)

        // SongManager 초기화
        SongManager.addListener(this)

        // SongActivity 실행
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainMiniplayerBtnPlayIv.setOnClickListener { SongManager.play() }
        binding.mainMiniplayerBtnPauseIv.setOnClickListener { SongManager.pause() }

        initBottomNavigation()
        startProgressUpdate()
        updateMiniPlayerUI()
    }

    private fun startProgressUpdate(){
        if(progressTask != null) return

        progressTask = object: Runnable{
            override fun run(){
                if(SongManager.isPlaying){
                    SongManager.updateProgress()
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(progressTask!!)
    }

    override fun onResume(){
        super.onResume()
        updateMiniPlayerUI()
    }

    override fun onPlay(){
        runOnUiThread {
            binding.mainMiniplayerBtnPlayIv.visibility = View.GONE
            binding.mainMiniplayerBtnPauseIv.visibility = View.VISIBLE
            updateMiniPlayerUI()
        }
    }

    override fun onPausePlayback(){
        runOnUiThread {
            binding.mainMiniplayerBtnPlayIv.visibility = View.VISIBLE
            binding.mainMiniplayerBtnPauseIv.visibility = View.GONE
        }
    }

    override fun onPlaybackStateChanged(position: Int, duration: Int){
        runOnUiThread {
            binding.mainMiniplayerSeekbarSb.max = duration
            binding.mainMiniplayerSeekbarSb.progress = position
        }
    }

    // 미니 플레이어 업데이트 메서드
    fun updateMiniPlayerUI() {
        val song = SongManager.currentSong

        if(song != null){
            binding.mainMiniplayerTitleTv.text = song.title
            binding.mainMiniplayerSingerTv.text = song.singer
            binding.mainMiniplayerBtnPlayIv.visibility =
                if(SongManager.isPlaying) View.GONE else View.VISIBLE
            binding.mainMiniplayerBtnPauseIv.visibility =
                if(SongManager.isPlaying) View.VISIBLE else View.GONE
        } else{
            binding.mainMiniplayerTitleTv.text = "현재 재생 중인 곡이 없습니다."
            binding.mainMiniplayerSingerTv.text = ""
            binding.mainMiniplayerSeekbarSb.progress = 0
            binding.mainMiniplayerBtnPlayIv.visibility = View.VISIBLE
            binding.mainMiniplayerBtnPauseIv.visibility = View.GONE
        }
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

    override fun onDestroy() {
        super.onDestroy()
        SongManager.removeListener(this)
        progressTask?.let { handler.removeCallbacks(it) }
    }
}