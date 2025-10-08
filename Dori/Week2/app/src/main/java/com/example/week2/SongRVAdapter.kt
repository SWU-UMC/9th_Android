package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.databinding.ItemSongBinding

class SongRVAdapter(private val songList: ArrayList<Song>) :
    RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {


    interface MyItemClickListener {
        fun onSongClick(song: Song)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.itemSongTrackNumberTv.text = String.format("%02d", song.trackNumber)
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer


        }
    }
}