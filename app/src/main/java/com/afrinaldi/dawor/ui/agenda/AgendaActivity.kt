package com.afrinaldi.dawor.ui.agenda

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.afrinaldi.dawor.databinding.ActivityAgendaBinding
import com.afrinaldi.dawor.ui.adapter.AgendaAdapter
import com.afrinaldi.dawor.ui.helper.*
import com.afrinaldi.dawor.ui.model.AgendaModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class AgendaActivity : AppCompatActivity() {
    private var _binding : ActivityAgendaBinding? = null
    private val binding get() = _binding!!

    private val listAgenda = ArrayList<AgendaModel>()
    private lateinit var db: FirebaseDatabase
    private lateinit var prefHelper: PrefHelper

    private var agendaModel = AgendaModel()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAgendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.database
        prefHelper = PrefHelper(this)

        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy")
        val dateNow = LocalDateTime.now().format(formatter)
        binding.tvDate.text = dateNow

        val name = intent.getStringExtra(NAME)
        binding.tvAgendaName.text = "Daftar Kegiatan - $name"

        readData(name!!)

        binding.fab.setOnClickListener{
            AddAgendaDialog(listener = {agenda, time ->
                addData(agenda, time, name)
            }).show(supportFragmentManager, "tag")
        }
    }

    private fun addData(agenda: String, time: String, name: String) {
        agendaModel.title = agenda
        agendaModel.startTime = time

        val profRef = db.reference.child("Profile")
        val currentDb = profRef.child(prefHelper.getString(PREF_TOKEN)!!)
        val nameRef = currentDb.child("Participant").child(name)

        nameRef.child("Agenda").child(agenda).setValue(agendaModel)

    }

    private fun readData(name: String) {
        val profRef = db.reference.child("Profile")
        val currentDb = profRef.child(prefHelper.getString(PREF_TOKEN)!!)
        val nameRef = currentDb.child("Participant").child(name)
        val agendaRef = nameRef.child("Agenda")

        agendaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listAgenda.clear()
                for (data in snapshot.children){
                    val agenda = data.getValue(AgendaModel::class.java)
                    listAgenda.add(agenda!!)
                }

                if (listAgenda.isNotEmpty()){
                    binding.rvListParticipant.adapter = AgendaAdapter(listAgenda) {
                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
                        val timeNow = LocalDateTime.now().format(formatter)
                        agendaModel.endTime = timeNow

                        agendaRef.child(it.title!!).child("endTime").setValue(timeNow)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AgendaActivity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}