package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchEditTextValue: String = ""

    private val baseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val ITunesService = retrofit.create(ITunesAPI::class.java)

    private val tracks = ArrayList<Track>()

    private val adapter = TrackAdapter()

    private lateinit var placeholderMessage: TextView
    private lateinit var imageBack: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var recyclerTrack: RecyclerView
    private lateinit var placeholderImage: ImageView
    private lateinit var nothingFindImage: ImageView
    private lateinit var noInternetMessage: TextView
    private lateinit var updateButton: Button


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
        noInternetMessage= findViewById(R.id.noInternetMessage)
        updateButton= findViewById(R.id.updateButton)

        adapter.tracks = tracks

        recyclerTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTrack.adapter = adapter

        imageBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            tracks.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request()
                true
            }
            false
        }

        updateButton.setOnClickListener {
            request()
            placeholderImage.visibility = View.GONE
            noInternetMessage.visibility = View.GONE
            updateButton.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchEditTextValue = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
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


    private companion object {
        const val SEARCH_EDITTEXT = "SEARCH_EDITTEXT"
    }

    fun request(){
        if (searchEditText.text.isNotEmpty()) {
            ITunesService.search(searchEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            nothingFindImage.visibility = View.GONE
                            placeholderMessage.visibility = View.GONE
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                showMessage(getString(R.string.nothing_found), true)
                            } else {
                                showMessage("", true)
                            }
                        } else {
                            showMessage(
                                getString(R.string.something_went_wrong),

                                false
                            )
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

}