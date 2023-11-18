package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HISTORY_PREFERENCES = "history_preferences"
const val HISTORY_KEY = "key_for_history"

class SearchActivity : AppCompatActivity(), TrackAdapter.TrackClickListener {
    private companion object {
        const val SEARCH_EDITTEXT = "SEARCH_EDITTEXT"
    }

    private lateinit var editSearchHistory: MutableList<Track>
    private var searchEditTextValue: String = ""
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val ITunesService = retrofit.create(ITunesAPI::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(this)
    private val adapterHistory = TrackAdapter(this)

    private lateinit var placeholderMessage: TextView
    private lateinit var imageBack: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var placeholderImage: ImageView
    private lateinit var nothingFindImage: ImageView
    private lateinit var noInternetMessage: TextView
    private lateinit var updateButton: Button
    private lateinit var clearHistoryBtn: Button
    private lateinit var historyTextView: TextView
    private lateinit var searchHistory: SearchHistory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        imageBack = findViewById(R.id.image_back)
        clearButton = findViewById(R.id.clearIcon)
        searchEditText = findViewById(R.id.searchEditText)
        recyclerTrack = findViewById(R.id.trackView)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage = findViewById(R.id.placeholderImage)
        nothingFindImage = findViewById(R.id.nothingFindImage)
        noInternetMessage = findViewById(R.id.noInternetMessage)
        updateButton = findViewById(R.id.updateButton)
        clearHistoryBtn = findViewById(R.id.clearHistory)
        historyTextView = findViewById(R.id.historyTextView)

        var historySharedPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(historySharedPreferences)

        val searchHistoryList: MutableList<Track> =
            ((searchHistory.read())?.toMutableList() ?: listOf<Track>()).toMutableList()

        adapterHistory.tracks = searchHistoryList as ArrayList<Track>
        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTrack.adapter = adapterHistory

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            historyTextView.visibility =
                if (hasFocus && searchEditText.text.isEmpty() && searchHistoryList.size!=0) View.VISIBLE else View.GONE
            clearHistoryBtn.visibility = if (hasFocus && searchEditText.text.isEmpty()&& searchHistoryList.size!=0) View.VISIBLE else View.GONE
            recyclerTrack.visibility =
                if (hasFocus && searchEditText.text.isEmpty() && searchHistoryList.size!=0) View.VISIBLE else View.GONE
        }

        imageBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val searchHistoryList: MutableList<Track> =//searchHistory.read()!!.toMutableList()
                ((searchHistory.read())?.toMutableList() ?: listOf<Track>()).toMutableList()
            if (searchHistoryList.size == 0){
                clearHistoryBtn.visibility= View.GONE
                historyTextView.visibility= View.GONE
            } else{
                adapterHistory.tracks = searchHistoryList as ArrayList<Track>
                recyclerTrack.adapter = adapterHistory
                adapterHistory.notifyDataSetChanged()
                clearHistoryBtn.visibility= View.VISIBLE
                historyTextView.visibility= View.VISIBLE
                recyclerTrack.visibility= View.VISIBLE
            }

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            nothingFindImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            noInternetMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
        }

        clearHistoryBtn.setOnClickListener {

            historySharedPreferences.edit()
                .clear()
                .apply()
            historyTextView.visibility = View.GONE
            clearHistoryBtn.visibility = View.GONE
            recyclerTrack.visibility = View.GONE

        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request()
                true
            }
            false
        }

        updateButton.setOnClickListener {

            placeholderMessage.visibility = View.GONE
            noInternetMessage.visibility = View.GONE
            updateButton.visibility = View.GONE
            nothingFindImage.visibility = View.GONE
            placeholderImage.visibility = View.GONE

            request()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchEditTextValue = s.toString()
                clearButton.visibility = clearButtonVisibility(s)

                if (searchEditText.hasFocus() && s?.isEmpty() == true && searchHistoryList.size != 0) {
                    historyTextView.visibility = View.VISIBLE
                    clearHistoryBtn.visibility = View.VISIBLE
                    recyclerTrack.visibility = View.VISIBLE

                } else {
                    historyTextView.visibility = View.GONE
                    clearHistoryBtn.visibility = View.GONE
                    recyclerTrack.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_EDITTEXT, searchEditTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        super.onRestoreInstanceState(savedInstanceState)
        searchEditTextValue = savedInstanceState.getString(SEARCH_EDITTEXT, "")
        searchEditText.setText(searchEditTextValue)
    }

    private fun request() {
        if (searchEditText.text.isNotEmpty()) {
            ITunesService.search(searchEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            nothingFindImage.visibility = View.GONE
                            placeholderMessage.visibility = View.GONE
                            recyclerTrack.adapter = adapter
                            if (response.body()?.results?.isNotEmpty() == true) {
                                recyclerTrack.visibility = View.VISIBLE
                                clearHistoryBtn.visibility = View.GONE
                                historyTextView.visibility = View.GONE

                                tracks.addAll(response.body()?.results!!)
                                adapter.tracks=tracks
                                adapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                showMessage(getString(R.string.nothing_found), true)
                            } else {
                                showMessage("", true)
                            }
                        } else {
                            showMessage(getString(R.string.something_went_wrong), false)
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showMessage(
                            getString(R.string.something_went_wrong),
                            false
                        )
                    }
                })
        }
    }

    private fun showMessage(text: String, image: Boolean) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (image)
                nothingFindImage.visibility = View.VISIBLE
            else {
                placeholderImage.visibility = View.VISIBLE
                noInternetMessage.visibility = View.VISIBLE
                updateButton.visibility = View.VISIBLE
            }
        }
    }

    
    override fun onTrackClick(track: Track) {
        var historySharedPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(historySharedPreferences)
        val searchHistoryList: MutableList<Track> =//searchHistory.read()!!.toMutableList()
            ((searchHistory.read())?.toMutableList() ?: listOf<Track>()).toMutableList()
        editSearchHistory = searchHistory.editList(searchHistoryList, track)
        adapterHistory.tracks = editSearchHistory as ArrayList<Track>
        adapterHistory.notifyDataSetChanged()
        val playerIntent = Intent(this, Audioplayer::class.java)
        playerIntent.putExtra("track", track)
        startActivity(playerIntent)


    }
}