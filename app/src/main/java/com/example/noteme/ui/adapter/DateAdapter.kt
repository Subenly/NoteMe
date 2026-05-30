package com.example.noteme.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.DateItem
import com.google.android.material.card.MaterialCardView

class DateAdapter(private val dateList: List<DateItem>) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView as MaterialCardView // Root layout adalah MaterialCardView
        val tvDayName: TextView = itemView.findViewById(R.id.tv_day_name)
        val tvDateNumber: TextView = itemView.findViewById(R.id.tv_date_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val dateItem = dateList[position]
        holder.tvDayName.text = dateItem.dayName
        holder.tvDateNumber.text = dateItem.dateNumber

        // Mengatur tampilan saat tanggal dipilih (Active) atau tidak
        if (dateItem.isActive) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#D81E5B")) // Magenta NoteMe
            holder.tvDayName.setTextColor(Color.WHITE)
            holder.tvDateNumber.setTextColor(Color.WHITE)
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
            holder.tvDayName.setTextColor(Color.parseColor("#757575")) // Abu-abu
            holder.tvDateNumber.setTextColor(Color.parseColor("#212121")) // Hitam
        }
    }

    override fun getItemCount(): Int = dateList.size
}