package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryRepository {

        fun getTracks():Array<Track>
        fun addTrack(track: Track):List<Track>
        fun cleanHistory()

}