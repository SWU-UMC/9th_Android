package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2week.databinding.FragmentBannerBinding

class BannerFragment: Fragment() {
    lateinit var binding: FragmentBannerBinding
    lateinit var bannerData: HomePannelData

    interface OnAlbumClickListener{
        fun onAlbumClicked(album: AlbumData)
    }

    var albumClickListener: OnAlbumClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        arguments?.let {
            bannerData = it.getParcelable("bannerData")!!
        }
    }

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)

        // 첫 번째 곡
        binding.homePanelAlbumImgIv.setImageResource(bannerData.songs[0].img)
        binding.homePanelAlbumTitleTv.text = bannerData.songs[0].title
        binding.homePanelAlbumSingerTv.text = bannerData.songs[0].singer

        // 두 번째 곡
        binding.homePanelAlbumImg2Iv.setImageResource(bannerData.songs[1].img)
        binding.homePanelAlbumTitle2Tv.text = bannerData.songs[1].title
        binding.homePanelAlbumSinger2Tv.text = bannerData.songs[1].singer

        // 클릭 리스너
        binding.homePanelAlbumImgIv.setOnClickListener {
            albumClickListener?.onAlbumClicked(bannerData.songs[0])
        }
        binding.homePanelAlbumImg2Iv.setOnClickListener {
            albumClickListener?.onAlbumClicked(bannerData.songs[1])
        }

        return binding.root
    }

    companion object {
        fun newInstance(bannerData: HomePannelData) =
            BannerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("bannerData", bannerData)
                }
            }
    }
}