package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2week.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment: Fragment() {

    private var _binding: FragmentLockerSavedsongBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        // 샘플 data
        val savedSongs = mutableListOf(
            AlbumData(R.drawable.img_album_exp, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "노래 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "노래 이름", "가수 이름"))

        val adapter = SavedSongAdapter(savedSongs) { clickedSong ->
            val bundle = Bundle().apply {
                putInt("albumImg", clickedSong.img)
                putString("albumTitle", clickedSong.title)
                putString("albumSinger", clickedSong.singer)
            }

            val albumFragment = AlbumFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        binding.savedSongListRv.apply{
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}