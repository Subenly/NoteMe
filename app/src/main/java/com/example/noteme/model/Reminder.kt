package com.example.noteme.model

data class Reminder(
    val iconResId: Int,
    val time: String,
    val title: String,
    val description: String,
    val noteId: String // Menghubungkan reminder ke catatan aslinya
)