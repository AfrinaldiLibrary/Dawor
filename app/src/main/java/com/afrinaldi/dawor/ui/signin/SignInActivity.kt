package com.afrinaldi.dawor.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.afrinaldi.dawor.R
import com.afrinaldi.dawor.databinding.ActivitySigninBinding
import com.afrinaldi.dawor.ui.signup.SignUpActivity

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding : ActivitySigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_sign_up -> {
                Intent(this, SignUpActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }
}