package com.example.noteme.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noteme.R
import com.example.noteme.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Memanggil fungsi untuk mengatur tampilan menu bawah
        setupBottomNavigation()

        // --- TAMBAHKAN KODE INI ---
        // Memuat HomeFragment sebagai halaman pertama jika aplikasi baru pertama kali dibuka
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }

    private fun setupBottomNavigation() {
        // 1. Setup Menu Home
        val menuHome = findViewById<View>(R.id.menuHome)
        menuHome.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_home)
        menuHome.findViewById<TextView>(R.id.textMenu).text = "Home"

        // 2. Setup Menu Notes
        val menuNotes = findViewById<View>(R.id.menuNotes)
        menuNotes.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_notes) // Ganti dengan nama ikon notes Anda
        menuNotes.findViewById<TextView>(R.id.textMenu).text = "Notes"

        // 3. Setup Menu Calendar
        val menuCalendar = findViewById<View>(R.id.menuCalendar)
        menuCalendar.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_profile) // Ganti dengan nama ikon calendar Anda
        menuCalendar.findViewById<TextView>(R.id.textMenu).text = "Calendar"

        // 4. Setup Menu Profile
        val menuProfile = findViewById<View>(R.id.menuProfile)
        menuProfile.findViewById<ImageView>(R.id.iconMenu).setImageResource(R.drawable.ic_profile) // Ganti dengan nama ikon profile Anda
        menuProfile.findViewById<TextView>(R.id.textMenu).text = "Profile"

        // Catatan: Logika klik (setOnClickListener) untuk mengganti halaman akan kita tambahkan setelah ini.
    }
}