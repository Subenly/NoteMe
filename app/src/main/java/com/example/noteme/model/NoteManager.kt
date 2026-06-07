package com.example.noteme.model

import com.example.noteme.R
import java.util.Calendar

object NoteManager {
    val noteList = mutableListOf<Note>()

    fun addDummyDataIfNeeded() {
        if (noteList.isEmpty()) {
            // Ambil bulan dan tahun saat ini secara langsung
            val cal = Calendar.getInstance()
            val currentMonth = cal.get(Calendar.MONTH)
            val currentYear = cal.get(Calendar.YEAR)

            noteList.add(Note("Tugas", R.color.magenta_noteme, "08:00 AM", "Tugas Mobile", "Tugas membuat aplikai sederhana", 8, currentMonth, currentYear))
        }
    }
}