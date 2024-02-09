package com.practicum.playlistmaker.media.ui.activity

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
const val MEDIA_PAGER_TABS_SIZE=2

class MediaPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = MEDIA_PAGER_TABS_SIZE

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoriteFragment.newInstance() else PlaylistsFragment.newInstance()
    }
}