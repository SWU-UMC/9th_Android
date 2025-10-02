package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.week2.databinding.FragmentAlbumSongBinding

class AlbumSongFragment : Fragment() {

    private var _binding: FragmentAlbumSongBinding? = null

    private val binding get() = _binding!!


    private var isMixOn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumSongBinding.inflate(inflater, container, false)


        binding.btnMixToggle.setOnClickListener {
            isMixOn = !isMixOn

            if (isMixOn) {

                binding.btnMixToggle.setImageResource(R.drawable.btn_toggle_on)
            } else {

                binding.btnMixToggle.setImageResource(R.drawable.btn_toggle_off)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}