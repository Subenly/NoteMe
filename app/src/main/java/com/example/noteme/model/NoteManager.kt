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

    fun saveNotes(context: Context) {
        try {
            val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val jsonArray = JSONArray()
            
            noteList.forEach { note ->
                val jsonObject = JSONObject()
                jsonObject.put("id", note.id)
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
                        category = obj.getString("cat"),
                        categoryColorResId = obj.getInt("col"),
                        time = obj.getString("time"),
                        title = obj.getString("title"),
                        preview = obj.getString("prev"),
                        dateNumber = obj.getInt("date"),
                        month = obj.getInt("month"),
                        year = obj.getInt("year"),
                        tags = tagsList,
                        ownerEmail = obj.getString("owner"),
                        id = obj.optString("id", java.util.UUID.randomUUID().toString())
                    ))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateNote(updatedNote: Note, context: Context) {
        val index = noteList.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            noteList[index] = updatedNote
            saveNotes(context)
        }
    }

    fun deleteNote(noteId: String, context: Context) {
        noteList.removeAll { it.id == noteId }
        saveNotes(context)
    }

    fun clearInMemoryNotes() {
        noteList.clear()
    }
}