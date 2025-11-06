package com.example.a2week

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(
    fragment: Fragment,
    private val onAlbumClick: (AlbumData) -> Unit
) : FragmentStateAdapter(fragment) {
    private val fragments = mutableListOf<BannerFragment>()

    // 리스트 내 Fragment 개수 반환
    override fun getItemCount(): Int = fragments.size

    // 리스트 내 Fragment 생성
    override fun createFragment(position: Int): Fragment{
        return fragments[position].apply{
            albumClickListener = object: BannerFragment.OnAlbumClickListener{
                override fun onAlbumClicked(album: AlbumData) {
                    onAlbumClick(album)
                }
            }
        }
    }

    fun addFragment(fragment: BannerFragment) {
        fragments.add(fragment)
    }
}