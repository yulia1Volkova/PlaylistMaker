package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.models.PlayerState

interface PlayerInteractor {

    fun createPlayer(url: String)
    fun play()
    fun pause()
    fun getState(): PlayerState
    fun getCurrentPosition():String
    fun release()
    fun stop()
}


