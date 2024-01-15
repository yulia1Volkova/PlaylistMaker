package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.models.PlayerState

interface PlayerRepository {
    fun createPlayer(url: String)
    fun play()
    fun pause()
    fun getState(): PlayerState
    fun getCurrentPosition(): String
    fun release()
    fun stop()


}