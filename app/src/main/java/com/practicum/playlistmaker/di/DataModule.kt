package com.practicum.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.search.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.ITunesAPI
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HISTORY_PREFERENCES = "history_preferences"
const val THEME_APP_PREFERENCES = "Theme_app_preferences"

val dataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    factory<SharedPreferences>(named(HISTORY_PREFERENCES)) {
        androidContext()
            .getSharedPreferences(HISTORY_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory(named(THEME_APP_PREFERENCES)) {
        androidContext()
            .getSharedPreferences(THEME_APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { MediaPlayer() }

         single { PlayerState.DEFAULT }

}