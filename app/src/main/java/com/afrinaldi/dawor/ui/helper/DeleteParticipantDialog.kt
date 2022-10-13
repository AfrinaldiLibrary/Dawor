package com.afrinaldi.dawor.ui.helper

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.afrinaldi.dawor.databinding.DialogDeleteBinding

class DeleteParticipantDialog(private val listener: (Boolean) -> Unit) : DialogFragment() {
    private var _binding : DialogDeleteBinding? = null
    private val binding get() = _binding!!
    private lateinit var isdialog: AlertDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            _binding = DialogDeleteBinding.inflate(LayoutInflater.from(context))

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)

            binding.btnDelete.setOnClickListener {
                listener.invoke(true)
                dismiss()
            }

            binding.btnClose.setOnClickListener {
                isDismiss()
            }

            isdialog = builder.create()
            isdialog
        }
    }

    private fun isDismiss(){
        if(::isdialog.isInitialized){
            isdialog.dismiss()
        }
    }
}