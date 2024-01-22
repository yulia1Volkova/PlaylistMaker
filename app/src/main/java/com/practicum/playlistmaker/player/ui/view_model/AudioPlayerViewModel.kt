package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.player.ui.model.PlaybackState
import com.practicum.playlistmaker.player.ui.model.TrackInfo

class AudioPlayerViewModel(
    private val playerTrack: TrackInfo,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    companion object {
        private const val DELAY = 500L
        private const val STARTTIMER = "00:00"
    }

    private val playingLiveData = MutableLiveData(PlaybackState(false, "00:00"))
    fun getplayingLiveData(): LiveData<PlaybackState> = playingLiveData

    private val handler = Handler(Looper.getMainLooper())

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
        handler.post(
            createUpdateTimerTask()
        )
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val state: PlayerState = playerInteractor.getState()
                if (state == PlayerState.PLAYING) {
                    playingLiveData.postValue(
                        PlaybackState(
                            true,
                            playerInteractor.getCurrentPosition()
                        )
                    )
                    handler.postDelayed(this, DELAY)
                } else {
                    handler.removeCallbacks(this)
                    if (state == PlayerState.END) {
                        playingLiveData.postValue(PlaybackState(false, STARTTIMER))
                    }
                }
            }
        }
    }

    fun onPause() {
        playerInteractor.pause()
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