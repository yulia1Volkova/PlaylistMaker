package com.practicum.playlistmaker.search.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import com.practicum.playlistmaker.search.ui.view_model.TrackSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.TrackClickListener {

    private companion object {
        const val TRACK = "TRACK"
        const val SEARCH_EDITTEXT = "SEARCH_EDITTEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by viewModel<TrackSearchViewModel>()
    private var isClickAllowed = true
    private var searchEditTextValue: String = ""
    private val adapter = TrackAdapter(this)
    private lateinit var textWatcher: TextWatcher
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.trackView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.getHistory()
        }


        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText("")
            viewModel.getHistory()


            val imm =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
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
            val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
            playerIntent.putExtra(TRACK, track)
            startActivity(playerIntent)
            viewModel.historyAddTrack(track)
        }
    }
}