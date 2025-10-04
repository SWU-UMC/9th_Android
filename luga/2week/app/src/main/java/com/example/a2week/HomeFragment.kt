package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.a2week.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homePanelAlbumImgIv.setOnClickListener {
            // 클릭한 앨범 정보
            val clickedAlbumTitle = binding.homePanelAlbumTitleTv.text.toString()
            val clickedAlbumSinger = binding.homePanelAlbumSingerTv.text.toString()

            // Bundle로 전달
            val bundle = Bundle().apply {
                putString("albumTitle", clickedAlbumTitle)
                putString("albumSinger", clickedAlbumSinger)
            }

            val albumFragment = AlbumFragment().apply{
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        // ViewPager2 구현
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_first_album_default))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_first_album_default))

        binding.homePanelBackgroundIv.adapter = bannerAdapter
        binding.homePanelBackgroundIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 인디케이터 연결
        TabLayoutMediator(binding.homePanelIndicator, binding.homePanelBackgroundIv) { _, _ -> }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}