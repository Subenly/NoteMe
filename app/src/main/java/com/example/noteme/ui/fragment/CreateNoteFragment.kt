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
import com.example.noteme.model.NoteManager
import com.example.noteme.model.Note
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noteme.worker.ReminderWorker

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

        // Inisialisasi View dari XML Anda
        tvDate = view.findViewById(R.id.tv_date)
        tvReminder = view.findViewById(R.id.tv_reminder)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save_entry)

        // Panggil EditText untuk Judul dan Isi Catatan
        val etTitle = view.findViewById<android.widget.EditText>(R.id.et_note_title)
        val etContent = view.findViewById<android.widget.EditText>(R.id.et_note_content)

        updateDateText()

        tvDate.setOnClickListener { showDatePicker() }
        tvReminder.setOnClickListener { showTimePicker() }

        btnSave.setOnClickListener {
            val titleText = etTitle.text.toString().trim()
            val contentText = etContent.text.toString().trim()

            if (titleText.isNotEmpty() && contentText.isNotEmpty()) {
                val selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                val selectedMonth = calendar.get(Calendar.MONTH)
                val selectedYear = calendar.get(Calendar.YEAR)

                val newNote = Note(
                    category = "Personal",
                    categoryColorResId = R.color.magenta_noteme,
                    time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time),
                    title = titleText,
                    preview = contentText,
                    dateNumber = selectedDayOfMonth,
                    month = selectedMonth,
                    year = selectedYear
                )

                NoteManager.noteList.add(0, newNote)

                val checkNowRequest = OneTimeWorkRequestBuilder<ReminderWorker>().build()
                WorkManager.getInstance(requireContext()).enqueue(checkNowRequest)

                Toast.makeText(requireContext(), "Catatan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()

            } else {
                Toast.makeText(requireContext(), "Judul dan isi tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
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
    }
}