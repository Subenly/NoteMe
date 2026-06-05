package com.example.noteme.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
        // 1. Terapkan tema yang disimpan sebelum memanggil super.onCreate
        applySavedTheme()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), false)
        }
    }

    private fun applySavedTheme() {
        val sharedPref = getSharedPreferences("Settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun setupBottomNavigation() {
        val menuHome = findViewById<View>(R.id.menuHome)
        menuHome.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_home)
        menuHome.findViewById<TextView>(R.id.textMenu).text = "Home"
        menuHome.setOnClickListener { loadFragment(HomeFragment(), false) }

        val menuNotes = findViewById<View>(R.id.menuNotes)
        menuNotes.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_notes)
        menuNotes.findViewById<TextView>(R.id.textMenu).text = "Notes"
        menuNotes.setOnClickListener { loadFragment(NotesFragment(), false) }

        val menuCalendar = findViewById<View>(R.id.menuCalendar)
        menuCalendar.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_calendar)
        menuCalendar.findViewById<TextView>(R.id.textMenu).text = "Calendar"
        menuCalendar.setOnClickListener { loadFragment(CalendarFragment(), false) }

        val menuProfile = findViewById<View>(R.id.menuProfile)
        menuProfile.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_profile)
        menuProfile.findViewById<TextView>(R.id.textMenu).text = "Profile"
        menuProfile.setOnClickListener { loadFragment(ProfileFragment(), true) }
    }
}