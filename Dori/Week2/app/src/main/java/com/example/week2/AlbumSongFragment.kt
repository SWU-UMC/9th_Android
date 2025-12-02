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
    private val gson: Gson = Gson()
    private var isMixOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumSongBinding.inflate(inflater, container, false)


        val albumJson = arguments?.getString("album")
        if (albumJson != null) {
            val type = object : TypeToken<Album>() {}.type
            receivedAlbum = gson.fromJson(albumJson, type)
        }

        // 2. 리사이클러뷰 연결 (여기서 DB 조회할 것임)
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {

        var songList = arrayListOf<Song>()
        val songDB = SongDatabase.getInstance(requireContext())!!

        if (receivedAlbum != null) {
            // DB에서 이 앨범(albumIdx)에 속한 노래들만 가져오기
            val songsFromDB = songDB.songDao().getSongsInAlbum(receivedAlbum!!.id)
            songList.addAll(songsFromDB)
        }

        // 만약 DB에 노래가 없으면 더미 데이터라도 보여주기 (테스트용)
        if (songList.isEmpty()) {
            songList = createDummySongList()
        }

        val songRVAdapter = SongRVAdapter(songList)
        binding.fragmentAlbumSongRv.adapter = songRVAdapter
        binding.fragmentAlbumSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener {
            override fun onSongClick(song: Song) {
                // 여기서 노래 재생 등 처리
            }
        })
    }


    private fun createDummySongList(): ArrayList<Song> {
        return ArrayList<Song>().apply {
            add(Song(1, "가수 정보 없음", trackNumber = 1))
            add(Song(2, "더미 아티스트", trackNumber = 2))
            add(Song(3, "더미 아티스트", trackNumber = 3))
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