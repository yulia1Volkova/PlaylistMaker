package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(),get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(named(THEME_APP_PREFERENCES)))
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(named(HISTORY_PREFERENCES)),get(),)
    }

    single<TracksSearchRepository> {
        TracksRepositoryImpl(get())
    }

}