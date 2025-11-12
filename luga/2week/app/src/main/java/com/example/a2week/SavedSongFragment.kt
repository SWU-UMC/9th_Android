package com.example.a2week

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2week.databinding.FragmentLockerSavedsongBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SavedSongFragment: Fragment(), EditModeHandler {

    private var _binding: FragmentLockerSavedsongBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomNav: BottomNavigationView
    private var isEditMode = false
    private lateinit var adapter: SavedSongAdapter
    private lateinit var selecAllText: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        bottomNav = requireActivity().findViewById(R.id.main_bnv)
        selecAllText = requireActivity().findViewById(R.id.locker_select_all_tv)

        // 샘플 data
        val savedSongs = mutableListOf(
            AlbumData(img = R.drawable.img_album_exp, title = "노래 이름", singer = "가수 이름"),
            AlbumData(img = R.drawable.img_album_exp, title = "노래 이름", singer = "가수 이름"),
            AlbumData(img = R.drawable.img_album_exp2, title = "노래 이름", singer = "가수 이름"),
            AlbumData(img = R.drawable.img_album_exp2, title = "노래 이름", singer = "가수 이름"),
            AlbumData(img = R.drawable.img_album_exp, title = "노래 이름", singer = "가수 이름"),
            AlbumData(img = R.drawable.img_album_exp, title = "노래 이름", singer = "가수 이름"),
            AlbumData(img = R.drawable.img_album_exp2, title = "노래 이름", singer = "가수 이름"))

        adapter = SavedSongAdapter(savedSongs) { clickedSong ->
            if(!isEditMode){
                val bundle = Bundle().apply {
                    putInt("albumImg", clickedSong.img)
                    putString("albumTitle", clickedSong.title)
                    putString("albumSinger", clickedSong.singer)
                }

                val albumFragment = AlbumFragment().apply {
                    arguments = bundle
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, albumFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
            else{
                adapter.toggleSelection(clickedSong)
            }
        }

        binding.savedSongListRv.apply{
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        bottomNav.setOnItemSelectedListener { item ->
            if(!isEditMode) return@setOnItemSelectedListener false
            when(item.itemId){
                R.id.deleteFragment -> {
                    adapter.deleteSelected()

                    toggleEditMode(false)
                    updateSelectAllText()
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.savedSongListRv.layoutManager = LinearLayoutManager(requireContext())
        binding.savedSongListRv.adapter = adapter
    }

    @SuppressLint("ResourceType")
    override fun toggleEditMode(isEditMode: Boolean){
        this.isEditMode = isEditMode
        val ctx = requireContext()

        if(isEditMode){
            bottomNav.menu.clear()
            bottomNav.inflateMenu(R.menu.bottom_sheet_dialog)
            adapter.isSelectionMode = true

            bottomNav.setBackgroundColor(ContextCompat.getColor(ctx, R.color.select_color))
            bottomNav.itemIconTintList = ContextCompat.getColorStateList(ctx, R.color.white)
            bottomNav.itemTextColor = ContextCompat.getColorStateList(ctx, R.color.white)
        }
        else{
            bottomNav.menu.clear()
            bottomNav.inflateMenu(R.menu.bottom_nav_menu)
            adapter.isSelectionMode = false
            adapter.clearSelection()

            bottomNav.setBackgroundColor(ContextCompat.getColor(ctx, R.color.white))
            bottomNav.itemIconTintList = resources.getColorStateList(R.drawable.btm_color_selector, ctx.theme)
            bottomNav.itemTextColor = resources.getColorStateList(R.drawable.btm_color_selector, ctx.theme)

            restoreDefaultBottomNavListener()
        }
    }

    fun selectAllItems(select: Boolean){
        if(select && !isEditMode) toggleEditMode(true)
        else{
            adapter.clearSelection()
            toggleEditMode(false)
        }
        adapter.selectAll(select)
    }

    private fun updateSelectAllText() {
        val isAllSelected = adapter.selectedCount() == adapter.itemCount && adapter.itemCount > 0
        selecAllText.text = if(isAllSelected) "선택 해제" else "전체 선택"
    }

    private fun restoreDefaultBottomNavListener() {
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> { /* 홈 이동 */ true }
                R.id.searchFragment -> { /* 검색 이동 */ true }
                R.id.lockerFragment -> { /* 로커 이동 */ true }
                else -> false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}