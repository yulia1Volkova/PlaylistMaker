package com.practicum.playlistmaker.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.models.PlayerState
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.mapper.TrackMapper


class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val DELAY = 500L
        private const val STARTTIMER = "00:00"
    }

    private val playerInteractor = Creator.providePlayerInteractor()

    private var mainThreadHandler: Handler? = null
    private var url: String = ""
    private lateinit var play: ImageButton
    private lateinit var textViewTimer: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backButton = findViewById<ImageButton>(R.id.backButtonImage)
        val imageCover = findViewById<ImageView>(R.id.coverImageView)
        val textViewTrackName = findViewById<TextView>(R.id.trackNameTextView)
        val textViewArtistName = findViewById<TextView>(R.id.artistNameTextView)
        val textViewDuration = findViewById<TextView>(R.id.durationDataTextView)
        val textViewAlbum = findViewById<TextView>(R.id.albumDataTextView)
        val textViewYear = findViewById<TextView>(R.id.yearDataTextView)
        val textViewGenre = findViewById<TextView>(R.id.genreDataTextView)
        val textViewCountry = findViewById<TextView>(R.id.countryDataTextView)
        val albumGroup = findViewById<Group>(R.id.albumGroup)
        play = findViewById(R.id.playButtonImageButton)
        textViewTimer = findViewById(R.id.timerTextView)

        val playerTrack = TrackMapper().map(
            intent.getSerializableExtra("track") as Track
        )

        url = playerTrack.previewUrl
        playerInteractor.createPlayer(url)
        play.isEnabled = true
        play.setImageResource(R.drawable.play)
        textViewTimer.text = STARTTIMER

        mainThreadHandler = Handler(Looper.getMainLooper())

        backButton.setOnClickListener {
            finish()
        }

        play.setOnClickListener {
            playbackControl()
        }

        Glide.with(applicationContext)
            .load(playerTrack.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(imageCover)

        textViewTrackName.text = playerTrack.trackName
        textViewArtistName.text = playerTrack.artistName
        textViewDuration.text = playerTrack.trackTime
        textViewYear.text = playerTrack.releaseDate
        textViewGenre.text = playerTrack.primaryGenreName
        textViewCountry.text = playerTrack.country

        if (playerTrack.collectionName.isNullOrEmpty()) {
            albumGroup.visibility = View.GONE
        } else {
            albumGroup.visibility = View.VISIBLE
            textViewAlbum.text = playerTrack.collectionName
        }

    }

    private fun playbackControl() {
        when (playerInteractor.getState()) {
            PlayerState.PLAYING -> {
                playerInteractor.pause()
                play.setImageResource(R.drawable.play)
            }

            PlayerState.END -> {
                textViewTimer.text = STARTTIMER
                statePlaying()
            }

            PlayerState.PREPARED, PlayerState.PAUSED, PlayerState.DEFAULT -> {
                statePlaying()

            }
        }
    }

    private fun statePlaying() {
        playerInteractor.play()
        play.setImageResource(R.drawable.pause)
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )
    }


    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val state: PlayerState = playerInteractor.getState()
                if (state == PlayerState.PLAYING) {
                    textViewTimer.text = playerInteractor.getCurrentPosition()
                    mainThreadHandler?.postDelayed(this, DELAY)
                } else {
                    mainThreadHandler?.removeCallbacks(this)
                    if (state == PlayerState.END) {
                        play.setImageResource(R.drawable.play)
                        textViewTimer.text = STARTTIMER
                    }
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
        play.setImageResource(R.drawable.play)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
    }
}