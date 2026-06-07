package com.example.noteme.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R
import com.example.noteme.model.DateItem
import com.example.noteme.model.NoteManager
import com.example.noteme.model.Reminder
import com.example.noteme.ui.adapter.DateAdapter
import com.example.noteme.ui.adapter.NoteAdapter
import com.example.noteme.ui.adapter.ReminderAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.noteme.utils.AuthManager

class HomeFragment : Fragment() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvRemindersTitle: TextView
    private lateinit var tvNoReminders: TextView
    private lateinit var rvReminders: RecyclerView
    private lateinit var authManager: AuthManager
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        tvGreeting = view.findViewById(R.id.tv_greeting)
        tvRemindersTitle = view.findViewById(R.id.tv_reminders_title)
        tvNoReminders = view.findViewById(R.id.tv_no_reminders)
        rvReminders = view.findViewById(R.id.rv_reminders)

        // Muat data dummy hanya jika usernya adalah "User" demo
        NoteManager.addDummyDataIfNeeded(authManager.getUserEmail())

        setupLiveDateHeader(view)
        setupHorizontalCalendar(view)
        
        updateRemindersUI()
        setupRecentNotes(view)
    }

    private fun setupLiveDateHeader(view: View) {
        val tvMonthYear = view.findViewById<TextView>(R.id.tv_month_year)
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonthYear.text = monthYearFormat.format(Calendar.getInstance().time)
    }

    private fun setupHorizontalCalendar(view: View) {
        val rvDates = view.findViewById<RecyclerView>(R.id.rv_dates)
        val dateList = mutableListOf<DateItem>()
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var todayIndex = 0

        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val tempCalendar = Calendar.getInstance()
        
        for (i in 1..maxDays) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, i)
            val dayString = dayFormat.format(tempCalendar.time).uppercase()
            if (i == today) todayIndex = i - 1
            dateList.add(DateItem(dayString, i.toString(), i == today))
        }

        rvDates.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvDates.adapter = DateAdapter(dateList) { clickedDate ->
            dateList.forEach { it.isSelected = (it.dateNumber == clickedDate.dateNumber) }
            rvDates.adapter?.notifyDataSetChanged()
            selectedCalendar.set(Calendar.DAY_OF_MONTH, clickedDate.dateNumber.toInt())
            updateRemindersUI()
        }
        rvDates.scrollToPosition(todayIndex)
    }

    private fun updateRemindersUI() {
        val userEmail = authManager.getUserEmail()
        
        // FILTER: Hanya ambil catatan milik user aktif
        val userNotes = NoteManager.noteList.filter { it.ownerEmail == userEmail }
        
        val today = Calendar.getInstance()
        val isToday = selectedCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                      selectedCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
        
        tvRemindersTitle.text = if (isToday) getString(R.string.today_s_reminders) 
                               else getString(R.string.reminders_for_date, SimpleDateFormat("MMM dd", Locale.getDefault()).format(selectedCalendar.time))

        // Filter berdasarkan tanggal yang dipilih
        val filteredReminders = userNotes.filter { note ->
            note.year == selectedCalendar.get(Calendar.YEAR) &&
            note.month == selectedCalendar.get(Calendar.MONTH) &&
            note.dateNumber == selectedCalendar.get(Calendar.DAY_OF_MONTH)
        }.map { note ->
            Reminder(R.drawable.ic_notes, note.time, note.title, note.preview)
        }

        if (filteredReminders.isEmpty()) {
            rvReminders.visibility = View.GONE
            tvNoReminders.visibility = View.VISIBLE
        } else {
            rvReminders.visibility = View.VISIBLE
            tvNoReminders.visibility = View.GONE
            rvReminders.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvReminders.adapter = ReminderAdapter(filteredReminders)
        }
    }

    private fun setupRecentNotes(view: View) {
        val rvRecentNotes: RecyclerView = view.findViewById(R.id.rv_recent_notes)
        val userEmail = authManager.getUserEmail()
        
        // FILTER: Hanya ambil catatan milik user aktif
        val userNotes = NoteManager.noteList.filter { it.ownerEmail == userEmail }
        
        rvRecentNotes.layoutManager = LinearLayoutManager(requireContext())
        rvRecentNotes.adapter = NoteAdapter(userNotes)
    }

    override fun onResume() {
        super.onResume()
        setupGreeting()
        updateRemindersUI()
        setupRecentNotes(requireView())
    }

    private fun setupGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingWord = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..21 -> "Good Evening"
            else -> "Good Night"
        }
        tvGreeting.text = "$greetingWord, ${authManager.getUserName()}"
    }
}