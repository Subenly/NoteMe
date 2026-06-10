package com.example.noteme.model

import java.util.UUID

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
    val ownerEmail: String,
    val id: String = UUID.randomUUID().toString() // Tambahkan ID unik untuk mempermudah edit & delete
)