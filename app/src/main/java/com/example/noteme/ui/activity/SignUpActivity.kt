package com.example.noteme.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteme.R
import com.example.noteme.utils.AuthManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {
    
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        authManager = AuthManager(this)

        val etFullName: TextInputEditText = findViewById(R.id.etFullName)
        val etEmail: TextInputEditText = findViewById(R.id.etEmail)
        val etPassword: TextInputEditText = findViewById(R.id.etPassword)
        val etConfirmPassword: TextInputEditText = findViewById(R.id.etConfirmPassword)
        val btnRegister: MaterialButton = findViewById(R.id.btnRegister)
        val tvSignIn: TextView = findViewById(R.id.tvSignIn)

        btnRegister.setOnClickListener {
            val name = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Please fill in all fields")
            } else if (password != confirmPassword) {
                showToast("Passwords do not match")
            } else {
                // 1. Simpan akun ke "Database" Lokal
                authManager.registerUser(name, email, password)
                
                // 2. Set status langsung login setelah daftar
                authManager.setLoginStatus(true)

                showToast("Account Created Successfully!")
                
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        tvSignIn.setOnClickListener {
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}