package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackDto

class TrackResponse(val resultCount: Int,
                    val results: List<TrackDto>): Response() {
}