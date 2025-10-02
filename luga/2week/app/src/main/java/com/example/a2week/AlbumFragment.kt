package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2week.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {
    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private val information = arrayListOf("수록곡", "상세정보", "영상")

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

        // ViewPager2 연결
        val albumVPAdapter = AlbumVPAdapter(
            this,
            binding.albumMusicTitleTv.text.toString(),
            binding.albumSingerNameTv.text.toString())

        binding.albumContentVp.adapter = albumVPAdapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}