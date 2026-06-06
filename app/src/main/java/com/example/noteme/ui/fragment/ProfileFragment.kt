package com.example.noteme.ui.fragment

import android.app.AlertDialog
import android.content.Context
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

class ProfileFragment : Fragment() {

    private lateinit var ivProfilePhoto: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
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
                // Simpan URI foto ke SharedPreferences
                saveImageUri(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto)
        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        
        setupClickListeners(view)
        setupDarkMode(view)
        
        // Memuat data dari SharedPreferences
        loadProfileData()
    }

    private fun loadProfileData() {
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        
        // Load Nama & Email
        tvUserName.text = sharedPref.getString("name", getString(R.string.user_name))
        tvUserEmail.text = sharedPref.getString("email", getString(R.string.user_email))
        
        // Load Foto Profil jika ada
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
        
        // Menu lainnya (Toast)
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
            view.findViewById<View>(id).setOnClickListener { showToast(message) }
        }

        view.findViewById<View>(R.id.btnLogout).setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupDarkMode(view: View) {
        val switchDarkMode = view.findViewById<SwitchCompat>(R.id.switchDarkMode)
        val sharedPref = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        
        // Load setting dark mode
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
                showToast("Logged out successfully")
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}