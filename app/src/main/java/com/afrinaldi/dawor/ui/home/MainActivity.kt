package com.afrinaldi.dawor.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afrinaldi.dawor.databinding.ActivityMainBinding
import com.afrinaldi.dawor.ui.adapter.ParticipantAdapter
import com.afrinaldi.dawor.ui.agenda.AgendaActivity
import com.afrinaldi.dawor.ui.helper.AddParticipantDialog
import com.afrinaldi.dawor.ui.helper.NAME
import com.afrinaldi.dawor.ui.helper.PREF_TOKEN
import com.afrinaldi.dawor.ui.helper.PrefHelper
import com.afrinaldi.dawor.ui.model.AgendaModel
import com.afrinaldi.dawor.ui.model.ParticipantModel
import com.afrinaldi.dawor.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var agendaModel = AgendaModel()
    private val listParticipant = ArrayList<ParticipantModel>()
    private lateinit var prefHelper: PrefHelper

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.database
        auth = FirebaseAuth.getInstance()

        prefHelper = PrefHelper(this)

        binding.rvListParticipant.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
        }

        readData()

        binding.fab.setOnClickListener{
            AddParticipantDialog(listener = {name, agenda, time ->
                addData(name, agenda, time)
            }).show(supportFragmentManager, "tag")
        }

        binding.tvSignOut.setOnClickListener {
            auth.signOut()
            prefHelper.clear()
            Intent(this, SignInActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun readData() {
        isLoading(true)
        val profRef = db.reference.child("Profile")
        val currentDb = profRef.child(prefHelper.getString(PREF_TOKEN)!!)
        val nameRef = currentDb.child("Participant")

        nameRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listParticipant.clear()
                isLoading(false)
                for (data in snapshot.children){
                    val participant = data.getValue(ParticipantModel::class.java)
                    listParticipant.add(participant!!)
                }

                if (listParticipant.isNotEmpty()){
                    binding.rvListParticipant.adapter = ParticipantAdapter(listParticipant) {data ->
                        Intent(this@MainActivity, AgendaActivity::class.java).also {
                            it.putExtra(NAME, data.name)
                            startActivity(it)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading(false)
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun addData(name: String, agenda: String, time: String) {
        agendaModel.title = agenda
        agendaModel.startTime = time

        val profRef = db.reference.child("Profile")
        val currentDb = profRef.child(prefHelper.getString(PREF_TOKEN)!!)
        val nameRef = currentDb.child("Participant").child(name)
//        add participant
        nameRef.child("name").setValue(name)
//        add agenda
        nameRef.child("Agenda").child(agenda).setValue(agendaModel)
    }

    private fun isLoading(loading : Boolean){
        if (loading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}