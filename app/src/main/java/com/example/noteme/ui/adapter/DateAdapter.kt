package com.example.noteme.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.DateItem

class DateAdapter(
    private val dateList: List<DateItem>,
    private val onDateClickListener: (DateItem) -> Unit // Fungsi aksi klik
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Pastikan ID ini sesuai dengan elemen di file item_date.xml Anda
        val bgSelector: LinearLayout = itemView.findViewById(R.id.bg_date_selector)
        val tvDayOfWeek: TextView = itemView.findViewById(R.id.tv_day_of_week)
        val tvDateNumber: TextView = itemView.findViewById(R.id.tv_date_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val dateItem = dateList[position]
        holder.tvDayOfWeek.text = dateItem.dayOfWeek
        holder.tvDateNumber.text = dateItem.dateNumber

        // Mengubah warna berdasarkan status apakah tanggal ini dipilih
        if (dateItem.isSelected) {
            holder.bgSelector.setBackgroundResource(R.drawable.bg_circle)
            holder.bgSelector.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#D81E5B")) // Warna Magenta
            holder.tvDayOfWeek.setTextColor(Color.WHITE)
            holder.tvDateNumber.setTextColor(Color.WHITE)
        } else {
            holder.bgSelector.setBackgroundResource(R.drawable.bg_circle)
            holder.bgSelector.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.WHITE) // Warna Putih
            holder.tvDayOfWeek.setTextColor(Color.parseColor("#9E9E9E")) // Abu-abu
            holder.tvDateNumber.setTextColor(Color.parseColor("#212121")) // Hitam
        }

        // Tangkap event klik
        holder.itemView.setOnClickListener {
            onDateClickListener(dateItem)
        }
    }

    override fun getItemCount(): Int = dateList.size
}