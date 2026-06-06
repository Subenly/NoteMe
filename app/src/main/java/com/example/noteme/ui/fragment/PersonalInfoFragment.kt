package com.example.noteme.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteme.R
import com.example.noteme.utils.AuthManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PersonalInfoFragment : Fragment() {

    private lateinit var etDisplayName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etDateOfBirth: TextInputEditText
    private lateinit var actvGender: AutoCompleteTextView
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        
        etDisplayName = view.findViewById(R.id.etDisplayName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhone = view.findViewById(R.id.etPhone)
        etDateOfBirth = view.findViewById(R.id.etDateOfBirth)
        actvGender = view.findViewById(R.id.actvGender)

        setupGenderDropdown()
        setupDatePicker()
        setupClickListeners(view)
        
        loadUserData()
    }

    private fun setupGenderDropdown() {
        val genders = listOf(getString(R.string.gender_male), getString(R.string.gender_female), getString(R.string.gender_other))
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genders)
        actvGender.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        etDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                val date = "$day/${month + 1}/$year"
                etDateOfBirth.setText(date)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun loadUserData() {
        // Mengambil nama dan email dari AuthManager
        etDisplayName.setText(authManager.getUserName())
        etEmail.setText(authManager.getUserEmail())
        
        // Mengambil data pendukung lainnya dari SharedPreferences khusus profil
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        etPhone.setText(sharedPref.getString("phone", ""))
        etDateOfBirth.setText(sharedPref.getString("dob", ""))
        
        val savedGender = sharedPref.getString("gender", "")
        if (!savedGender.isNullOrEmpty()) {
            actvGender.setText(savedGender, false)
        }
    }

    private fun saveUserData() {
        val name = etDisplayName.text.toString()
        val email = etEmail.text.toString()

        // 1. Update data utama di AuthManager (supaya Login berikutnya pakai data terbaru ini)
        // Kita ambil password lama supaya tidak berubah
        val sharedPrefAuth = requireActivity().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val currentPass = sharedPrefAuth.getString("user_password", "") ?: ""
        authManager.registerUser(name, email, currentPass)

        // 2. Simpan data tambahan (HP, Tgl Lahir) di UserProfile
        val sharedPrefProfile = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPrefProfile.edit()
        editor.putString("phone", etPhone.text.toString())
        editor.putString("dob", etDateOfBirth.text.toString())
        editor.putString("gender", actvGender.text.toString())
        editor.apply()
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<MaterialButton>(R.id.btnSave).setOnClickListener {
            saveUserData()
            Toast.makeText(requireContext(), getString(R.string.info_saved), Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}