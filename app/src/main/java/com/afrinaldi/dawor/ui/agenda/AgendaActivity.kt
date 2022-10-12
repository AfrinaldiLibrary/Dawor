package com.afrinaldi.dawor.ui.agenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afrinaldi.dawor.databinding.ActivityAgendaBinding
import com.afrinaldi.dawor.databinding.ActivityMainBinding

class AgendaActivity : AppCompatActivity() {
    private var _binding : ActivityAgendaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}