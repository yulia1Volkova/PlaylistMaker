package com.practicum.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

class TrackViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.view_track, parentView, false)
)  {
    private val trackCover: ImageView = itemView.findViewById(R.id.trackCover)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: Track) {
        artistName.text = model.artistName
        trackName.text = model.trackName
        trackTime.text = model.trackTime
        //trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(4))
            .into(trackCover)
    }
    fun setOnTrackListener(listener : onTrackClickListener){
        itemView.setOnClickListener{
            listener.action()
        }
    }

}
interface onTrackClickListener {
    fun action()
}