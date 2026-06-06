package com.example.noteme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.example.noteme.utils.AuthManager
import java.util.Calendar
import com.example.noteme.model.NoteManager

class HomeFragment : Fragment() {

    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        authManager = AuthManager(requireContext())

        setupGreeting(view)
        setupDateRecyclerView(view)
        setupReminderRecyclerView(view)
        setupNoteRecyclerView(view)
    }

    private fun setupGreeting(view: View) {
        val tvGreeting: TextView = view.findViewById(R.id.tv_greeting)
        val userName = authManager.getUserName()
        
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        
        val greetingRes = when (hour) {
            in 0..11 -> R.string.good_morning
            in 12..16 -> R.string.good_afternoon
            else -> R.string.good_evening
        }
        
        tvGreeting.text = getString(greetingRes, userName)
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
            Reminder("Water Plants", "Monstera and succulents need...", "10:00 AM", R.drawable.ic_home),
            Reminder("Review Draft", "Read through chapter 4", "02:00 PM", R.drawable.ic_notes)
        )

        // Memasang Adapter dan Layout Manager Horizontal
        rvReminders.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvReminders.adapter = ReminderAdapter(reminderList)
    }

    private fun setupNoteRecyclerView(view: View) {
        // UBAH ID DI SINI MENJADI rv_recent_notes
        val rvRecentNotes: RecyclerView = view.findViewById(R.id.rv_recent_notes)

        // Pastikan ada data dummy jika aplikasi baru pertama kali dibuka
        NoteManager.addDummyDataIfNeeded()

        // Ambil data langsung dari NoteManager (yang sudah ditambah catatan baru)
        val adapter = NoteAdapter(NoteManager.noteList)

        rvRecentNotes.layoutManager = LinearLayoutManager(requireContext())
        rvRecentNotes.adapter = adapter
    }
}