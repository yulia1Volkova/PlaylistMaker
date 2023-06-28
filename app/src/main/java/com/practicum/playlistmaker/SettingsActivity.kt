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
            val backImageIntent = Intent(this, MainActivity::class.java)
            startActivity(backImageIntent)
        }

        val texViewSupport = findViewById<TextView>(R.id.textViewSupport)
        texViewSupport.setOnClickListener {
            val textViewSupportIntent = Intent(Intent.ACTION_SENDTO)
            textViewSupportIntent.data = Uri.parse("mailto:")
            textViewSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            textViewSupportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(textViewSupportIntent)
        }

        val textViewShare = findViewById<TextView>(R.id.textViewShare)
        textViewShare.setOnClickListener {
            val textViewShareIntent = Intent(Intent.ACTION_SEND)
            textViewShareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_link))
            textViewShareIntent.type = "text/plain"
            startActivity(textViewShareIntent)
        }

        val textViewUseAgreement = findViewById<TextView>(R.id.textViewUseAgreement)
        textViewUseAgreement.setOnClickListener {
            val url = getString(R.string.use_agreement_text)
            val textViewUseAgreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(textViewUseAgreementIntent)
        }
    }
}