package com.practicum.playlistmaker

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    val trackClickListener: TrackClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = tracks[position]
        holder.bind(item)
        holder.setOnTrackListener(object :  onTrackClickListener{
            override fun action() {
                Log.d("TrackAdapter", "OnTrackClickListener $item ")
                trackClickListener.onTrackClick(item)

            }

        })

    }

    override fun getItemCount() = tracks.size

    interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

}

