package com.example.noteme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.DateItem
import com.example.noteme.model.Note
import com.example.noteme.model.Reminder
import com.example.noteme.ui.adapter.DateAdapter
import com.example.noteme.ui.adapter.NoteAdapter
import com.example.noteme.ui.adapter.ReminderAdapter

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDateRecyclerView(view)
        setupReminderRecyclerView(view)
        setupNoteRecyclerView(view)
    }

    private fun setupDateRecyclerView(view: View) {
        val rvDates: RecyclerView = view.findViewById(R.id.rv_dates)

        // Membuat Dummy Data untuk Kalender
        val dateList = listOf(
            DateItem("MON", "12", false),
            DateItem("TUE", "13", true), // Tanggal yang sedang aktif (warna magenta)
            DateItem("WED", "14", false),
            DateItem("THU", "15", false),
            DateItem("FRI", "16", false)
        )

        // Memasang Adapter dan Layout Manager Horizontal
        rvDates.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvDates.adapter = DateAdapter(dateList)
    }

    private fun setupReminderRecyclerView(view: View) {
        val rvReminders: RecyclerView = view.findViewById(R.id.rv_reminders)

        // Membuat Dummy Data untuk Pengingat
        val reminderList = listOf(
            Reminder("Water Plants", "Monstera and succulents need...", "10:00 AM", R.drawable.ic_home), // Ganti dengan ikon air jika ada
            Reminder("Review Draft", "Read through chapter 4", "02:00 PM", R.drawable.ic_notes) // Ganti dengan ikon dokumen jika ada
        )

        // Memasang Adapter dan Layout Manager Horizontal
        rvReminders.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvReminders.adapter = ReminderAdapter(reminderList)
    }

    private fun setupNoteRecyclerView(view: View) {
        val rvNotes: RecyclerView = view.findViewById(R.id.rv_recent_notes)

        // Membuat Dummy Data untuk Catatan
        val noteList = listOf(
            Note(
                "Inspiration",
                R.color.magenta_noteme, // Pastikan warna ini ada di colors.xml
                "Just now",
                "Morning Routine Ideas",
                "Start with 10 minutes of meditation, followed by a quick journaling session. Don't check phone until after"
            ),
            Note(
                "Work",
                R.color.gray_icon, // Menggunakan warna abu-abu sebagai pembeda
                "Yesterday",
                "Q3 Design Sync Notes",
                "Focus on improving the tactile feel of the mobile app. Update shadows to be softer and more tinted. Review..."
            )
        )

        // Memasang Adapter dan Layout Manager Vertikal
        rvNotes.layoutManager = LinearLayoutManager(requireContext())
        rvNotes.adapter = NoteAdapter(noteList)
    }
}