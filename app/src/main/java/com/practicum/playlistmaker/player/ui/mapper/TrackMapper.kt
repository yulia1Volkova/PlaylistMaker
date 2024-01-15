package com.practicum.playlistmaker.player.ui.mapper

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.model.TrackInfo
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = track.collectionName,
            releaseDate = ((track.releaseDate)?:"").substring(0, 4),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl?:""
        )
    }
}