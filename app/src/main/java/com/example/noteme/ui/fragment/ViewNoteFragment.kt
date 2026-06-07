package com.example.noteme.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.noteme.R
import com.example.noteme.model.Note
import com.example.noteme.model.NoteManager

class ViewNoteFragment : Fragment() {

    private var noteId: String? = null
    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = arguments?.getString("note_id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId?.let { id ->
            currentNote = NoteManager.noteList.find { it.id == id }
        }

        if (currentNote == null) {
            Toast.makeText(requireContext(), "Note not found", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        displayNote(view)
        setupClickListeners(view)
    }

    private fun displayNote(view: View) {
        val note = currentNote ?: return
        
        view.findViewById<TextView>(R.id.tvViewTitle).text = note.title
        view.findViewById<TextView>(R.id.tvViewContent).text = note.preview
        view.findViewById<TextView>(R.id.tvViewDate).text = "📅 ${note.time}, ${note.dateNumber}/${note.month + 1}/${note.year}"
        
        val tagsText = if (note.tags.isNotEmpty()) {
            note.tags.joinToString(", ") { "#$it" }
        } else {
            "#NoTags"
        }
        view.findViewById<TextView>(R.id.tvViewTags).text = tagsText
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<View>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<View>(R.id.btnEdit).setOnClickListener {
            // Pindah ke mode Edit (CreateNoteFragment dengan note_id)
            val fragment = CreateNoteFragment().apply {
                arguments = Bundle().apply {
                    putString("note_id", noteId)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<View>(R.id.btnDelete).setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                noteId?.let { id ->
                    NoteManager.deleteNote(id, requireContext())
                    Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
