package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlaybackState
import com.practicum.playlistmaker.player.ui.model.TrackInfo
import com.practicum.playlistmaker.search.ui.activity.SearchFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val playerTrack: TrackInfo,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    companion object {
        private const val DELAY = 300L
        private const val STARTTIMER = "00:00"
    }

    private val playingLiveData = MutableLiveData(PlaybackState(false, "00:00"))
    fun getplayingLiveData(): LiveData<PlaybackState> = playingLiveData

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
}