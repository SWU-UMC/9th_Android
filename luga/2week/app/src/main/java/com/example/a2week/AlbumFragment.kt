package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2week.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // HomeActivity에서 데이터 전달받기
        arguments?.let{bundle ->
            val title = bundle.getString("albumTitle")
            val singer = bundle.getString("albumSinger")

            binding.albumMusicTitleTv.text = title
            binding.albumSingerNameTv.text = singer
        }

        // 뒤로가기
        binding.albumBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}