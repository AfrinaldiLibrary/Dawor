package com.afrinaldi.dawor.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afrinaldi.dawor.R
import com.afrinaldi.dawor.databinding.ActivityMainBinding
import com.afrinaldi.dawor.databinding.ActivitySignUpBinding

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}