package com.practicum.playlistmaker.media.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.db.domain.api.FavoritesInteractor
import com.practicum.playlistmaker.media.ui.models.FavoriteState
import com.practicum.playlistmaker.search.domain.models.Track

import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import kotlinx.coroutines.launch

class FavoriteFragmentViewModel(
    private val favoritesInteractor: FavoritesInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()
    init {
        getData()
    }
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun getData(){
        viewModelScope.launch {
            favoritesInteractor.getFavoritesTracks().collect { tracks -> getState(tracks) }

        }
    }
    fun getState(tracks:List<Track>){
        if (tracks == null) {
            stateLiveData.postValue(FavoriteState.noFavorite)
        } else {
            if (tracks.isEmpty()) {
                stateLiveData.postValue(FavoriteState.noFavorite)
            } else {
                stateLiveData.postValue(FavoriteState.FavoriteContent(tracks))
            }
        }
    }
}


