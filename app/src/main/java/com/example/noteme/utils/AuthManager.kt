package com.example.noteme.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AuthManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PASSWORD = "user_password" // Tambahan untuk simulasi akun
    }

    // Fungsi untuk mendaftarkan akun baru (Register)
    fun registerUser(name: String, email: String, pass: String) {
        sharedPref.edit {
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PASSWORD, pass)
        }
    }

    // Fungsi untuk cek apakah email & pass cocok (Login)
    fun checkLogin(email: String, pass: String): Boolean {
        val savedEmail = sharedPref.getString(KEY_USER_EMAIL, "")
        val savedPass = sharedPref.getString(KEY_USER_PASSWORD, "")
        
        return if (email == savedEmail && pass == savedPass) {
            setLoginStatus(true)
            true
        } else {
            false
        }
    }

    fun setLoginStatus(isLoggedIn: Boolean) {
        sharedPref.edit { putBoolean(KEY_IS_LOGGED_IN, isLoggedIn) }
    }

    fun isLoggedIn(): Boolean = sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    fun getUserName(): String = sharedPref.getString(KEY_USER_NAME, "User") ?: "User"
    fun getUserEmail(): String = sharedPref.getString(KEY_USER_EMAIL, "user@noteme.app") ?: "user@noteme.app"

    fun logout() {
        // Hanya hapus status login, JANGAN hapus data user (agar bisa login lagi nanti)
        sharedPref.edit { putBoolean(KEY_IS_LOGGED_IN, false) }
    }
}