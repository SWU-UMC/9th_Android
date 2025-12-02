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
    lateinit var albumDB: SongDatabase // DB 연결 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerSavedAlbumBinding.inflate(inflater, container, false)

        // DB 초기화
        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.lockSavedAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        Thread {
            val likedAlbums = albumDB.albumDao().getLikedAlbums()

            activity?.runOnUiThread {
                val savedAlbumRVAdapter = SavedAlbumRVAdapter(likedAlbums as ArrayList<Album>)
                binding.lockSavedAlbumRv.adapter = savedAlbumRVAdapter
                savedAlbumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener {
                    override fun onPlayClick(album: Album) {
                        Log.d("LockerAlbumPlay", "Now playing: ${album.title}")
                    }


                    override fun onMoreClick(position: Int) {
                        val album = likedAlbums[position]

                        Thread {
                            albumDB.albumDao().updateIsLikeById(false, album.id)
                        }.start()

                        savedAlbumRVAdapter.removeItem(position)
                        Log.d("LockerAlbumDelete", "Album deleted: ${album.title}")
                    }
                })
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}