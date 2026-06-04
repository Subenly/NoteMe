package com.example.noteme.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.noteme.R
import com.example.noteme.ui.fragment.CalendarFragment
import com.example.noteme.ui.fragment.HomeFragment
import com.example.noteme.ui.fragment.NotesFragment
import com.example.noteme.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Mengatur padding agar konten tidak tertutup oleh navigasi sistem bawaan HP
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Siapkan menu navigasi bawah beserta logika kliknya
        setupBottomNavigation()

        // 2. Tampilkan HomeFragment pertama kali saat aplikasi baru dibuka
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    // Fungsi untuk mengganti isi halaman di area fragment_container
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Fungsi untuk mengatur ikon, teks, dan aksi klik pada Custom Bottom Navigation
    private fun setupBottomNavigation() {
        // -- Menu Home --
        val menuHome = findViewById<View>(R.id.menuHome)
        menuHome.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_home)
        menuHome.findViewById<TextView>(R.id.textMenu).text = "Home"
        menuHome.setOnClickListener {
            loadFragment(HomeFragment())
        }

        // -- Menu Notes --
        val menuNotes = findViewById<View>(R.id.menuNotes)
        menuNotes.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_notes) // Pastikan Anda punya ic_notes
        menuNotes.findViewById<TextView>(R.id.textMenu).text = "Notes"
        menuNotes.setOnClickListener {
            loadFragment(NotesFragment())
        }

        // -- Menu Calendar --
        val menuCalendar = findViewById<View>(R.id.menuCalendar)
        menuCalendar.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_profile) // Pastikan Anda punya ic_calendar
        menuCalendar.findViewById<TextView>(R.id.textMenu).text = "Calendar"
        menuCalendar.setOnClickListener {

        }

        // -- Menu Profile --
        val menuProfile = findViewById<View>(R.id.menuProfile)
        menuProfile.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_profile) // Pastikan Anda punya ic_profile
        menuProfile.findViewById<TextView>(R.id.textMenu).text = "Profile"
        menuProfile.setOnClickListener {

        }
    }
}