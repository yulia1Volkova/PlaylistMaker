package com.practicum.playlistmaker.media.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.media.ui.models.FavoriteState
import com.practicum.playlistmaker.media.ui.view_model.FavoriteFragmentViewModel
import com.practicum.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.activity.SearchFragment
import com.practicum.playlistmaker.search.ui.activity.TrackAdapter
import com.practicum.playlistmaker.search.ui.models.TrackSearchState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteFragment: Fragment(), TrackAdapter.TrackClickListener {

    companion object{
        fun newInstance()=FavoriteFragment()
        const val TRACK = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private val viewModel by viewModel<FavoriteFragmentViewModel>()
    private var isClickAllowed = true
    private val adapter = TrackAdapter(this)
    private var _binding: FragmentFavoriteBinding?=null
    private val  binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.trackView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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


    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.FavoriteContent -> showContent(state.trackInfo)
            is FavoriteState.noFavorite -> showEmpty()
        }
    }

    private fun showContent(tracks: List<Track>) {
        binding.placeholderImage.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.trackView.visibility = View.VISIBLE
        binding.trackView.adapter = adapter
        adapter.tracks = tracks as ArrayList<Track>
        adapter.notifyDataSetChanged()
    }
    private fun showEmpty(){
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.trackView.visibility = View.GONE
    }

    override fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
            playerIntent.putExtra(TRACK, track)
            startActivity(playerIntent)

        }
    }
}