package com.example.week2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.week2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    // ① View Binding 객체를 선언 (private var로 선언)
    private var _binding: FragmentHomeBinding? = null

    // ② Null-Safe한 getter 프로퍼티 생성 (코드를 간결하게 사용하기 위함)
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ③ 바인딩 객체 생성 및 초기화
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root // ④ 프래그먼트의 View로 binding.root를 반환
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ⑤ 뷰가 생성된 후 클릭 리스너 연결 함수 호출
        setupClickListener()
    }

    // ⑥ 이미지뷰 클릭 리스너 설정 함수
    private fun setupClickListener() {
        // ID: bottom_pannel_album_img_iv -> Binding 프로퍼티: bottomPannelAlbumImgIv
        binding.bottomPannelAlbumImgIv.setOnClickListener {
            // Intent를 사용해 AlbumActivity로 이동
            val intent = Intent(context, AlbumActivity::class.java)
            startActivity(intent)
        }
    }

    // ⑦ 메모리 누수 방지: 뷰가 파괴될 때 binding 객체를 해제
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}