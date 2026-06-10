package com.example.noteme.ui.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteme.R
import com.example.noteme.model.Note
import com.example.noteme.model.NoteManager
import com.example.noteme.utils.AuthManager
import com.google.android.material.button.MaterialButton
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noteme.worker.ReminderWorker
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CreateNoteFragment : Fragment() {

    private lateinit var tvDate: TextView
    private lateinit var tvReminder: TextView
    private lateinit var tvWordCount: TextView
    private lateinit var tvHeaderTitle: TextView
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var etTags: EditText
    private lateinit var btnDelete: ImageView
    private lateinit var authManager: AuthManager
    
    private val calendar = Calendar.getInstance()
    private var existingNoteId: String? = null // Penanda apakah kita sedang EDIT atau CREATE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        
        // 1. Inisialisasi View
        tvDate = view.findViewById(R.id.tv_date)
        tvReminder = view.findViewById(R.id.tv_reminder)
        tvWordCount = view.findViewById(R.id.tv_word_count)
        tvHeaderTitle = view.findViewById(R.id.tvHeaderTitle)
        etTitle = view.findViewById(R.id.et_note_title)
        etContent = view.findViewById(R.id.et_note_content)
        etTags = view.findViewById(R.id.et_note_tags)
        btnDelete = view.findViewById(R.id.btnDelete)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save_entry)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        // 2. Cek Mode (Edit atau Create Baru?)
        existingNoteId = arguments?.getString("note_id")
        if (existingNoteId != null) {
            loadExistingNote(existingNoteId!!)
        } else {
            updateDateText() // Jika baru, set ke tanggal hari ini
        }

        setupWordCounter()

        tvDate.setOnClickListener { showDatePicker() }
        tvReminder.setOnClickListener { showTimePicker() }
        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
        
        // 3. Logika Hapus
        btnDelete.setOnClickListener {
            existingNoteId?.let { id ->
                NoteManager.deleteNote(id, requireContext())
                Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }

        // 4. Logika Simpan (Create atau Update)
        btnSave.setOnClickListener {
            val titleText = etTitle.text.toString().trim()
            val contentText = etContent.text.toString().trim()
            val tagsText = etTags.text.toString().trim()

            if (titleText.isNotEmpty() && contentText.isNotEmpty()) {
                val tagList = if (tagsText.isNotEmpty()) {
                    tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                } else {
                    emptyList()
                }

                val displayCategory = if (tagList.isNotEmpty()) tagList[0].replaceFirstChar { it.uppercase() } else "Personal"

                val note = Note(
                    category = displayCategory,
                    categoryColorResId = R.color.magenta_noteme,
                    time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time),
                    title = titleText,
                    preview = contentText,
                    dateNumber = calendar.get(Calendar.DAY_OF_MONTH),
                    month = calendar.get(Calendar.MONTH),
                    year = calendar.get(Calendar.YEAR),
                    tags = tagList,
                    ownerEmail = authManager.getUserEmail(),
                    id = existingNoteId ?: UUID.randomUUID().toString() // Pakai ID lama jika Edit
                )

                if (existingNoteId != null) {
                    NoteManager.updateNote(note, requireContext())
                } else {
                    NoteManager.noteList.add(0, note)
                    NoteManager.saveNotes(requireContext())
                }

                triggerDemoNotification(note)
                Toast.makeText(requireContext(), "Note Saved!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()

            } else {
                Toast.makeText(requireContext(), "Fill in all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadExistingNote(id: String) {
        val note = NoteManager.noteList.find { it.id == id }
        note?.let {
            tvHeaderTitle.text = "Edit Entry"
            btnDelete.visibility = View.VISIBLE
            etTitle.setText(it.title)
            etContent.setText(it.preview)
            etTags.setText(it.tags.joinToString(", "))
            
            // Set waktu calendar agar sinkron dengan data lama
            calendar.set(it.year, it.month, it.dateNumber)
            updateDateText()
            updateReminderText()
        }
    }

    private fun setupWordCounter() {
        etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString()?.trim()
                tvWordCount.text = if (text.isNullOrEmpty()) "0 words" else "${text.split("\\s+".toRegex()).size} words"
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun triggerDemoNotification(note: Note) {
        val data = Data.Builder()
            .putString("note_title", note.title)
            .putInt("note_day", note.dateNumber)
            .putInt("note_month", note.month)
            .putInt("note_year", note.year)
            .build()

        val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(reminderRequest)
    }

    private fun showDatePicker() {
        DatePickerDialog(requireContext(), { _, y, m, d ->
            calendar.set(Calendar.YEAR, y); calendar.set(Calendar.MONTH, m); calendar.set(Calendar.DAY_OF_MONTH, d)
            updateDateText()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(requireContext(), { _, h, min ->
            calendar.set(Calendar.HOUR_OF_DAY, h); calendar.set(Calendar.MINUTE, min); calendar.set(Calendar.SECOND, 0)
            updateReminderText()
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
    }

    private fun updateDateText() {
        tvDate.text = "📅 ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(calendar.time)}"
    }

    private fun updateReminderText() {
        tvReminder.text = "⏰ ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)}"
    }
}