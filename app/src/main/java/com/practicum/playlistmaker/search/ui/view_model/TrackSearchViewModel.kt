package com.practicum.playlistmaker.search.ui.view_model

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.HistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackSearchViewModel(
    application: Application,
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : AndroidViewModel(application) {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<TrackSearchState>()
    fun observeState(): LiveData<TrackSearchState> = stateLiveData

    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TrackSearchState.Loading)

            viewModelScope.launch {
                searchInteractor.searchTracks(newSearchText).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }
        when {
            errorMessage == "Internet" -> {
                renderState(
                    TrackSearchState.Error(
                        getApplication<Application>().getString(R.string.something_went_wrong)
                    )
                )
            }

            tracks.isEmpty() -> {
                renderState(
                    TrackSearchState.Empty(
                        getApplication<Application>().getString(R.string.nothing_found)
                    )
                )
            }

            else -> {
                renderState(
                    TrackSearchState.SearchContent(
                        tracks
                    )
                )
            }
        }

    }
    private fun renderState(state: TrackSearchState) {
        stateLiveData.postValue(state)
    }

    fun historyAddTrack(track: Track) {
        val newHisroryList = historyInteractor.addTrack(track) as ArrayList<Track>
        renderState(
            TrackSearchState.HistoryContent(
                newHisroryList, false
            )
        )
    }

    fun getHistory() {
        val historyTracks: MutableList<Track> = historyInteractor.getTracks().toMutableList()
        renderState(
            TrackSearchState.HistoryContent(
                historyTracks, false
            )
        )
    }

    fun cleanHistory() {
        historyInteractor.cleanHistory()
        renderState(
            TrackSearchState.HistoryContent(
                ArrayList(), false
            )
        )
    }

}