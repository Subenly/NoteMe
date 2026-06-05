package com.example.noteme.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.TimelineEvent

class TimelineAdapter(private val eventList: List<TimelineEvent>) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tv_event_time)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_event_title)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_event_desc)
        val tvTag: TextView = itemView.findViewById(R.id.tv_event_tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val event = eventList[position]
        holder.tvTime.text = event.time
        holder.tvTitle.text = event.title
        holder.tvDesc.text = event.description
        holder.tvTag.text = event.tag
    }

    override fun getItemCount(): Int = eventList.size
}