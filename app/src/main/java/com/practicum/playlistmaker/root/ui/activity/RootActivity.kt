package com.practicum.playlistmaker.root.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextWatcher
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.search.ui.activity.SearchFragment
import com.practicum.playlistmaker.search.ui.activity.TrackAdapter
import com.practicum.playlistmaker.search.ui.view_model.TrackSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
    }

}