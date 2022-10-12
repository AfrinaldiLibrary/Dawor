package com.afrinaldi.dawor.ui.helper

import android.util.Patterns
import java.util.regex.Pattern

class Validation {

    fun isValidName(name: String?) : Boolean {
        val passwordREGEX = Pattern.compile("^.{3,50}$")
        return passwordREGEX.matcher(name.toString()).matches()
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String?) : Boolean {
        val passwordREGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")
        return passwordREGEX.matcher(password.toString()).matches()
    }
}