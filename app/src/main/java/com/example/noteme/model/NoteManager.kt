package com.example.noteme.model

import com.example.noteme.R

object NoteManager {
    // Ini adalah wadah penyimpan daftar catatan Anda secara global
    val noteList = mutableListOf<Note>()

    // Fungsi untuk menambahkan data dummy awal agar layar tidak kosong
    fun addDummyDataIfNeeded() {
        // Hanya tambahkan data dummy jika list masih kosong
        if (noteList.isEmpty()) {
            noteList.add(
                Note(
                    category = "Tugas",
                    categoryColorResId = R.color.magenta_noteme,
                    time = "Yesterday",
                    title = "Proyek Akhir Mobile",
                    preview = "Membuat aplikasi sederhana"
                )
            )
        }
    }
}