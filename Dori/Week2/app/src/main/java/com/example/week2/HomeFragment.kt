package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2

import com.google.gson.Gson
import com.example.week2.databinding.FragmentHomeBinding
import java.util.ArrayList



class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var albumDatas = ArrayList<Album>()
    private val handler = Handler(Looper.getMainLooper())
    private val SLIDE_DELAY: Long = 3000
    private val indicatorViews by lazy {
        listOf(binding.indicator0IvHome, binding.indicator1IvHome, binding.indicator2IvHome)
    }


    private val slideRunnable = object : Runnable {
        override fun run() {
            val adapter = binding.homePannelBackgroundIv.adapter
            if (adapter != null && adapter.itemCount > 0) {
                val current = binding.homePannelBackgroundIv.currentItem
                val next = (current + 1) % adapter.itemCount
                binding.homePannelBackgroundIv.setCurrentItem(next, true)
            }
            handler.postDelayed(this, SLIDE_DELAY)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        albumDatas.apply {
            add(Album(title = "Butter", singer = "방탄소년단 (BTS)", coverImg = R.drawable.img_album_exp, Songs = ArrayList()))
            add(Album(title = "Lilac", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2, Songs = ArrayList()))
            add(Album(title = "spring globe", singer = "요네즈 켄시", coverImg = R.drawable.img_album_globe, Songs = ArrayList()))
            add(Album(title = "dandelion", singer = "우효 (Oohyo)", coverImg = R.drawable.img_album_dendelion, Songs = ArrayList()))
            add(Album(title = "2am", singer = "릴러말즈 (Leellamarz)", coverImg = R.drawable.img_album_2am, Songs = ArrayList()))
            add(Album(title = "3am", singer = "로제 (Rose)", coverImg = R.drawable.img_album_3am, Songs = ArrayList()))
        }


        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter


        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

            override fun onPlayButtonClick(album: Album) {

                val firstSong = album.Songs.firstOrNull() ?:
                Song(
                    title = album.title ?: "제목",
                    singer = album.singer ?: "가수"

                )

                (activity as MainActivity).updateMiniPlayer(firstSong)

            }
        })

        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)



        val topBannerAdapter = BannerVPAdapter(this@HomeFragment)
        topBannerAdapter.addFragment(MainBannerFragment(R.drawable.img_first_album_default))
        topBannerAdapter.addFragment(MainBannerFragment(R.drawable.img_album_dendelion)) // 더미 데이터 2
        topBannerAdapter.addFragment(MainBannerFragment(R.drawable.img_album_2am)) // 더미 데이터 3

        binding.homePannelBackgroundIv.adapter = topBannerAdapter
        binding.homePannelBackgroundIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        updateIndicator(0)


        binding.homePannelBackgroundIv.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicator(position)
            }
        })



        val bottomBannerAdapter = BannerVPAdapter(this@HomeFragment)
        bottomBannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bottomBannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bottomBannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }


    override fun onResume() {
        super.onResume()

        handler.postDelayed(slideRunnable, SLIDE_DELAY)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(slideRunnable)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        handler.removeCallbacks(slideRunnable)
        _binding = null
    }



    private fun updateIndicator(position: Int) {

        indicatorViews.forEach { it.setImageResource(R.drawable.shape_circle_gray) }


        if (position >= 0 && position < indicatorViews.size) {
            indicatorViews[position].setImageResource(R.drawable.shape_circle_blue)
        }
    }



    private fun changeAlbumFragment(album: Album) {

        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}