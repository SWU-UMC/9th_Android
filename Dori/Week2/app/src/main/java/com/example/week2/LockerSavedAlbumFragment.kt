package com.example.week2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentLockerSavedAlbumBinding
import java.util.ArrayList

class LockerSavedAlbumFragment : Fragment() {

    private var _binding: FragmentLockerSavedAlbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerSavedAlbumBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }


    private fun setupRecyclerView() {
        val savedAlbums = createDummySavedAlbumList()

        val savedAlbumRVAdapter = SavedAlbumRVAdapter(savedAlbums)


        binding.lockSavedAlbumRv.adapter = savedAlbumRVAdapter

        binding.lockSavedAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        savedAlbumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener {
            override fun onPlayClick(album: Album) {
                Log.d("LockerAlbumPlay", "Now playing: ${album.title}")
            }

            override fun onMoreClick(position: Int) {
                savedAlbumRVAdapter.removeItem(position)
                Log.d("LockerAlbumDelete", "Album at position $position deleted.")
            }
        })
    }


    private fun createDummySavedAlbumList(): ArrayList<Album> {
        return ArrayList<Album>().apply {

            add(
                Album(
                    title = "Butter",
                    singer = "방탄소년단 (BTS)",
                    coverImg = R.drawable.img_album_exp,
                    date = "2021.05.21",
                    type = "싱글 | KPOP",
                    Songs = ArrayList()
                )
            )
            add(
                Album(
                    title = "Lilac",
                    singer = "아이유 (IU)",
                    coverImg = R.drawable.img_album_exp2,
                    date = "2021.03.25",
                    type = "정규 | KPOP",
                    Songs = ArrayList()
                )
            )
            add(
                Album(
                    title = "spring globe",
                    singer = "요네즈 켄시",
                    coverImg = R.drawable.img_album_globe,
                    date = "2024.04.10",
                    type = "싱글 | JPOP",
                    Songs = ArrayList()
                )
            )
            add(
                Album(
                    title = "dandelion",
                    singer = "우효 (Oohyo)",
                    coverImg = R.drawable.img_album_dendelion,
                    date = "2015.05.07",
                    type = "정규 | Indie",
                    Songs = ArrayList()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}