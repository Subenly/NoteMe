package com.example.noteme.model

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {
    // Membuat file penyimpanan permanen bernama "NoteMe_Prefs" di memori HP
    private val prefs: SharedPreferences = context.getSharedPreferences("NoteMe_Prefs", Context.MODE_PRIVATE)

    // Variabel pintar untuk menyimpan dan memanggil nama
    var userName: String
        get() = prefs.getString("KEY_USER_NAME", "User") ?: "User" // "Sarah" adalah default jika belum diset
        set(value) = prefs.edit().putString("KEY_USER_NAME", value).apply() // apply() menyimpannya di background
}