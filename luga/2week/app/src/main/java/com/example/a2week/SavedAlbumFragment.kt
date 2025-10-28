package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2week.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment: Fragment() {

    private var _binding: FragmentLockerSavedalbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)

        // 샘플 data
        val savedSongs = mutableListOf(
            AlbumData(R.drawable.img_album_exp, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "앨범 이름", "가수 이름"),
            AlbumData(R.drawable.img_album_exp2, "앨범 이름", "가수 이름"))

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

        binding.savedAlbumListRv.apply{
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