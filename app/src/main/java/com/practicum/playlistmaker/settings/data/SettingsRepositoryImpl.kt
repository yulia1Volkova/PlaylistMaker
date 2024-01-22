package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings

const val THEME_APP_KEY = "key_for_theme"

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences ): SettingsRepository {

    var darkTheme:Boolean = false

    override fun getThemeSettings(): ThemeSettings {

        darkTheme = sharedPrefs.getBoolean(THEME_APP_KEY, darkTheme)
        return ThemeSettings(darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        darkTheme = settings.darkThemeEnabled
        sharedPrefs.edit()
            .putBoolean(THEME_APP_KEY, darkTheme)
            .apply()
    }
}