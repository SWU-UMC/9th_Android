package com.example.a2week

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.a2week.databinding.ItemTodaymusicBinding

class TodayMusicAdapter(
    private val albumList: List<AlbumData>,
    private val onClick: (AlbumData) -> Unit,
    private val onPlayClick: (Song) -> Unit // 앨범 좌측 하단의 플레이 버튼
) : RecyclerView.Adapter<TodayMusicAdapter.TodayMusicViewHolder>() {

    inner class TodayMusicViewHolder(val binding: ItemTodaymusicBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMusicViewHolder {
        val binding = ItemTodaymusicBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return TodayMusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayMusicViewHolder, position: Int) {
        val album = albumList[position]
        with(holder.binding){
            homeAlbumImgIv1.setImageResource(album.img)
            todayTitleTv1.text = album.title
            todaySingerTv1.text = album.singer

            root.setOnClickListener {
                onClick(album)
            }

            todayPlayBtnIv.setOnClickListener {
                val song = Song(title = album.title, singer = album.singer)
                onPlayClick(song)
            }

            // 아이템 폭 설정
            val displayMetrics = root.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth / 2.8).toInt()
            root.layoutParams.width = itemWidth
        }
    }

    override fun getItemCount(): Int = albumList.size
}