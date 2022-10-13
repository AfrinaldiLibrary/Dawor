package com.afrinaldi.dawor.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.afrinaldi.dawor.R
import com.afrinaldi.dawor.databinding.ActivitySigninBinding
import com.afrinaldi.dawor.ui.helper.PREF_TOKEN
import com.afrinaldi.dawor.ui.helper.PrefHelper
import com.afrinaldi.dawor.ui.helper.Validation
import com.afrinaldi.dawor.ui.home.MainActivity
import com.afrinaldi.dawor.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivitySigninBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var prefHelper: PrefHelper
    private lateinit var validation: Validation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefHelper = PrefHelper(this)
        auth = FirebaseAuth.getInstance()
        validation = Validation()

        inputValidation()

        binding.btnSignIn.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)
    }

    private fun inputValidation() {
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_sign_up -> {
                Intent(this, SignUpActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.btn_sign_in -> {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (!validation.isValidEmail(email)){
            binding.etEmail.error = "email tidak valid"
        }

        if (!validation.isValidPassword(password)){
            binding.etPassword.error = "password terdiri dari 1 angka, 1 lowecase, 1 uppercase"
        }

        if (validation.isValidEmail(email) && validation.isValidPassword(password)){
            isLoading(true)
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful){
                    isLoading(false)
                    val token = auth.currentUser?.uid.toString()
                    saveSession(token)
                    Intent(this, MainActivity::class.java).also { toMainActivity ->
                        startActivity(toMainActivity)
                        finish()
                    }
                    Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                } else{
                    isLoading(false)
                    Toast.makeText(this, "Login gagal, periksa kembali email dan password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isLoading(loading : Boolean){
        if (loading) binding.progressBar.visibility = View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    private fun saveSession(token: String){
        prefHelper.put(PREF_TOKEN, token)
    }

    override fun onStart() {
        super.onStart()
        if (prefHelper.getString(PREF_TOKEN) != null){
            Intent(this, MainActivity::class.java).also { toMainActivity ->
                startActivity(toMainActivity)
                finish()
            }
        }
    }
}