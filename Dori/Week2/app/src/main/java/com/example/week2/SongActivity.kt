package com.example.week2

import android.content.Intent

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.week2.databinding.ActivitySongBinding


class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var albumTitle: String? = null
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    private var isPlaying: Boolean = false

    lateinit var timer: Timer
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initPlayList()
        initClickListener()
        initSong()

        val title = intent.getStringExtra("title")
        val singer = intent.getStringExtra("singer")


        binding.tvSongTitle.text = title
        binding.tvArtistName.text = singer

        albumTitle = title




    }

    private fun initClickListener(){
        binding.btnPlay.setOnClickListener {
            togglePlayPause()
        }



        binding.songDownIb.setOnClickListener {

            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null

            // 타이머 중지
            timer.interrupt()

            returnResultToMainActivity()
            finish()
        }

        binding.btnNext.setOnClickListener {
            moveSong(+1)
        }

        binding.btnPrevious.setOnClickListener {
            moveSong(-1)
        }
    }

    private fun togglePlayPause() {
        isPlaying = !isPlaying

        if (isPlaying) {

            binding.btnPlay.setImageResource(R.drawable.nugu_btn_pause_32)
            setPlayerStatus(true)
        } else {

            binding.btnPlay.setImageResource(R.drawable.btn_miniplayer_play)
            setPlayerStatus(false)
        }
    }


    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }


    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId",0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID",songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }


    private fun setPlayer(song: Song){

        binding.tvSongTitle.text = song.title
        binding.tvArtistName.text = song.singer
        binding.tvCurrentTime.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.tvTotalTime.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)


        binding.ivAlbumCover.setImageResource(song.coverImg!!)


        binding.seekBar.progress = (song.second * 100 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)


        if (song.isLike){

        } else{

        }

        setPlayerStatus(song.isPlaying)

    }


    private fun startTimer(){

        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){

                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {

                            binding.seekBar.progress = ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread {

                                binding.tvCurrentTime.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }

    private fun setPlayerStatus (isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying


        if(isPlaying){

            mediaPlayer?.start()
        } else {

            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }

    }



    private fun moveSong(direct: Int){
        if (nowPos + direct < 0){
            Toast.makeText(this,"first song", Toast.LENGTH_SHORT).show()
            return
        }

        if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }



    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
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


    override fun onPause() {
        super.onPause()

        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}