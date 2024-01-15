package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackRequest
import com.practicum.playlistmaker.search.data.dto.TrackResponse
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksSearchRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Internet")
            }

            200 -> {
                Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        (SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis)),
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}