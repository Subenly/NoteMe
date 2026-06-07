package com.example.noteme.model

data class DateItem(
    val dayOfWeek: String,
    val dateNumber: String,
    var isSelected: Boolean = false
)