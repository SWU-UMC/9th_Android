package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.databinding.ItemLockerAlbumBinding
import java.util.ArrayList

class SavedAlbumRVAdapter(private val albumList: ArrayList<Album>) :
    RecyclerView.Adapter<SavedAlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onPlayClick(album: Album)
        fun onMoreClick(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLockerAlbumBinding = ItemLockerAlbumBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]

        holder.bind(album)

        holder.itemView.setOnClickListener {

        }

        holder.binding.itemSavedPlayIv.setOnClickListener {
            mItemClickListener.onPlayClick(album)
        }

        holder.binding.itemSavedMoreIv.setOnClickListener {
            mItemClickListener.onMoreClick(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemLockerAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.itemSavedAlbumTitleTv.text = album.title
            binding.itemSavedSingerTv.text = album.singer

            if (album.coverImg != null) {
                binding.itemSavedAlbumCoverIv.setImageResource(album.coverImg!!)
            }


            binding.itemSavedDateTv.text = album.date
            binding.itemSavedTypeTv.text = album.type
        }
    }
}