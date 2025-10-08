package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.databinding.ItemSavedSongBinding

import java.util.ArrayList

class SavedSongRVAdapter(private val songList: ArrayList<Song>) :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {


    interface MyItemClickListener {
        fun onSongClick(song: Song)
        fun onMoreClick(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }


    fun removeItem(position: Int) {
        if (position in songList.indices) {
            songList.removeAt(position)
            notifyItemRemoved(position)
        }
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


        holder.binding.itemSavedMoreIv.setOnClickListener {
            if (::mItemClickListener.isInitialized) {
                mItemClickListener.onMoreClick(holder.bindingAdapterPosition)
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