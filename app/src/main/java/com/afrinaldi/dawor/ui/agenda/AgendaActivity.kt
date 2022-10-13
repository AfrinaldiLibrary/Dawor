package com.afrinaldi.dawor.ui.agenda

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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
    private var name : String? = null

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

        name = intent.getStringExtra(NAME)
        binding.tvAgendaName.text = "Daftar Kegiatan - $name"

        readData(name!!)
        initAction()

        binding.fab.setOnClickListener{
            AddAgendaDialog(listener = {agenda, time ->
                addData(agenda, time)
            }).show(supportFragmentManager, "add")
        }

        binding.tvDelete.setOnClickListener {
            DeleteParticipantDialog(listener = { _ ->
                val profRef = db.reference.child("Profile")
                val currentDb = profRef.child(prefHelper.getString(PREF_TOKEN)!!)
                val nameRef = currentDb.child("Participant").child(name!!)
                nameRef.removeValue()
                Toast.makeText(this, "$name dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }).show(supportFragmentManager, "delete")
        }
    }

    private fun addData(agenda: String, time: String) {
        agendaModel.title = agenda
        agendaModel.startTime = time

        val profRef = db.reference.child("Profile")
        val currentDb = profRef.child(prefHelper.getString(PREF_TOKEN)!!)
        val nameRef = currentDb.child("Participant").child(name!!)

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

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val agenda = (viewHolder as AgendaAdapter.ViewHolder).getAgenda
                deleteAgenda(agenda.title!!)
            }

        })
        itemTouchHelper.attachToRecyclerView(binding.rvListParticipant)
    }

    private fun deleteAgenda(title: String){
        val profRef = db.reference.child("Profile")
        val currentDb = profRef.child(prefHelper.getString(PREF_TOKEN)!!)
        val nameRef = currentDb.child("Participant").child(name!!)
        val agendaRef = nameRef.child("Agenda")

        agendaRef.child(title).removeValue()
        Toast.makeText(this@AgendaActivity, "$title dihapus", Toast.LENGTH_SHORT).show()
    }
}