package com.example.noteme.model

data class CalendarDate(
    val dateNumber: String,
    var isSelected: Boolean = false, // Menandai apakah tanggal sedang diklik
    val hasEvent: Boolean = false    // Menandai apakah ada titik event di bawah tanggal
)