package com.practicum.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import com.practicum.playlistmaker.search.ui.view_model.TrackSearchViewModel


class SearchActivity : AppCompatActivity(), TrackAdapter.TrackClickListener {

    private companion object {
        const val SEARCH_EDITTEXT = "SEARCH_EDITTEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var viewModel: TrackSearchViewModel
    private lateinit var binding: ActivitySearchBinding

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var searchEditTextValue: String = ""

    private val adapter = TrackAdapter(this)
        //  private val adapterHistory = TrackAdapter(this)
    private lateinit var textWatcher: TextWatcher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            TrackSearchViewModel.getViewModelFactory()
        )[TrackSearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.trackView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //binding.trackView.adapter = adapterHistory

        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.getHistory()
        }

        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText("")
            viewModel.getHistory()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }

        binding.clearHistory.setOnClickListener {
            viewModel.cleanHistory()
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                searchEditTextValue = s?.toString() ?: ""
                viewModel.searchDebounce(
                    searchEditTextValue
                )
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcher?.let { binding.searchEditText.addTextChangedListener(it) }


        binding.updateButton.setOnClickListener {
            viewModel.searchRequest(searchEditTextValue)

        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
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

    private fun render(state: TrackSearchState) {
        when (state) {
            is TrackSearchState.SearchContent -> showContent(state.trackInfo)
            is TrackSearchState.Empty -> showEmpty(state.message)
            is TrackSearchState.Error -> showError(state.errorMessage)
            is TrackSearchState.Loading -> showLoading()
            is TrackSearchState.HistoryContent -> showHistory(state.trackInfo, state.isEmpty)
        }
    }

    private fun showContent(tracks: MutableList<Track>) {
        binding.nothingFindImage.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.noInternetMessage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.trackView.visibility = View.VISIBLE
        binding.clearHistory.visibility = View.GONE
        binding.historyTextView.visibility = View.GONE
        binding.trackView.adapter = adapter
        adapter.tracks = tracks as ArrayList<Track>
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty(message: String) {
        binding.nothingFindImage.visibility = View.VISIBLE
        binding.placeholderImage.visibility = View.GONE
        binding.noInternetMessage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.trackView.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        binding.historyTextView.visibility = View.GONE
        binding.placeholderMessage.text = message
    }

    private fun showError(message: String) {
        binding.nothingFindImage.visibility = View.GONE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.noInternetMessage.visibility = View.VISIBLE
        binding.updateButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.trackView.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        binding.historyTextView.visibility = View.GONE
        binding.placeholderMessage.text = message
    }

    private fun showLoading() {
        binding.nothingFindImage.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.noInternetMessage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.trackView.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        binding.historyTextView.visibility = View.GONE
    }

    private fun showHistory(tracks: MutableList<Track>, isEmpty: Boolean) {
        binding.nothingFindImage.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.noInternetMessage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.trackView.visibility = View.VISIBLE
        if (tracks.isEmpty()) {
            binding.clearHistory.visibility = View.GONE
            binding.historyTextView.visibility = View.GONE
        } else {
            binding.clearHistory.visibility = View.VISIBLE
            binding.historyTextView.visibility = View.VISIBLE
        }
        binding.trackView.adapter = adapter
        adapter.tracks = tracks as ArrayList<Track>
        adapter.notifyDataSetChanged()
    }


    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            val playerIntent = Intent(this, AudioPlayerActivity::class.java)
            playerIntent.putExtra("track", track)
            startActivity(playerIntent)
            viewModel.historyAddTrack(track)
        }
    }
}


