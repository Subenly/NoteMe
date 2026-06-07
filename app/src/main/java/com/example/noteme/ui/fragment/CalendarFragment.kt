package com.example.noteme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.CalendarDate
import com.example.noteme.model.NoteManager
import com.example.noteme.model.TimelineEvent
import com.example.noteme.ui.adapter.CalendarDateAdapter
import com.example.noteme.ui.adapter.TimelineAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private lateinit var rvCalendarGrid: RecyclerView
    private lateinit var rvTimeline: RecyclerView
    private lateinit var tvTimelineDate: TextView
    private lateinit var tvMonthYear: TextView
    private lateinit var btnPrevMonth: TextView
    private lateinit var btnNextMonth: TextView

    private val dateList = mutableListOf<CalendarDate>()

    // Mengambil sistem waktu saat ini secara LIVE dari HP
    private val currentCalendar = Calendar.getInstance()
    private var selectedDateNumber: Int = currentCalendar.get(Calendar.DAY_OF_MONTH)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi komponen tampilan
        rvCalendarGrid = view.findViewById(R.id.rv_calendar_grid)
        rvTimeline = view.findViewById(R.id.rv_timeline)
        tvTimelineDate = view.findViewById(R.id.tv_timeline_date)
        tvMonthYear = view.findViewById(R.id.tv_month_year)
        btnPrevMonth = view.findViewById(R.id.btn_prev_month)
        btnNextMonth = view.findViewById(R.id.btn_next_month)

        NoteManager.addDummyDataIfNeeded()

        // Tampilkan kalender awal berdasarkan bulan berjalan
        updateCalendarUi()

        // Tombol Panah Kiri (<) untuk kembali ke bulan sebelumnya
        btnPrevMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            // Reset tanggal terpilih ke tanggal 1 setiap pindah bulan agar aman
            selectedDateNumber = 1
            updateCalendarUi()
        }

        // Tombol Panah Kanan (>) untuk maju ke bulan berikutnya
        btnNextMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            selectedDateNumber = 1
            updateCalendarUi()
        }
    }

    // Fungsi pusat untuk memperbarui Teks Judul, Grid Kotak Angka, dan Timeline
    private fun updateCalendarUi() {
        // 1. Perbarui teks bulan dan tahun di atas kartu (Contoh: "Mei 2026")
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonthYear.text = monthYearFormat.format(currentCalendar.time)

        // 2. Gambar ulang kotak grid tanggal kalendernya
        setupCalendarGrid()

        // 3. Sinkronkan ulang timeline catatan di bawahnya
        updateTimelineForDate(selectedDateNumber)
    }

    private fun setupCalendarGrid() {
        dateList.clear()

        // Buat tiruan kalender khusus untuk mendeteksi awal hari pada bulan yang aktif
        val monthCalendar = currentCalendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK)

        // Selipkan kotak kosong transparan agar posisi hari pas
        val emptySlots = firstDayOfWeek - 1
        for (i in 0 until emptySlots) {
            dateList.add(CalendarDate(""))
        }

        // Mendapatkan batas maksimum hari pada bulan tersebut (misal Februari bisa 28/29)
        val maxDaysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Isi angka tanggal dari 1 sampai batas akhir bulan
        for (i in 1..maxDaysInMonth) {
            val isSelectedDate = (i == selectedDateNumber)

            // Cek titik merah: Tanggal, Bulan, dan Tahun HARUS COCOK
            val viewMonth = currentCalendar.get(Calendar.MONTH)
            val viewYear = currentCalendar.get(Calendar.YEAR)

            val hasEventDot = NoteManager.noteList.any {
                it.dateNumber == i && it.month == viewMonth && it.year == viewYear
            }

            dateList.add(CalendarDate(i.toString(), isSelectedDate, hasEventDot))
        }

        rvCalendarGrid.layoutManager = GridLayoutManager(requireContext(), 7)

        rvCalendarGrid.adapter = CalendarDateAdapter(dateList) { clickedDate ->
            val dateInt = clickedDate.dateNumber.toIntOrNull()
            if (dateInt != null) {
                dateList.forEach { it.isSelected = (it.dateNumber == clickedDate.dateNumber) }
                rvCalendarGrid.adapter?.notifyDataSetChanged()

                selectedDateNumber = dateInt
                updateTimelineForDate(dateInt)
            }
        }
    }

    private fun updateTimelineForDate(dateNumber: Int) {
        // Indikator teks kanan bawah timeline otomatis mengikuti singkatan bulan (Contoh: "MEI 13")
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val monthString = monthFormat.format(currentCalendar.time).uppercase()
        tvTimelineDate.text = "$monthString $dateNumber"

        // Filter Timeline: Tanggal, Bulan, dan Tahun HARUS COCOK
        val viewMonth = currentCalendar.get(Calendar.MONTH)
        val viewYear = currentCalendar.get(Calendar.YEAR)

        val filteredNotes = NoteManager.noteList.filter {
            it.dateNumber == dateNumber && it.month == viewMonth && it.year == viewYear
        }

        val eventList = filteredNotes.map { note ->
            TimelineEvent(
                time = note.time,
                title = note.title,
                description = note.preview,
                tag = note.category.uppercase()
            )
        }

        rvTimeline.layoutManager = LinearLayoutManager(requireContext())
        rvTimeline.adapter = TimelineAdapter(eventList)
    }
}