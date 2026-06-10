package com.example.noteme.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.noteme.R
import com.example.noteme.model.NoteManager
import com.example.noteme.utils.AuthManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val authManager = AuthManager(this)
        
        // PENTING: Muat catatan dari memori HP ke dalam NoteManager saat app mulai
        NoteManager.loadNotes(this)

        val splashTimeOut = 2000L

        Handler(Looper.getMainLooper()).postDelayed({
            if (authManager.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish()
        }, splashTimeOut)
    }
}