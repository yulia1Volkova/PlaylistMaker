package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
): PlayerInteractor {

    override fun createPlayer(url: String){
        playerRepository.createPlayer(url)
    }

    override fun play(){
        playerRepository.play()
    }
    override fun pause(){
        playerRepository.pause()
    }
    override fun getState(): PlayerState {
         return playerRepository.getState()
    }

    override  fun getCurrentPosition():String{
        return playerRepository.getCurrentPosition()
    }
    override  fun release(){
        playerRepository.release()
    }
    override fun stop(){
        playerRepository.stop()
    }

}
