package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.util.Creator


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val application: Application
) : ViewModel() {

    private val themeSwitcherLiveData =
        MutableLiveData(settingsInteractor.getThemeSettings().darkThemeEnabled)

    fun getThemeSwitcherLiveData(): LiveData<Boolean> = themeSwitcherLiveData

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as App

                SettingsViewModel(
                    Creator.provideSharingInteractor(application),
                    application.provideSettingsInteractor(application),
                    application
                )
            }
        }
    }


    fun shareApp() = sharingInteractor.shareApp()
    fun openSupport() = sharingInteractor.openSupport()
    fun openTerms() = sharingInteractor.openTerms()
    fun switchTheme(darkThemeEnabled: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(darkThemeEnabled))
        themeSwitcherLiveData.postValue(darkThemeEnabled)
        (application as App).switchTheme(darkThemeEnabled)
    }

}