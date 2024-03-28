package com.practicum.playlistmaker.media.ui.models

import com.practicum.playlistmaker.search.domain.models.Track


sealed interface FavoriteState {

    object noFavorite : FavoriteState

    data class FavoriteContent(
        val trackInfo: List<Track>
    ) : FavoriteState
}