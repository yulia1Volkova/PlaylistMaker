package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.PlayerState

interface PlayerRepository {
    fun createPlayer(url: String)
    fun play()
    fun pause()
    fun getState(): PlayerState
    fun getCurrentPosition(): String
    fun release()
    fun stop()


}