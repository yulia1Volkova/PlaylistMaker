package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.player.ui.mapper.TrackMapper
import com.practicum.playlistmaker.player.ui.model.TrackInfo
import com.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerActivity : AppCompatActivity() {
    companion object{
        const val TRACK = "TRACK"
    }
   private lateinit var playerTrack: TrackInfo

    val viewModel:AudioPlayerViewModel by viewModel{
        parametersOf(playerTrack)
    }

    private lateinit var binding: ActivityAudioplayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerTrack = TrackMapper().map(
            intent.getSerializableExtra(TRACK) as Track
        )


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