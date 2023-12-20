package com.practicum.playlistmaker.presentation.model

data class TrackInfo(    val trackId: Int,
                         val trackName: String,
                         val artistName: String,
                         val trackTime: String,
                         val artworkUrl512: String,
                         val collectionName: String,
                         val releaseDate: String,
                         val primaryGenreName: String,
                         val country: String,
                         val previewUrl: String)
