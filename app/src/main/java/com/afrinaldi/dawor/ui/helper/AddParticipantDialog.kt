package com.afrinaldi.dawor.ui.helper

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.afrinaldi.dawor.databinding.DialogParticipantBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddParticipantDialog(private val listener: (String, String, String) -> Unit) :
    DialogFragment() {
    private var _binding: DialogParticipantBinding? = null
    private val binding get() = _binding!!
    private lateinit var isdialog: AlertDialog
    private lateinit var validation: Validation

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            _binding = DialogParticipantBinding.inflate(LayoutInflater.from(context))

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            validation = Validation()

            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val timeNow = LocalDateTime.now().format(formatter)

            inputValidation()

            binding.btnSave.setOnClickListener {
                val name = binding.etName.text.toString()
                val agenda = binding.etAgenda.text.toString()

                if (!validation.isValidName(name)){
                    binding.etName.error = "Min 3 character"
                }

                if (!validation.isValidName(agenda)){
                    binding.etAgenda.error = "Min 3 character"
                }

                if (validation.isValidName(name) && validation.isValidName(agenda)){
                    listener.invoke(
                        binding.etName.text.toString(),
                        binding.etAgenda.text.toString(),
                        timeNow
                    )
                    dismiss()
                }
            }

            binding.btnClose.setOnClickListener {
                isDismiss()
            }

            isdialog = builder.create()
            isdialog
        }
    }

    private fun inputValidation() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!validation.isValidName(s.toString())) {
                    binding.etName.error = "Min 3 character"
                }
            }

        })

        binding.etAgenda.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!validation.isValidName(s.toString())) {
                    binding.etAgenda.error = "Min 3 character"
                }
            }

        })
    }

    private fun isDismiss() {
        if (::isdialog.isInitialized) {
            isdialog.dismiss()
        }
    }
}