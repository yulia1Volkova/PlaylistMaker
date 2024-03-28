package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

private const val APP_LINK = "https://practicum.yandex.ru/android-developer/"
private const val TERMS_LINK = "https://yandex.ru/legal/practicum_offer/"
private const val SUPPORT_EMAIL = "volkova.yulia88@yandex.ru"
private const val MAIL_SUBJECT = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
private const val MAIL_MESSAGE = "Спасибо разработчикам и разработчицам за крутое приложение!"

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

  private fun getShareAppLink(): String =APP_LINK

    private fun getSupportEmailData(): EmailData = EmailData(SUPPORT_EMAIL,MAIL_SUBJECT ,MAIL_MESSAGE)

    private fun getTermsLink(): String = TERMS_LINK


}