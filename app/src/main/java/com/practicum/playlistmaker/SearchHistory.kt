package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun read(): Array<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun write(tracks: MutableList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    fun editList(searchHistoryList: MutableList<Track>, track: Track): MutableList<Track> {
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


}