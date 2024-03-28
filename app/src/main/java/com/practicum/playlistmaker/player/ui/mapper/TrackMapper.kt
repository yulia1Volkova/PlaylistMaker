package com.practicum.playlistmaker.player.ui.mapper

import com.practicum.playlistmaker.player.ui.model.TrackInfo
import com.practicum.playlistmaker.search.domain.models.Track

class TrackMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName?:"",
            artistName = track.artistName?:"",
            trackTime = track.trackTime?:"",
            artworkUrl512 = if (track.artworkUrl100!=null) track.artworkUrl100!!.replaceAfterLast('/', "512x512bb.jpg") else "",
            collectionName = track.collectionName?:"",
            releaseDate = ((track.releaseDate)?:"").substring(0, 4),
            primaryGenreName = track.primaryGenreName?:"",
            country = track.country?:"",
            previewUrl = track.previewUrl?:""
        )
    }

    fun map(track: TrackInfo): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName?:"",
            artistName = track.artistName?:"",
            trackTime = track.trackTime?:"",
            artworkUrl100 = track.artworkUrl512.replaceAfterLast('/', "100x100bb.jpg"),
            collectionName = track.collectionName?:"",
            releaseDate = ((track.releaseDate)?:"").substring(0, 4),
            primaryGenreName = track.primaryGenreName?:"",
            country = track.country?:"",
            previewUrl = track.previewUrl?:""
        )
    }


}