package com.example.noteme.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteme.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateNoteFragment : Fragment() {

    private lateinit var tvDate: TextView
    private lateinit var tvReminder: TextView
    private val calendar = Calendar.getInstance() // Mengambil waktu saat ini

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi View
        tvDate = view.findViewById(R.id.tv_date)
        tvReminder = view.findViewById(R.id.tv_reminder)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save_entry)

        // Atur teks tanggal bawaan (hari ini)
        updateDateText()

        // Aksi klik untuk memilih Tanggal
        tvDate.setOnClickListener {
            showDatePicker()
        }

        // Aksi klik untuk memilih Waktu Reminder
        tvReminder.setOnClickListener {
            showTimePicker()
        }

        // Aksi Simpan Catatan
        btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Catatan berhasil disimpan!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack() // Kembali ke halaman daftar catatan
        }
    }

    // Fungsi untuk memunculkan popup Kalender
    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Jika user memilih tanggal, simpan ke variabel calendar kita
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay)
                updateDateText() // Perbarui tulisan di layar
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // Fungsi untuk memunculkan popup Jam
    private fun showTimePicker() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Jika user memilih jam, simpan ke variabel calendar kita
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                updateReminderText() // Perbarui tulisan di layar
            },
            hour, minute, false // 'false' berarti kita pakai format 12 jam (AM/PM)
        )
        timePickerDialog.show()
    }

    // Memperbarui tulisan TextView Tanggal
    private fun updateDateText() {
        val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        tvDate.text = "📅 ${format.format(calendar.time)}"
    }

    // Memperbarui tulisan TextView Jam/Reminder
    private fun updateReminderText() {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        tvReminder.text = "⏰ ${format.format(calendar.time)}"

        // Opsional: Anda bisa memberikan warna magenta agar terlihat tombol telah diaktifkan
        // tvReminder.setTextColor(ContextCompat.getColor(requireContext(), R.color.magenta_noteme))
    }
}