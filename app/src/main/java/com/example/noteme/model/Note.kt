package com.example.noteme.model

data class Note(
    val category: String,
    val categoryColorResId: Int,
    val time: String,
    val title: String,
    val preview: String,
    val dateNumber: Int = 11,
    val month: Int = 5,
    val year: Int = 2026,
    val tags: List<String> = emptyList(),
    val ownerEmail: String // Tambahkan ini sebagai tanda pengenal pemilik catatan
)