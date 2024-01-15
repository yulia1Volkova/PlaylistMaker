package com.practicum.playlistmaker.search.ui.models

import com.practicum.playlistmaker.player.ui.model.TrackInfo
import com.practicum.playlistmaker.search.domain.models.Track

sealed interface TrackSearchState {

    object Loading : TrackSearchState

    data class SearchContent(
        val trackInfo: MutableList<Track>
    ) : TrackSearchState

    data class HistoryContent(
        val trackInfo: MutableList<Track>,
        val isEmpty: Boolean
    ) : TrackSearchState

    data class Error(
        val errorMessage: String
    ) : TrackSearchState

    data class Empty(
        val message: String
    ) : TrackSearchState

}