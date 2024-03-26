package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.player.ui.mapper.TrackMapper
import com.practicum.playlistmaker.player.ui.model.PlaybackState
import com.practicum.playlistmaker.player.ui.model.TrackInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class AudioPlayerViewModel(
    private val playerTrack: TrackInfo,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
) : ViewModel() {

    companion object {
        private const val DELAY = 300L
        private const val STARTTIMER = "00:00"
    }

    private val playingLiveData = MutableLiveData(PlaybackState(false, "00:00"))
    private val favoritesLiveData = MutableLiveData(isFavorite(playerTrack.trackId))
    fun getplayingLiveData(): LiveData<PlaybackState> = playingLiveData
    fun getFavoritesLiveData(): LiveData<Boolean> = favoritesLiveData

    private var timerJob: Job? = null

    fun create() {
        playerInteractor.createPlayer(playerTrack.previewUrl)
    }

    fun play() {
        when (playerInteractor.getState()) {
            PlayerState.PLAYING -> {
                playerInteractor.pause()
                playingLiveData.postValue(
                    PlaybackState(
                        false,
                        playerInteractor.getCurrentPosition()
                    )
                )
            }

            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.DEFAULT, PlayerState.END -> {
                statePlaying()
            }
        }
    }

    fun likeClick() {
        if (isFavorite(playerTrack.trackId)) {
            viewModelScope.launch {
                val track = TrackMapper().map(playerTrack)
                favoritesInteractor.deleteFromFavorites(track)
            }
            favoritesLiveData.postValue(false)
        } else {
            viewModelScope.launch {
                val track = TrackMapper().map(playerTrack)
                favoritesInteractor.addToFavorites(track)
                favoritesLiveData.postValue(true)
            }
        }
    }


    private fun statePlaying() {
        playerInteractor.play()
        playingLiveData.postValue(PlaybackState(true, playerInteractor.getCurrentPosition()))
        timerJob = viewModelScope.launch {
            while (playerInteractor.getState() == PlayerState.PLAYING) {
                playingLiveData.postValue(
                    PlaybackState(
                        true,
                        playerInteractor.getCurrentPosition()
                    )
                )
                delay(DELAY)
            }
            if (playerInteractor.getState() == PlayerState.END) {
                playingLiveData.postValue(PlaybackState(false, STARTTIMER))
            }
        }
    }

    fun onPause() {
        playerInteractor.pause()
        timerJob?.cancel()
        playingLiveData.postValue(
            PlaybackState(
                false,
                playerInteractor.getCurrentPosition()
            )
        )
    }

    fun onDestroy() {
        playerInteractor.release()
    }

    private fun isFavorite(trackId: Int): Boolean {
        var count=0
        viewModelScope.launch {
            count = favoritesInteractor.isFavorite(trackId)

        }
        return count != 0
    }
}