package com.practicum.playlistmaker.settings.domain.api

import android.content.Context
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}