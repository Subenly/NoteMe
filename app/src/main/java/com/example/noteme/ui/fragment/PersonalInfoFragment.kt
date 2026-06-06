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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PersonalInfoFragment : Fragment() {

    private lateinit var etDisplayName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etDateOfBirth: TextInputEditText
    private lateinit var actvGender: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inisialisasi semua View
        etDisplayName = view.findViewById(R.id.etDisplayName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhone = view.findViewById(R.id.etPhone)
        etDateOfBirth = view.findViewById(R.id.etDateOfBirth)
        actvGender = view.findViewById(R.id.actvGender)

        setupGenderDropdown()
        setupDatePicker()
        setupClickListeners(view)
        
        // 2. Muat data yang sudah tersimpan sebelumnya (jika ada)
        loadUserData()
    }

    private fun setupGenderDropdown() {
        val genders = listOf(
            getString(R.string.gender_male),
            getString(R.string.gender_female),
            getString(R.string.gender_other)
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genders)
        actvGender.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        etDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val date = "$day/${month + 1}/$year"
                    etDateOfBirth.setText(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    private fun loadUserData() {
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        
        etDisplayName.setText(sharedPref.getString("name", getString(R.string.user_name)))
        etEmail.setText(sharedPref.getString("email", getString(R.string.user_email)))
        etPhone.setText(sharedPref.getString("phone", ""))
        etDateOfBirth.setText(sharedPref.getString("dob", ""))
        
        val savedGender = sharedPref.getString("gender", "")
        if (!savedGender.isNullOrEmpty()) {
            actvGender.setText(savedGender, false)
        }
    }

    private fun saveUserData() {
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        
        editor.putString("name", etDisplayName.text.toString())
        editor.putString("email", etEmail.text.toString())
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