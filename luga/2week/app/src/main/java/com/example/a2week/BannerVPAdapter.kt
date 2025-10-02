package com.example.a2week

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    // 리스트 내 Fragment 생성
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    // 리스트 내 Fragment 개수 반환
    override fun getItemCount(): Int = fragmentlist.size

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1)
    }
}