package com.example.noteme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.CalendarDate
import com.example.noteme.model.TimelineEvent
import com.example.noteme.ui.adapter.CalendarDateAdapter
import com.example.noteme.ui.adapter.TimelineAdapter

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendarGrid(view)
        setupTimeline(view)
    }

    private fun setupCalendarGrid(view: View) {
        val rvCalendarGrid: RecyclerView = view.findViewById(R.id.rv_calendar_grid)

        // Membuat dummy data untuk 35 kotak (5 minggu x 7 hari)
        // Sebagian kosong (sebelum tanggal 1), sebagian berisi tanggal
        val dateList = mutableListOf<CalendarDate>()

        // Contoh tanggal kosong di awal bulan (misal bulan mulai di hari Selasa)
        dateList.add(CalendarDate("29"))
        dateList.add(CalendarDate("30"))

        // Mengisi tanggal 1 sampai 31
        for (i in 1..31) {
            val isSelectedDate = (i == 11) // Anggap hari ini tanggal 11 (Aktif warna magenta)
            val hasEventDot = (i == 2 || i == 8 || i == 15 || i == 18) // Tanggal-tanggal yang ada titik event

            dateList.add(CalendarDate(i.toString(), isSelectedDate, hasEventDot))
        }

        // Layout manager menggunakan Grid 7 kolom
        rvCalendarGrid.layoutManager = GridLayoutManager(requireContext(), 7)
        rvCalendarGrid.adapter = CalendarDateAdapter(dateList)
    }

    private fun setupTimeline(view: View) {
        val rvTimeline: RecyclerView = view.findViewById(R.id.rv_timeline)

        // Data dummy untuk event hari ini
        val eventList = listOf(
            TimelineEvent(
                "08:00\nAM",
                "Morning Meditation & Journal",
                "Felt particularly grounded today. Focused on gratitude for the small things.",
                "MINDFULNESS"
            ),
            TimelineEvent(
                "11:30\nAM",
                "Strategy Session with Creative Team",
                "Discussed the upcoming Q4 goals and visual direction. Key takeaway: simplicity is luxury.",
                "WORK  PRIORITY" // Anda bisa memisahkan tag jika diperlukan desain lanjutan
            ),
            TimelineEvent(
                "05:00\nPM",
                "Grocery List & Meal Prep",
                "Need to pick up fresh basil, balsamic glaze, and heirloom tomatoes for tonight's salad.",
                "PERSONAL"
            )
        )

        // Menggunakan LayoutManager Vertikal biasa
        rvTimeline.layoutManager = LinearLayoutManager(requireContext())
        rvTimeline.adapter = TimelineAdapter(eventList)
    }
}