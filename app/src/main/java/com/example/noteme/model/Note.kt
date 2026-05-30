package com.example.noteme.model

data class Note(
    val category: String,
    val categoryColorResId: Int, // Untuk warna titik kategori
    val time: String,
    val title: String,
    val preview: String
)