package com.practicum.playlistmaker.media.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding

class FavoriteFragment: Fragment() {

    companion object{
        fun newInstance()=FavoriteFragment()
    }

    private var _binding: FragmentFavoriteBinding?=null
    private val  binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding!!.root
    }
}