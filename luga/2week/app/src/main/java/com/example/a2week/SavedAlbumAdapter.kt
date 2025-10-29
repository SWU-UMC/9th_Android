package com.example.a2week

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.a2week.databinding.ItemSavedalbumBinding

class SavedAlbumAdapter(
    private val savedAlbums: MutableList<SavedAlbumData>,
    private val onClick: (SavedAlbumData) -> Unit
) : RecyclerView.Adapter<SavedAlbumAdapter.SavedAlbumViewHolder>() {
    inner class SavedAlbumViewHolder(val binding: ItemSavedalbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: SavedAlbumData) {
            binding.savedAlbumIv.setImageResource(song.img)
            binding.savedAlbumTitleTv.text = song.title
            binding.savedAlbumSingerTv.text = song.singer
            binding.savedAlbumDetailTv.text = song.detail


            // 재생 버튼 상태 변경
            binding.songPlayIv.setImageResource(
                if(song.isPlaying) R.drawable.btn_miniplay_pause
                else R.drawable.btn_miniplayer_play
            )

            binding.songPlayIv.setOnClickListener {
                song.isPlaying = !song.isPlaying
                notifyItemChanged(bindingAdapterPosition)
            }

            binding.root.setOnClickListener {
                onClick(song)
            }

            // [...] 버튼 클릭 이벤트
            binding.songMoreIv.setOnClickListener{ view ->
                val popupMenu = PopupMenu(binding.root.context, binding.songMoreIv)
                popupMenu.menuInflater.inflate(R.menu.saved_song_item_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                    if(menuItem.itemId == R.id.action_delete){
                        val position = bindingAdapterPosition
                        if(position != RecyclerView.NO_POSITION){
                            savedAlbums.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        true
                    }else false
                }
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAlbumViewHolder {
        val binding = ItemSavedalbumBinding.inflate(
            LayoutInflater.from(parent.context),parent, false)
        return SavedAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedAlbumViewHolder, position: Int) {
        holder.bind(savedAlbums[position])
    }

    override fun getItemCount(): Int = savedAlbums.size
}