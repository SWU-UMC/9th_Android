package com.example.week2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.week2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.bottomPannelAlbumImgIv.setOnClickListener {


            val albumTitle = binding.bottemPannelAlbumTv.text.toString() // "Buttor"
            val albumSinger = binding.bottemPannelAlbumSuvTv.text.toString() // "BTS"

            val fragment = AlbumFragment().apply {
                arguments = Bundle().apply {

                    putString("title", albumTitle)
                    putString("singer", albumSinger)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }


        val topBannerAdapter = BannerVPAdapter(this@HomeFragment)

        topBannerAdapter.addFragment(MainBannerFragment(R.drawable.img_first_album_default))


        binding.homePannelBackgroundIv.adapter = topBannerAdapter
        binding.homePannelBackgroundIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val bottomBannerAdapter = BannerVPAdapter(this@HomeFragment)

        bottomBannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bottomBannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bottomBannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}