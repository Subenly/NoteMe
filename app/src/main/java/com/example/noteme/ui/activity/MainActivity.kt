package com.example.noteme.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noteme.R
import com.example.noteme.ui.fragment.CalendarFragment
import com.example.noteme.ui.fragment.HomeFragment
import com.example.noteme.ui.fragment.NotesFragment
import com.example.noteme.ui.fragment.ProfileFragment
import com.example.noteme.worker.ReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // 1. TAMBAHAN: Launcher untuk memunculkan pop-up minta izin notifikasi
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Izin notifikasi ditolak. Anda mungkin melewatkan pengingat catatan.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Terapkan tema yang disimpan sebelum memanggil super.onCreate
        applySavedTheme()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 2. TAMBAHAN: Jalankan pengecekan izin notifikasi
        askNotificationPermission()

        setupDailyReminderCheck()

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

    // 3. TAMBAHAN: Fungsi untuk meminta izin POST_NOTIFICATIONS pada Android 13+ (TIRAMISU)
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Jika belum diizinkan, munculkan dialog permintaan izin bawaan Android
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
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

    private fun setupDailyReminderCheck() {
        // 1. Ini jadwal aslinya (berjalan 24 jam sekali di background)
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyReminderWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        // 2. --- KODE TES: Paksa berjalan SEKARANG JUGA ---
        val testRequest = androidx.work.OneTimeWorkRequestBuilder<ReminderWorker>().build()
        WorkManager.getInstance(this).enqueue(testRequest)
    }
}