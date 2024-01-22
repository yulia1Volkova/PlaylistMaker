package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imageBack = findViewById<ImageView>(R.id.image_back)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val texViewSupport = findViewById<TextView>(R.id.textViewSupport)
        val textViewShare = findViewById<TextView>(R.id.textViewShare)
        val textViewUseAgreement = findViewById<TextView>(R.id.textViewUseAgreement)

        viewModel.getThemeSwitcherLiveData().observe(this) { darkThemeEnabled -> themeSwitcher.isChecked=darkThemeEnabled}

        imageBack.setOnClickListener {
            finish()
        }

        texViewSupport.setOnClickListener {
            viewModel.openSupport()
        }

        textViewShare.setOnClickListener {
            viewModel.shareApp()
        }

        textViewUseAgreement.setOnClickListener {
            viewModel.openTerms()
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }
    }
}