package com.practicum.playlistmaker.util

import android.content.Context
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }
    fun providePlayerInteractor(): PlayerInteractorImpl {
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun getSearchRepository(context: Context): TracksSearchRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }
    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(context))
    }

    private fun provideHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context)
    }
    fun provideHistoryInteractor(context: Context): HistoryInteractorImpl {
        return HistoryInteractorImpl(provideHistoryRepository(context))
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator{
        return ExternalNavigatorImpl(context)
    }
    fun provideSharingInteractor(context: Context): SharingInteractorImpl{
        return SharingInteractorImpl(getExternalNavigator(context))
    }


}