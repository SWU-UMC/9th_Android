package com.example.a2week

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a2week.databinding.ItemSavedsongBinding

class SavedSongAdapter(
    private val songList: MutableList<AlbumData>,
    private val onClick: (AlbumData) -> Unit
) : RecyclerView.Adapter<SavedSongAdapter.SavedSongViewHolder>() {

    private val selectedSongs = mutableSetOf<AlbumData>()
    var isSelectionMode = false

    inner class SavedSongViewHolder(val binding: ItemSavedsongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: AlbumData) {
            binding.savedSongIv.setImageResource(song.img)
            binding.savedSongTitleTv.text = song.title
            binding.savedSongSingerTv.text = song.singer

            val context = binding.root.context
            if(selectedSongs.contains(song)){
                binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_song))
            }else{
                binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

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
                if(isSelectionMode) toggleSelection(song)
                else onClick(song)
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

    fun toggleSelection(song: AlbumData) {
        if(selectedSongs.contains(song)) selectedSongs.remove(song)
        else selectedSongs.add(song)
        notifyDataSetChanged()
    }

    fun selectAll(select: Boolean){
        selectedSongs.clear()
        if(select) selectedSongs.addAll(songList)
        notifyDataSetChanged()
    }

    fun deleteSelected(){
        songList.removeAll(selectedSongs)
        selectedSongs.clear()
        notifyDataSetChanged()
    }

    fun clearSelection(){
        selectedSongs.clear()
        notifyDataSetChanged()
    }

    fun selectedCount(): Int = selectedSongs.size
}