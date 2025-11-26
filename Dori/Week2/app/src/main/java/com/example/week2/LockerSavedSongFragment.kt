package com.example.week2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentLockerSavedSongBinding
import java.util.ArrayList

class LockerSavedSongFragment : Fragment() {

    private var _binding: FragmentLockerSavedSongBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerSavedSongBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val savedSongs = createDummySavedSongList()


        val savedSongRVAdapter = SavedSongRVAdapter(savedSongs)
        binding.lockerSavedSongRv.adapter = savedSongRVAdapter

        binding.lockerSavedSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        savedSongRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onSongClick(song: Song) {
                Log.d("LockerSongClick", "Playing: ${song.title}")

            }

            override fun onMoreClick(position: Int) {

                savedSongRVAdapter.removeItem(position)
                Log.d("LockerDelete", "Item at position $position deleted.")
            }
        })
    }




    private fun createDummySavedSongList(): ArrayList<Song> {
        return ArrayList<Song>().apply {
            add(
                Song(
                    title = "제목 없음 (보관함)",
                    singer = "저장된 아티스트 1",
                    coverImg = R.drawable.img_album_exp,
                    isLike = true,
                    albumIdx = 0,
                    trackNumber = 1
                )
            )
            add(
                Song(
                    title = "Saved Song 2",
                    singer = "저장된 아티스트 2",
                    coverImg = R.drawable.img_album_lilac,
                    isLike = true,
                    albumIdx = 0,
                    trackNumber = 2
                )
            )
            add(
                Song(
                    title = "Saved Song 3",
                    singer = "저장된 아티스트 3",
                    coverImg = R.drawable.img_album_globe,
                    isPlaying = true,
                    isLike = true,
                    albumIdx = 0,
                    trackNumber = 3
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}