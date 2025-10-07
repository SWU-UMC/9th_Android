package com.example.week2

import android.os.Bundle
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
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("spring globe", "요네즈 켄시", R.drawable.img_album_globe))
            add(Album("dandelion", "우효 (Oohyo)", R.drawable.img_album_dendelion))
            add(Album("2am", "릴러말즈 (Leellamarz)", R.drawable.img_album_2am))
            add(Album("3am", "로제 (Rose)", R.drawable.img_album_3am))
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
                val firstSong = album.Songs?.firstOrNull() ?: Song(album.title ?: "제목", album.singer ?: "가수")


                (activity as MainActivity).updateMiniPlayer(
                    firstSong.title,
                    firstSong.singer
                )
            }
        })

        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)






        val topBannerAdapter = BannerVPAdapter(this@HomeFragment)
        topBannerAdapter.addFragment(MainBannerFragment(R.drawable.img_first_album_default))

        binding.homePannelBackgroundIv.adapter = topBannerAdapter
        binding.homePannelBackgroundIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val bottomBannerAdapter = BannerVPAdapter(this@HomeFragment)
        bottomBannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bottomBannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bottomBannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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