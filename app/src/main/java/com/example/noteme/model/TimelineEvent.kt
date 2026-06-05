package com.example.noteme.model

data class TimelineEvent(
    val time: String,       // Contoh: "08:00\nAM"
    val title: String,      // Contoh: "Morning Meditation"
    val description: String,
    val tag: String         // Contoh: "MINDFULNESS"
)