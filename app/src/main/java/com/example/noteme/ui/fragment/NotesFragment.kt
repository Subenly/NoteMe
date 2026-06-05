package com.example.noteme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.Note
import com.example.noteme.ui.adapter.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupFab(view)
    }

    private fun setupRecyclerView(view: View) {
        val rvAllNotes: RecyclerView = view.findViewById(R.id.rv_all_notes)

        // Data dummy untuk halaman semua catatan
        val noteList = listOf(
            Note(
                "Inspiration",
                R.color.magenta_noteme,
                "Just now",
                "Morning Routine Ideas",
                "Start with 10 minutes of meditation, followed by a quick journaling session."
            ),
            Note(
                "Work",
                R.color.text_secondary, // Diubah dari gray_icon
                "Yesterday",
                "Q3 Design Sync Notes",
                "Focus on improving the tactile feel of the mobile app. Update shadows to be softer."
            ),
            Note(
                "Personal",
                R.color.magenta_noteme,
                "Mon",
                "Grocery List",
                "Almond milk, eggs, spinach, chicken breast, and some fresh strawberries."
            ),
            Note(
                "Study",
                R.color.text_secondary, // Diubah dari gray_icon
                "Sun",
                "Kotlin Coroutines",
                "Need to review how suspend functions work in the background thread."
            )
        )

        // Kita bisa langsung memanggil NoteAdapter yang sama dengan yang di Home!
        rvAllNotes.layoutManager = LinearLayoutManager(requireContext())
        rvAllNotes.adapter = NoteAdapter(noteList)
    }

    private fun setupFab(view: View) {
        val fabAddNote: FloatingActionButton = view.findViewById(R.id.fab_add_note)

        fabAddNote.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateNoteFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}