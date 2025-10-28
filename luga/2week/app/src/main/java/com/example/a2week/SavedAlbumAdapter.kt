package com.example.a2week

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.a2week.databinding.ItemSavedsongBinding

class SavedAlbumAdapter(
    private val songList: MutableList<AlbumData>,
    private val onClick: (AlbumData) -> Unit
) : RecyclerView.Adapter<SavedAlbumAdapter.SavedSongViewHolder>() {
    inner class SavedSongViewHolder(val binding: ItemSavedsongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: AlbumData) {
            binding.savedSongIv.setImageResource(song.img)
            binding.savedSongTitleTv.text = song.title
            binding.savedSongSingerTv.text = song.singer

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
                            songList.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        true
                    }else false
                }
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSongViewHolder {
        val binding = ItemSavedsongBinding.inflate(
            LayoutInflater.from(parent.context),parent, false)
        return SavedSongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    override fun getItemCount(): Int = songList.size
}