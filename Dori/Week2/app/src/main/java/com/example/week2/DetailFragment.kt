package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week2.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!


    private val ARG_ALBUM_NAME = "album_name"
    private val ARG_COMPOSER = "composer_name"

    companion object {
        fun newInstance(albumName: String, composer: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ALBUM_NAME, albumName)
                    putString(ARG_COMPOSER, composer)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val albumName = arguments?.getString(ARG_ALBUM_NAME) ?: "정보 없음"
        val composer = arguments?.getString(ARG_COMPOSER) ?: "정보 없음"


        val infoText = "이 앨범의 이름은 ${albumName} 입니다.\n이 앨범의 작곡가는 ${composer} 입니다."

        binding.tvDetailInfo.text = infoText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}