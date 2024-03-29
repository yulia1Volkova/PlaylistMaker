package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.Track

const val HISTORY_KEY = "key_for_history"

class HistoryRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : HistoryRepository {

    override fun getTracks(): Array<Track> {
        val json =
            sharedPrefs.getString(HISTORY_KEY, null)
                ?: return emptyArray()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    override fun addTrack(track: Track): List<Track> {
        val searchHistoryList = getTracks().toMutableList()
        val index = searchHistoryList.indexOfFirst { it.trackId == track.trackId }
        if (index != -1) {
            searchHistoryList.removeAt(index)
            searchHistoryList.add(0, track)
        } else if (searchHistoryList.size < 10) {
            searchHistoryList.add(0, track)
        } else {
            searchHistoryList.removeAt(9)
        }
        write(searchHistoryList)

        return searchHistoryList
    }

    override fun cleanHistory() {
        sharedPrefs.edit()
            .clear()
            .apply()
    }

    private fun write(tracks: MutableList<Track>) {
        val json = gson.toJson(tracks)
        sharedPrefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }
}