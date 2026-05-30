package com.example.noteme.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.Note

class NoteAdapter(private val noteList: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dotCategory: View = itemView.findViewById(R.id.dot_category)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_note_category)
        val tvTime: TextView = itemView.findViewById(R.id.tv_note_time)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_note_title)
        val tvPreview: TextView = itemView.findViewById(R.id.tv_note_preview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]

        holder.tvCategory.text = note.category
        holder.tvTime.text = note.time
        holder.tvTitle.text = note.title
        holder.tvPreview.text = note.preview

        // Mengubah warna teks kategori dan warna titik/dot sesuai data
        val color = ContextCompat.getColor(holder.itemView.context, note.categoryColorResId)
        holder.tvCategory.setTextColor(color)
        holder.dotCategory.backgroundTintList = ColorStateList.valueOf(color)
    }

    override fun getItemCount(): Int = noteList.size
}