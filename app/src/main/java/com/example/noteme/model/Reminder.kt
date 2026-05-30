package com.example.noteme.model

data class Reminder(
    val title: String,
    val description: String,
    val time: String,
    val iconResId: Int // Untuk menyimpan gambar ikon (misal: R.drawable.ic_water)
)