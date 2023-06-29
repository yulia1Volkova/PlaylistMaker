package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imageBack = findViewById<ImageView>(R.id.image_back)
        imageBack.setOnClickListener {
            finish()
        }

        val texViewSupport = findViewById<TextView>(R.id.textViewSupport)
        texViewSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
                startActivity(this)
            }
        }

        val textViewShare = findViewById<TextView>(R.id.textViewShare)
        textViewShare.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_link))
                type = "text/plain"
                startActivity(this)
            }
        }

        val textViewUseAgreement = findViewById<TextView>(R.id.textViewUseAgreement)
        textViewUseAgreement.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.use_agreement_text))).apply {
                startActivity(this)
            }
        }
    }
}