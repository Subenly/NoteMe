package com.example.noteme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.ui.adapter.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.noteme.model.NoteManager
import com.example.noteme.utils.AuthManager

class NotesFragment : Fragment() {

    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        authManager = AuthManager(requireContext())
        setupRecyclerView(view)
        setupFab(view)
    }

    private fun setupRecyclerView(view: View) {
        val rvAllNotes: RecyclerView = view.findViewById(R.id.rv_all_notes)
        val userEmail = authManager.getUserEmail()

        // 1. FILTER: Ambil hanya catatan yang ownerEmail-nya cocok
        val userNotes = NoteManager.noteList.filter { it.ownerEmail == userEmail }

        rvAllNotes.layoutManager = LinearLayoutManager(requireContext())
        rvAllNotes.adapter = NoteAdapter(userNotes)
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

    override fun onResume() {
        super.onResume()
        // Refresh daftar saat kembali dari halaman tambah catatan
        setupRecyclerView(requireView())
    }
}