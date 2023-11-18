package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class Audioplayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val imageCover = findViewById<ImageView>(R.id.cover)
        val textViewTrackName = findViewById<TextView>(R.id.trackNameData)
        val textViewArtistName = findViewById<TextView>(R.id.artistNameData)
        val textViewDuration = findViewById<TextView>(R.id.durationData)
        val textViewAlbum = findViewById<TextView>(R.id.albumData)
        val textViewYear = findViewById<TextView>(R.id.yearData)
        val textViewGenre = findViewById<TextView>(R.id.genreData)
        val textViewCountry = findViewById<TextView>(R.id.countryData)
        val albumGroup = findViewById<Group>(R.id.albumGroup)

        val playerTrack = intent.getSerializableExtra("track") as Track

        backButton.setOnClickListener {
            finish()
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

        textViewYear.text = playerTrack.releaseDate.substring(0,4)
        textViewGenre.text = playerTrack.primaryGenreName
        textViewCountry.text = playerTrack.country

    }
}