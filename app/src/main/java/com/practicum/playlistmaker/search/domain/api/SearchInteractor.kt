package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchInteractor {
    fun searchTracks(expression: String, consumer : TracksConsumer)

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}