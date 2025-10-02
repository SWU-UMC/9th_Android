package com.example.a2week

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// 상세정보 탭에서 앨범명과 가수 이름을 불러다 쓰기 위해 매개변수로 받아 bundle로 보냄
class AlbumVPAdapter(
    fragment: Fragment,
    private val albumTitle: String?,
    private val albumSinger: String?
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 // 수록곡, 상세정보, 영상

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment()
            1 -> DetailFragment().apply{
                arguments = Bundle().apply {
                    putString("albumTitle", albumTitle)
                    putString("albumSinger", albumSinger)
                }
            }
            else -> VideoFragment()
        }
    }

}