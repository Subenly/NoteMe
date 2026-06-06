package com.example.noteme.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.noteme.R
import com.example.noteme.utils.AuthManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val authManager = AuthManager(this)
        val splashTimeOut = 2000L

        Handler(Looper.getMainLooper()).postDelayed({
            // Cek status login
            if (authManager.isLoggedIn()) {
                // Jika sudah login, langsung ke halaman Utama
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Jika belum, ke Onboarding
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish()
        }, splashTimeOut)
    }
}