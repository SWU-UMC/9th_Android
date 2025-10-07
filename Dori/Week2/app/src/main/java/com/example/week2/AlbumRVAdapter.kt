package com.example.week2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.databinding.ItemAlbumBinding
import java.util.*

class AlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){


    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
        fun onPlayButtonClick(album: Album)
    }


    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        val album = albumList[position]
        holder.bind(album)


        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(album) }


        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            mItemClickListener.onPlayButtonClick(album)
        }
    }


    override fun getItemCount(): Int = albumList.size


    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemAblumTitleTv.text = album.title
            binding.itemAblumSingerTv.text = album.singer
            binding.itemAblumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}