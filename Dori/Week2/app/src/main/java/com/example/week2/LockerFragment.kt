package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.week2.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!


    private val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val lockerAdapter = LockerVPAdapter(this)


        lockerAdapter.addFragment(LockerSavedSongFragment())
        lockerAdapter.addFragment(LockerMusicFileFragment())
        lockerAdapter.addFragment(LockerSavedAlbumFragment())

        binding.lockerContentVp.adapter = lockerAdapter


        TabLayoutMediator(binding.lockerTabLayout, binding.lockerContentVp) {
                tab, position ->
            tab.text = information[position]
        }.attach()

        binding.btnLogin.setOnClickListener {
            // 1. 토스트 메시지 띄우기 (클릭 확인용)
            Toast.makeText(requireContext(), "로그인 버튼 클릭됨! 👆", Toast.LENGTH_SHORT).show()

            // 2. 화면 이동 시도
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}