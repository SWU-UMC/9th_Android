package com.example.a2week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a2week.databinding.ItemSonglistBinding

class SongListAdapter(
    private val songList: List<SongListData>,
    private val onClick: (SongListData) -> Unit
) : RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {

    inner class SongListViewHolder(val binding: ItemSonglistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(songList: SongListData) {
            binding.songListOrderTv.text = songList.order.toString()
            binding.songListTitleTv.text = songList.title
            binding.songListSingerTv.text = songList.singer

            binding.root.setOnClickListener {
                onClick(songList)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val binding = ItemSonglistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    override fun getItemCount(): Int = songList.size
}