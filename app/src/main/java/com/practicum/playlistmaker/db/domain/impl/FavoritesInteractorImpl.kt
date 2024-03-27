package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.db.data.db.entity.TrackEntity
import com.practicum.playlistmaker.db.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.db.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {

    override suspend fun addToFavorites(track: Track){
        favoritesRepository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoritesRepository.deleteFromFavorites(track)
    }

    override fun getFavoritesTracks(): Flow<List<Track>> =
        favoritesRepository.getFavoritesTracks()

    override suspend fun isFavorite(trackId:Int):Boolean =
        favoritesRepository.isFavorite(trackId)



}

