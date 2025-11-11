package com.example.a2week

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    private var bannerPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // ViewPager2 구현
        val bannerAdapter = BannerVPAdapter(this){ clickedAlbum ->
            openAlbumFragment(clickedAlbum)
        }

        val bannerList = listOf(
            HomePannelData(
                title = "오늘의 추천 노래",
                songs = listOf(
                    AlbumData(R.drawable.img_album_exp, "Dynamite", "BTS"),
                    AlbumData(R.drawable.img_album_exp, "Butter", "BTS")
                )
            ),
            HomePannelData(
                title = "달밤의 감성 산책",
                songs = listOf(
                    AlbumData(R.drawable.img_album_exp2, "Lilac", "IU"),
                    AlbumData(R.drawable.img_album_exp2, "Love Wins All", "IU")
                )
            )
        )

        bannerList.forEach { data ->
            bannerAdapter.addFragment(
                BannerFragment.newInstance(data)
            )
        }

        binding.homeTopBannerVp.apply{
            adapter = bannerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        // 인디케이터 연결
        binding.homeTopBannerVp.post{
            binding.homePanelIndicator.setViewPager(binding.homeTopBannerVp)
        }

        // 자동 슬라이드
        val handler = Handler(Looper.getMainLooper())
        val runnable = object: Runnable{
            override fun run(){
                _binding?.let{
                    bannerPosition = (bannerPosition + 1) % bannerAdapter.itemCount
                    it.homeTopBannerVp.setCurrentItem(bannerPosition, true)
                    handler.postDelayed(this, 3000)

                }
            }
        }
        handler.postDelayed(runnable, 3000)

        // 수동 스크롤 시 인디케이터 동기화
        binding.homeTopBannerVp.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        // RecycleView 구현 및 아이템 클릭 이벤트
        val todayAlbumList = listOf(
            AlbumData(R.drawable.img_album_exp2, "LILAC", "아이유(IU)"),
            AlbumData(R.drawable.img_album_exp2, "Love Wins All", "아이유(IU)"),
            AlbumData(R.drawable.img_album_exp, "Butter", "BTS"),
            AlbumData(R.drawable.img_album_exp, "Dynamite", "BTS")
        )

        val todayMusicAdapter = TodayMusicAdapter(
            todayAlbumList,
            onClick = { clickedAlbum -> openAlbumFragment(clickedAlbum) },
            onPlayClick = { clickedAlbum ->
                (activity as? MainActivity)?.updateMiniPlayerUI()
            })

        binding.homeTodayMusicRv.apply {
            adapter = todayMusicAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            if (itemDecorationCount == 0) {
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.right = 24
                    }
                })
            }
        }
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