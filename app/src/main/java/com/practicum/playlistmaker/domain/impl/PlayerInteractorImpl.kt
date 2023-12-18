package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.models.PlayerState
import com.practicum.playlistmaker.domain.api.PlayerRepository

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
    override fun getState(): PlayerState{
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
