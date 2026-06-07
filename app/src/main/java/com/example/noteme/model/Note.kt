package com.example.noteme.model

data class Note(
    val category: String,
    val categoryColorResId: Int,
    val time: String,
    val title: String,
    val preview: String,
    val dateNumber: Int = 11,
    val month: Int = 5, // Tambahkan variabel bulan
    val year: Int = 2026  // Tambahkan variabel tahun
)