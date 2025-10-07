package com.example.a2week

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        // 상단 앨범 클릭 이벤트
        val topAlbum = AlbumData(
            img = R.drawable.img_album_exp,
            title = binding.homePanelAlbumTitleTv.text.toString(),
            singer = binding.homePanelAlbumSingerTv.text.toString()
        )

        binding.homePanelAlbumImgIv.setOnClickListener {
            openAlbumFragment(topAlbum)
        }

        // ViewPager2 구현
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_first_album_default))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_first_album_default))

        binding.homePanelBackgroundIv.adapter = bannerAdapter
        binding.homePanelBackgroundIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 인디케이터 연결
        TabLayoutMediator(binding.homePanelIndicator, binding.homePanelBackgroundIv) { _, _ -> }.attach()

        // RecycleView 구현 및 아이템 클릭 이벤트
        val todayAlbumList = listOf(
            AlbumData(R.drawable.img_album_exp2, "LILAC", "아이유(IU)"),
            AlbumData(R.drawable.img_album_exp2, "Love Wins All", "아이유(IU)"),
            AlbumData(R.drawable.img_album_exp, "Butter", "BTS"),
            AlbumData(R.drawable.img_album_exp, "Dynamite", "BTS"))

        val todayMusicAdapter = TodayMusicAdapter(
            todayAlbumList,
            onClick = { clickedAlbum -> openAlbumFragment(clickedAlbum)},
            onPlayClick = { clickedAlbum ->
                (activity as? MainActivity)?.updateMiniPlayer(clickedAlbum)
        })

        binding.homeTodayMusicRv.apply {
            adapter = todayMusicAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val itemSpacing = 24
        binding.homeTodayMusicRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = itemSpacing
            }
        })

        return binding.root
    }

    // 앨범 클릭 시 AlbumFragment 연결 메서드
    private fun openAlbumFragment(album: AlbumData) {
        // 클릭한 앨범 정보
        val bundle = Bundle().apply {
            putInt("albumImg", album.img)
            putString("albumTitle", album.title)
            putString("albumSinger", album.singer)
        }

        val albumFragment = AlbumFragment().apply{
            arguments = bundle
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, albumFragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}