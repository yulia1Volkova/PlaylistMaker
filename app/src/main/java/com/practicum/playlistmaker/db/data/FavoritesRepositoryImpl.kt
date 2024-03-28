package com.practicum.playlistmaker.db.data

import com.practicum.playlistmaker.db.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.data.db.AppDatabase
import com.practicum.playlistmaker.db.data.db.entity.TrackEntity
import com.practicum.playlistmaker.db.domain.api.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoritesRepository {
    override suspend fun addToFavorites(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteFromFavorites(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun isFavorite(trackId: Int): Boolean = appDatabase.trackDao().isFavorite(trackId)

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}

