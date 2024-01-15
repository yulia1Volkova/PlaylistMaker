package com.practicum.playlistmaker.sharing.domain.api

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(string: String)
    fun openEmail(emailData: EmailData)
    fun openLink(string: String)

}