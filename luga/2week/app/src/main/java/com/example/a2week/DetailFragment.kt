package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2week.databinding.FragmentDetailBinding

class DetailFragment: Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)


        // 앨범 이름과 작곡가 이름 출력
        arguments?.let{bundle ->
            val title = bundle.getString("albumTitle","앨범 제목")
            val singer = bundle.getString("albumSinger","가수 이름")

            // 출력 형식
            val displayText = "이 앨범의 이름은 $title 입니다.\n이 앨범의 작곡가는 $singer 입니다."

            binding.albumDetailTv.text = displayText
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}