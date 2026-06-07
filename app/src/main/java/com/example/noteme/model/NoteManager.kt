package com.example.noteme.model

import android.content.Context
import com.example.noteme.R
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

object NoteManager {
    val noteList = mutableListOf<Note>()
    private const val PREFS_NAME = "NoteMePrefs"
    private const val KEY_NOTES = "saved_notes"

    // Fungsi untuk menambah data dummy hanya untuk akun demo
    fun addDummyDataIfNeeded(ownerEmail: String) {
        val isDemoUser = ownerEmail == "user@noteme.app"
        val hasDemoNote = noteList.any { it.ownerEmail == "user@noteme.app" }
        
        if (isDemoUser && !hasDemoNote) {
            val cal = Calendar.getInstance()
            noteList.add(
                Note(
                    "Tugas", R.color.magenta_noteme, "08:00 AM", "Tugas Mobile",
                    "Tugas membuat aplikasi sederhana", 8, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),
                    listOf("Kuliah", "Android"), "user@noteme.app"
                )
            )
        }
    }

    // SIMPAN: Simpan semua catatan ke memori HP (Permanen)
    fun saveNotes(context: Context) {
        try {
            val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val jsonArray = JSONArray()
            
            noteList.forEach { note ->
                val jsonObject = JSONObject()
                jsonObject.put("cat", note.category)
                jsonObject.put("col", note.categoryColorResId)
                jsonObject.put("time", note.time)
                jsonObject.put("title", note.title)
                jsonObject.put("prev", note.preview)
                jsonObject.put("date", note.dateNumber)
                jsonObject.put("month", note.month)
                jsonObject.put("year", note.year)
                jsonObject.put("tags", JSONArray(note.tags))
                jsonObject.put("owner", note.ownerEmail)
                jsonArray.put(jsonObject)
            }
            
            sharedPref.edit().putString(KEY_NOTES, jsonArray.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // MUAT: Ambil data dari memori HP saat aplikasi dibuka
    fun loadNotes(context: Context) {
        try {
            val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val jsonString = sharedPref.getString(KEY_NOTES, null)
            
            if (jsonString != null) {
                noteList.clear()
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val tagsJson = obj.getJSONArray("tags")
                    val tagsList = mutableListOf<String>()
                    for (j in 0 until tagsJson.length()) tagsList.add(tagsJson.getString(j))

                    noteList.add(Note(
                        obj.getString("cat"),
                        obj.getInt("col"),
                        obj.getString("time"),
                        obj.getString("title"),
                        obj.getString("prev"),
                        obj.getInt("date"),
                        obj.getInt("month"),
                        obj.getInt("year"),
                        tagsList,
                        obj.getString("owner")
                    ))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Hanya bersihkan memori RAM saat logout (Data di HP tetap aman)
    fun clearInMemoryNotes() {
        noteList.clear()
    }
}