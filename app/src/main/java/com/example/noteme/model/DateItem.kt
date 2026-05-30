package com.example.noteme.model

data class DateItem(
    val dayName: String,
    val dateNumber: String,
    var isActive: Boolean = false // Untuk menandai tanggal yang sedang dipilih (warna magenta)
)