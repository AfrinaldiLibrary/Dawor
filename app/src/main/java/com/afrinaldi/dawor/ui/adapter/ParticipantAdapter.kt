package com.afrinaldi.dawor.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afrinaldi.dawor.databinding.ItemParticipantBinding
import com.afrinaldi.dawor.ui.model.ParticipantModel

class ParticipantAdapter (private val data: List<ParticipantModel>, private val listener: (ParticipantModel) -> Unit) : RecyclerView.Adapter<ParticipantAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemParticipantBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ParticipantModel, listener: (ParticipantModel) -> Unit){
            binding.tvName.text = item.name
            itemView.setOnClickListener {
                listener(item)
            }
        }
    }



}