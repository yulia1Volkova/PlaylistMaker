package com.practicum.playlistmaker.db.domain.api

import com.practicum.playlistmaker.db.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    fun getFavoritesTracks(): Flow<List<Track>>
    suspend fun isFavorite(trackId: Int): Boolean
}