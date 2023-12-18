package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.model.TrackInfo
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis),
            artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = track.collectionName,
            releaseDate = track.releaseDate.substring(0, 4),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}