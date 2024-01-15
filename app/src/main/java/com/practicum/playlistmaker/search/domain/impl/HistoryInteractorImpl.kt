package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(
    private val historyRepository: HistoryRepository
):HistoryInteractor {
    override fun getTracks(): Array<Track> {
       return historyRepository.getTracks()
    }

    override fun addTrack(track: Track): List<Track> {
        return historyRepository.addTrack(track)
    }

    override fun cleanHistory() {
        historyRepository.cleanHistory()
    }
}


