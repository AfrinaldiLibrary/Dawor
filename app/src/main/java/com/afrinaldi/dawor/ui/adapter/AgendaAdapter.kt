package com.afrinaldi.dawor.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afrinaldi.dawor.databinding.ItemAgendaBinding
import com.afrinaldi.dawor.ui.model.AgendaModel

class AgendaAdapter (private val data: List<AgendaModel>, private val listener: (AgendaModel) -> Unit) : RecyclerView.Adapter<AgendaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAgendaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemAgendaBinding) : RecyclerView.ViewHolder(binding.root){
        lateinit var getAgenda: AgendaModel
        @SuppressLint("SetTextI18n")
        fun bind(item : AgendaModel, listener: (AgendaModel) -> Unit){
            getAgenda = item
            binding.tvAgenda.text = item.title
            binding.tvStart.text = "${item.startTime} - "
            binding.tvEnd.text = item.endTime
            binding.tvEnd.setOnClickListener {
                listener(item)
            }
        }
    }
}