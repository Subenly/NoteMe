package com.example.noteme.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.noteme.R
import com.example.noteme.ui.activity.LoginActivity
import com.example.noteme.utils.AuthManager

class ProfileFragment : Fragment() {

    private lateinit var ivProfilePhoto: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var authManager: AuthManager
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                updateProfileImage(it)
                saveImageUri(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto)
        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        
        setupClickListeners(view)
        setupDarkMode(view)
        
        loadProfileData()
    }

    private fun loadProfileData() {
        // Ambil data dari AuthManager (Data yang diinput saat Register/Login)
        tvUserName.text = authManager.getUserName()
        tvUserEmail.text = authManager.getUserEmail()
        
        // Load Foto Profil dari SharedPreferences UserProfile
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val imageUriString = sharedPref.getString("profile_image", null)
        if (imageUriString != null) {
            updateProfileImage(Uri.parse(imageUriString))
        }
    }

    private fun updateProfileImage(uri: Uri) {
        ivProfilePhoto.setImageURI(uri)
        ivProfilePhoto.imageTintList = null
        ivProfilePhoto.setPadding(0, 0, 0, 0)
    }

    private fun saveImageUri(uri: Uri) {
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        sharedPref.edit().putString("profile_image", uri.toString()).apply()
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<View>(R.id.btnEditPhoto).setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        view.findViewById<View>(R.id.menuPersonalInfo).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PersonalInfoFragment())
                .addToBackStack(null)
                .commit()
        }
        
        val menus = listOf(
            R.id.menuSecurity to "Security Settings",
            R.id.menuBackup to "Backup & Sync",
            R.id.menuTheme to "Theme Customization",
            R.id.menuNotifications to "Notification Settings",
            R.id.menuHelp to "Help Center",
            R.id.menuPrivacy to "Privacy Policy",
            R.id.menuAbout to "About NoteMe"
        )
        
        menus.forEach { (id, message) ->
            view.findViewById<View>(id).setOnClickListener { Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
        }

        view.findViewById<View>(R.id.btnLogout).setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupDarkMode(view: View) {
        val switchDarkMode = view.findViewById<SwitchCompat>(R.id.switchDarkMode)
        val sharedPref = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        
        val isEnabled = sharedPref.getBoolean("dark_mode", false)
        switchDarkMode.isChecked = isEnabled

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("dark_mode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_confirm_title))
            .setMessage(getString(R.string.logout_confirm_msg))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                // 1. Hapus session login
                authManager.logout()
                
                // 2. Arahkan ke LoginActivity dan bersihkan semua riwayat halaman
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
}