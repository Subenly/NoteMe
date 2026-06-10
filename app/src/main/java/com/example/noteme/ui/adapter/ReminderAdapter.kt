package com.example.noteme.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.Reminder

class ReminderAdapter(
    private val reminderList: List<Reminder>,
    private val onReminderClickListener: (Reminder) -> Unit // Tambahkan listener
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.img_reminder_icon)
        val tvTime: TextView = itemView.findViewById(R.id.tv_reminder_time)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_reminder_title)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_reminder_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminderList[position]
        holder.imgIcon.setImageResource(reminder.iconResId)
        holder.tvTime.text = reminder.time
        holder.tvTitle.text = reminder.title
        holder.tvDesc.text = reminder.description

        holder.itemView.setOnClickListener {
            onReminderClickListener(reminder)
        }
    }

    override fun getItemCount(): Int = reminderList.size
}