package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.databinding.ItemSavedSongBinding
import com.example.week2.R // R 클래스 사용을 위해 임시로 추가 (실제 프로젝트 구조에 따라 달라질 수 있음)

class SavedSongRVAdapter(private val songList: ArrayList<Song>) :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {


    interface MyItemClickListener {
        fun onSongClick(song: Song)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemSavedSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songList[position]
        holder.bind(song)


        holder.itemView.setOnClickListener {
            if (::mItemClickListener.isInitialized) {
                mItemClickListener.onSongClick(song)
            }
        }
    }

    override fun getItemCount(): Int = songList.size


    inner class ViewHolder(val binding: ItemSavedSongBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {

            binding.itemSavedAlbumCoverIv.setImageResource(R.drawable.img_album_exp)


            binding.itemSavedSongTitleTv.text = song.title
            binding.itemSavedSingerTv.text = song.singer


        }
    }
}
