package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.player.ui.mapper.TrackMapper
import com.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track

class AudioPlayerActivity : AppCompatActivity() {
    companion object{
        const val TRACK = "TRACK"
    }

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioplayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playerTrack = TrackMapper().map(
            intent.getSerializableExtra(TRACK) as Track
        )

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(playerTrack)
        )[AudioPlayerViewModel::class.java]

        viewModel.create()
        binding.playButtonImageButton.isEnabled = true

        binding.backButtonImage.setOnClickListener {
            finish()
        }

        binding.playButtonImageButton.setOnClickListener {
            viewModel.play()
        }

        viewModel.getplayingLiveData().observe(this) { playbackState ->
            if (playbackState.plays) {
                binding.playButtonImageButton.setImageResource(R.drawable.pause)
            } else {
                binding.playButtonImageButton.setImageResource(R.drawable.play)
            }
            binding.timerTextView.text = playbackState.position
        }

        Glide.with(applicationContext)
            .load(playerTrack.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.coverImageView)

        binding.trackNameTextView.text = playerTrack.trackName
        binding.artistNameTextView.text = playerTrack.artistName
        binding.durationDataTextView.text = playerTrack.trackTime
        binding.yearDataTextView.text = playerTrack.releaseDate
        binding.genreDataTextView.text = playerTrack.primaryGenreName
        binding.countryDataTextView.text = playerTrack.country

        if (playerTrack.collectionName.isEmpty()) {
            binding.albumGroup.visibility = View.GONE
        } else {
            binding.albumGroup.visibility = View.VISIBLE
            binding.albumDataTextView.text = playerTrack.collectionName
        }
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }

}