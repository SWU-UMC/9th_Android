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

    private lateinit var db: AppDatabase
    private lateinit var songDao: SongDao

    private lateinit var songs: List<Song>
    private var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !splashFinished }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.postDelayed({splashFinished = true}, 2000)

        // db
        db = AppDBProvider.getInstance(this)
        songDao = db.songDao()

        // DB 비었을 때 더미 데이터
        if(songDao.getAllSongs().isEmpty()){
            songDao.insertSong(Song(title = "Lilac", singer = "IU", music = R.raw.music_lilac , albumIdx = 1))
            songDao.insertSong(Song(title = "Blueming", singer = "IU", music = R.raw.music_blueming, albumIdx = 2))
        }
        songs = songDao.getAllSongs()

        // songId 불러오기
        val sharedPreferences = getSharedPreferences("songPrefs", MODE_PRIVATE)
        val savedSongId = sharedPreferences.getInt("songId", -1)

        // id로 노래 불러오기
        val currentSong = if(savedSongId != -1) {songDao.getSongById(savedSongId)}
        else {songDao.getAllSongs().firstOrNull()}

        // SongManager 초기화
        currentSong?.let{
            saveSongId(it.id)
            SongManager.init(this, it.music, it)
        }
        SongManager.addListener(this)

        // SongActivity 실행
        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainMiniplayerBtnPlayIv.setOnClickListener { SongManager.play() }
        binding.mainMiniplayerBtnPauseIv.setOnClickListener { SongManager.pause() }
        binding.mainMiniplayerBtnPrevIv.setOnClickListener { movePrevSong() }
        binding.mainMiniplayerBtnNextIv.setOnClickListener { moveNextSong() }

        initBottomNavigation()
        startProgressUpdate()
        updateMiniPlayerUI()
    }

    private fun saveSongId(id: Int) {
        val sharedPreferences = getSharedPreferences("songPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putInt("songId", id).apply()
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

    private fun movePrevSong(){
        if(songs.isEmpty()) return

        nowPos = if(nowPos - 1 < 0) {songs.size - 1} else nowPos - 1
        val prevSong = songs[nowPos]
        SongManager.changeSong(this, prevSong)
        saveSongId(prevSong.id)
        updateMiniPlayerUI()
    }

    private fun moveNextSong() {
        if (songs.isEmpty()) return

        nowPos = (nowPos + 1) % songs.size
        val nextSong = songs[nowPos]
        SongManager.changeSong(this, nextSong)
        saveSongId(nextSong.id)
        updateMiniPlayerUI()
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
        progressTask?.let {
            handler.removeCallbacks(it)
            progressTask = null
        }
    }
}