package com.example.week2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week2.com.example.week2.AlbumVPAdapter
import com.example.week2.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private var albumTitle: String? = null
    private var artistName: String? = null
    private var receivedAlbum: Album? = null


    private var isLiked: Boolean = false

    private val information = arrayListOf("수록곡", "상세 정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 데이터 받기 (JSON 파싱)
        val albumJson = arguments?.getString("album")
        if (albumJson != null) {
            val type = object : TypeToken<Album>() {}.type
            receivedAlbum = Gson().fromJson(albumJson, type)

            albumTitle = receivedAlbum?.title ?: "제목 없음"
            artistName = receivedAlbum?.singer ?: "가수 정보 없음"

            // ✨ 전달받은 앨범 데이터에서 좋아요 상태(isLike) 가져오기
            isLiked = receivedAlbum?.isLike ?: false
        } else {
            albumTitle = arguments?.getString("title") ?: "제목 없음"
            artistName = arguments?.getString("singer") ?: "가수 정보 없음"
        }

        // 2. DB 연결 (SongDatabase 가져오기)
        val songDB = SongDatabase.getInstance(requireContext())!!

        // 3. UI 초기화 (DB도 같이 넘겨줌)
        initView(songDB)

        setupViewPager()
    }


    private fun initView(songDB: SongDatabase) {
        Log.d("AlbumData", "제목: $albumTitle, 가수: $artistName")

        binding.tvSongTitle.text = albumTitle
        binding.tvArtistName.text = artistName
        binding.ivAlbumCover.setImageResource(receivedAlbum?.coverImg ?: R.drawable.img_album_exp)


        setLikeIcon(isLiked)


        binding.albumBackIb.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        binding.ivLike.setOnClickListener {
            isLiked = !isLiked


            receivedAlbum?.let { album ->
                Thread {
                    songDB.albumDao().updateIsLikeById(isLiked, album.id)
                }.start()
            }

            // 3. 화면 아이콘 즉시 변경
            setLikeIcon(isLiked)
        }
    }


    private fun setLikeIcon(isLiked: Boolean) {
        if (isLiked) {
            binding.ivLike.setImageResource(R.drawable.ic_my_like_on) // 꽉 찬 하트
        } else {
            binding.ivLike.setImageResource(R.drawable.ic_my_like_off) // 빈 하트
        }
    }

    private fun setupViewPager() {
        val albumAdapter = AlbumVPAdapter(this)

        val detailAlbumTitle = albumTitle ?: "제목 없음"
        val composerName = artistName ?: "정보 없음"

        albumAdapter.addFragment(AlbumSongFragment.newInstance(receivedAlbum))
        albumAdapter.addFragment(DetailFragment.newInstance(detailAlbumTitle, composerName))
        albumAdapter.addFragment(VideoFragment())

        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumTabLayout, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}