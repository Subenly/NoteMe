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
import com.example.noteme.model.Note
import com.example.noteme.model.NoteManager
import com.example.noteme.model.TimelineEvent
import com.example.noteme.ui.adapter.CalendarDateAdapter
import com.example.noteme.ui.adapter.TimelineAdapter
import com.example.noteme.utils.AuthManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private lateinit var rvCalendarGrid: RecyclerView
    private lateinit var rvTimeline: RecyclerView
    private lateinit var tvTimelineDate: TextView
    private lateinit var tvTimelineTitle: TextView
    private lateinit var tvNoTimeline: TextView
    private lateinit var tvMonthYear: TextView
    private lateinit var btnPrevMonth: TextView
    private lateinit var btnNextMonth: TextView
    private lateinit var authManager: AuthManager

    private val dateList = mutableListOf<CalendarDate>()
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

        authManager = AuthManager(requireContext())
        
        rvCalendarGrid = view.findViewById(R.id.rv_calendar_grid)
        rvTimeline = view.findViewById(R.id.rv_timeline)
        tvTimelineDate = view.findViewById(R.id.tv_timeline_date)
        tvTimelineTitle = view.findViewById(R.id.tv_timeline_title)
        tvNoTimeline = view.findViewById(R.id.tv_no_timeline)
        tvMonthYear = view.findViewById(R.id.tv_month_year)
        btnPrevMonth = view.findViewById(R.id.btn_prev_month)
        btnNextMonth = view.findViewById(R.id.btn_next_month)

        NoteManager.addDummyDataIfNeeded(authManager.getUserEmail())

        updateCalendarUi()

        btnPrevMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            selectedDateNumber = 1
            updateCalendarUi()
        }

        btnNextMonth.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            selectedDateNumber = 1
            updateCalendarUi()
        }
    }

    private fun updateCalendarUi() {
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonthYear.text = monthYearFormat.format(currentCalendar.time)

        setupCalendarGrid()
        updateTimelineForDate(selectedDateNumber)
    }

    private fun setupCalendarGrid() {
        dateList.clear()
        val userEmail = authManager.getUserEmail()

        val monthCalendar = currentCalendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK)

        val emptySlots = firstDayOfWeek - 1
        for (i in 0 until emptySlots) {
            dateList.add(CalendarDate(""))
        }

        val maxDaysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val userNotes = NoteManager.noteList.filter { it.ownerEmail == userEmail }

        for (i in 1..maxDaysInMonth) {
            val isSelectedDate = (i == selectedDateNumber)
            val viewMonth = currentCalendar.get(Calendar.MONTH)
            val viewYear = currentCalendar.get(Calendar.YEAR)

            val hasEventDot = userNotes.any {
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
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val monthString = monthFormat.format(currentCalendar.time).uppercase()
        tvTimelineDate.text = "$monthString $dateNumber"

        val today = Calendar.getInstance()
        val isToday = dateNumber == today.get(Calendar.DAY_OF_MONTH) &&
                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)

        if (isToday) {
            tvTimelineTitle.text = "Timeline for Today"
        } else {
            val fullDateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val tempCal = currentCalendar.clone() as Calendar
            tempCal.set(Calendar.DAY_OF_MONTH, dateNumber)
            tvTimelineTitle.text = "Timeline for ${fullDateFormat.format(tempCal.time)}"
        }

        val userEmail = authManager.getUserEmail()
        val viewMonth = currentCalendar.get(Calendar.MONTH)
        val viewYear = currentCalendar.get(Calendar.YEAR)

        val filteredNotes = NoteManager.noteList.filter {
            it.ownerEmail == userEmail && it.dateNumber == dateNumber && it.month == viewMonth && it.year == viewYear
        }

        if (filteredNotes.isEmpty()) {
            rvTimeline.visibility = View.GONE
            tvNoTimeline.visibility = View.VISIBLE
        } else {
            rvTimeline.visibility = View.VISIBLE
            tvNoTimeline.visibility = View.GONE

            val eventList = filteredNotes.map { note ->
                TimelineEvent(
                    time = note.time,
                    title = note.title,
                    description = note.preview,
                    tag = note.category.uppercase(),
                    noteId = note.id
                )
            }

            rvTimeline.layoutManager = LinearLayoutManager(requireContext())
            rvTimeline.adapter = TimelineAdapter(eventList) { clickedEvent ->
                val note = NoteManager.noteList.find { it.id == clickedEvent.noteId }
                note?.let { openNoteView(it) }
            }
        }
    }

    private fun openNoteView(note: Note) {
        // SEKARANG MEMBUKA ViewNoteFragment
        val fragment = ViewNoteFragment().apply {
            arguments = Bundle().apply {
                putString("note_id", note.id)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
