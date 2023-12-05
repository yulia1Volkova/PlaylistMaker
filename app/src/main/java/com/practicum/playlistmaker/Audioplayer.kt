package com.practicum.playlistmaker

import android.media.MediaPlayer
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
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }

    private var mainThreadHandler: Handler? = null
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
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

        val playerTrack = intent.getSerializableExtra("track") as Track
        url = playerTrack.previewUrl
        preparePlayer()
        mainThreadHandler = Handler(Looper.getMainLooper())

        backButton.setOnClickListener {
            finish()
        }

        play.setOnClickListener {
            playbackControl()
        }


        Glide.with(applicationContext)
            .load(playerTrack.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(imageCover)

        textViewTrackName.text = playerTrack.trackName
        textViewArtistName.text = playerTrack.artistName
        textViewDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerTrack.trackTimeMillis)

        if (playerTrack.collectionName.isNullOrEmpty()) {
            albumGroup.visibility = View.GONE
        } else {
            albumGroup.visibility = View.VISIBLE
            textViewAlbum.text = playerTrack.collectionName
        }

        textViewYear.text = playerTrack.releaseDate.substring(0, 4)
        textViewGenre.text = playerTrack.primaryGenreName
        textViewCountry.text = playerTrack.country

    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.play)
            textViewTimer.text="00:00"
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {

                mediaPlayer.getCurrentPosition()
                        if (playerState==STATE_PLAYING) {
                    textViewTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, DELAY)
                }
                else{
                    mainThreadHandler?.removeCallbacks(this)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}