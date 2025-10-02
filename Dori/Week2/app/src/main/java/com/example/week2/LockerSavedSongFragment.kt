package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week2.databinding.FragmentLockerSavedSongBinding

class LockerSavedSongFragment : Fragment() {


    private var _binding: FragmentLockerSavedSongBinding? = null
    private val binding get() = _binding!!


    private val songData = arrayListOf(
        Song("Butter", "BTS"),
        Song("Lilac", "IU"),
        Song("Next Level", "aespa"),
        Song("Dun Dun Dance", "Oh My Girl"),
        Song("Permission to Dance", "BTS"),
        Song("Weekend", "Taeyeon"),
        Song("Celebrity", "IU"),
        Song("Hot Sauce", "NCT Dream")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLockerSavedSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}