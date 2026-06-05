package com.example.noteme.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.CalendarDate

class CalendarDateAdapter(private val dateList: List<CalendarDate>) : RecyclerView.Adapter<CalendarDateAdapter.DateViewHolder>() {

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bgSelector: LinearLayout = itemView.findViewById(R.id.bg_date_selector)
        val tvDateNumber: TextView = itemView.findViewById(R.id.tv_date_number)
        val dotIndicator: View = itemView.findViewById(R.id.dot_event_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val dateItem = dateList[position]
        holder.tvDateNumber.text = dateItem.dateNumber

        // Tampilkan titik jika ada event
        holder.dotIndicator.visibility = if (dateItem.hasEvent) View.VISIBLE else View.INVISIBLE

        // Ubah warna jika tanggal dipilih (aktif)
        if (dateItem.isSelected) {
            // Gunakan warna magenta NoteMe
            holder.bgSelector.setBackgroundColor(Color.parseColor("#D81E5B"))
            holder.tvDateNumber.setTextColor(Color.WHITE)
        } else {
            // Transparan/putih jika tidak dipilih
            holder.bgSelector.setBackgroundColor(Color.TRANSPARENT)
            holder.tvDateNumber.setTextColor(Color.parseColor("#212121"))
        }
    }

    override fun getItemCount(): Int = dateList.size
}