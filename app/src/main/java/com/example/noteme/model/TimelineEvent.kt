package com.example.noteme.model

data class TimelineEvent(
    val time: String,       // Contoh: "08:00 AM"
    val title: String,      // Contoh: "Morning Meditation"
    val description: String,
    val tag: String,        // Contoh: "PERSONAL"
    val noteId: String      // Tambahkan ID untuk menghubungkan ke data Note asli
)