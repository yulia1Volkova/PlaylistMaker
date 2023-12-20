package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.api.PlayerRepository

object Creator {
    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }
    fun providePlayerInteractor(): PlayerInteractorImpl {
        return PlayerInteractorImpl(providePlayerRepository())
    }
}