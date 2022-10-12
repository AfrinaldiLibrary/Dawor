package com.afrinaldi.dawor.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.afrinaldi.dawor.R
import com.afrinaldi.dawor.databinding.ActivitySignUpBinding
import com.afrinaldi.dawor.ui.helper.Validation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivitySignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var validation: Validation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = Firebase.database
        validation = Validation()

        inputValidation()

        binding.btnSignUp.setOnClickListener(this)
        binding.tvSignIn.setOnClickListener(this)
    }

    private fun inputValidation() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!validation.isValidName(s.toString())){
                    binding.etName.error = "Min 3 max 50 character"
                }
            }

        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!validation.isValidEmail(s.toString())){
                    binding.etEmail.error = "Email tidak valid"
                }
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!validation.isValidPassword(s.toString())){
                    binding.etPassword.error = "Terdiri dari angka, huruf kecil, huruf besar, dan min 8 character"
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_sign_up -> {
                register()
            }

            R.id.tv_sign_in -> {
                onBackPressed()
            }
        }
    }

    private fun register() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (!validation.isValidName(name)){
            binding.etName.error = "Min 3 max 50 character"
        }

        if (!validation.isValidEmail(email)){
            binding.etEmail.error = "email tidak valid"
        }

        if (!validation.isValidPassword(password)){
            binding.etPassword.error = "password terdiri dari 1 angka, 1 lowecase, 1 uppercase"
        }

        if (validation.isValidName(name) && validation.isValidEmail(email) && validation.isValidPassword(password)){
            isLoading(true)

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if (!it.isSuccessful){
                    isLoading(false)
                    try {
                        throw it.exception!!
                    } catch (email : FirebaseAuthUserCollisionException){
                        Toast.makeText(this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                    } catch (e : Exception){
                        Toast.makeText(this, "Gagal membuat akun, coba lagi!", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    isLoading(false)
                    val user = auth.currentUser

                    val profRef = db.reference.child("Profile")
                    val currentDb = profRef.child(user?.uid!!)
                    currentDb.child("name").setValue(name)
                    Toast.makeText(this, "Akun berhasil dibuat & cek email", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }
        }
    }

    private fun isLoading(loading : Boolean){
        if (loading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }
}