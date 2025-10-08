package com.example.week2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentAlbumSongBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class AlbumSongFragment : Fragment() {

    private var _binding: FragmentAlbumSongBinding? = null
    private val binding get() = _binding!!
    private var receivedAlbum: Album? = null

    private var isMixOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumSongBinding.inflate(inflater, container, false)


        val albumJson = arguments?.getString("album")
        if (albumJson != null) {
            val type = object : TypeToken<Album>() {}.type

            receivedAlbum = Gson().fromJson(albumJson, type)


            Log.d("AlbumSong", "Received Album Title: ${receivedAlbum?.title}")
        }


        setupRecyclerView()
        setupMixToggle()

        return binding.root
    }

    private fun setupRecyclerView() {

        val songList = receivedAlbum?.Songs ?: createDummySongList()



        val songRVAdapter = SongRVAdapter(songList)
        binding.fragmentAlbumSongRv.adapter = songRVAdapter



        binding.fragmentAlbumSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener {
            override fun onSongClick(song: Song) {

                Log.d("SongClick", "Now playing: ${song.title}")
            }
        })
    }


    private fun createDummySongList(): ArrayList<Song> {
        return ArrayList<Song>().apply {
            add(Song("제목 없음 (더미)", "가수 정보 없음", trackNumber = 1))
            add(Song("노래 2", "더미 아티스트", trackNumber = 2))
            add(Song("노래 3", "더미 아티스트", trackNumber = 3))
        }
    }

    private fun setupMixToggle() {
        binding.btnMixToggle.setOnClickListener {
            isMixOn = !isMixOn



            if (isMixOn) {
                binding.btnMixToggle.setImageResource(R.drawable.btn_toggle_on)
            } else {
                binding.btnMixToggle.setImageResource(R.drawable.btn_toggle_off)
            }


            // TODO: 믹스 모드에 따른 RecyclerView 데이터 및 UI 변경 로직은 여기에 추가합니다.
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    companion object {
        private const val ALBUM_JSON = "album"

        fun newInstance(album: Album?): AlbumSongFragment {
            val fragment = AlbumSongFragment()
            if (album != null) {
                val bundle = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString(ALBUM_JSON, albumJson)
                }
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}