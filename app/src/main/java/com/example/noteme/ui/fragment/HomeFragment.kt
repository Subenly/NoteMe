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
import com.example.noteme.model.UserManager
import com.example.noteme.utils.AuthManager

class HomeFragment : Fragment() {

    private lateinit var tvGreeting: TextView
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext()) // Inisialisasi AuthManager
        tvGreeting = view.findViewById(R.id.tv_greeting)

        // Setup komponen lainnya...
        setupLiveDateHeader(view)
        setupHorizontalCalendar(view)
        setupReminders(view)
        setupRecentNotes(view)
    }

    private fun setupLiveDateHeader(view: View) {
        val tvMonthYear = view.findViewById<TextView>(R.id.tv_month_year)
        val calendar = Calendar.getInstance()

        // Format otomatis menjadi nama bulan dan tahun berjalan (Contoh: "Juni 2026")
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        tvMonthYear.text = monthYearFormat.format(calendar.time)
    }

    private fun setupHorizontalCalendar(view: View) {
        val rvDates = view.findViewById<RecyclerView>(R.id.rv_dates)
        val dateList = mutableListOf<DateItem>()

        val calendar = Calendar.getInstance()
        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        var todayIndex = 0

        // Format untuk singkatan nama hari (MON, TUE, WED)
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

        val tempCalendar = Calendar.getInstance()
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)

        // Membuat daftar hari dari tanggal 1 hingga akhir bulan
        for (i in 1..maxDays) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, i)
            val dayString = dayFormat.format(tempCalendar.time).uppercase()

            // Tandai warna otomatis untuk tanggal hari ini
            val isSelected = (i == today)
            if (isSelected) {
                todayIndex = i - 1
            }

            dateList.add(DateItem(dayString, i.toString(), isSelected))
        }

        // Layout ke samping (Horizontal)
        rvDates.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Mengatur Adapter dan logika Klik (Ubah warna tanggal yang dipilih)
        val adapter = DateAdapter(dateList) { clickedDate ->
            dateList.forEach { it.isSelected = (it.dateNumber == clickedDate.dateNumber) }
            rvDates.adapter?.notifyDataSetChanged()

            // Opsional: Di sini Anda bisa memfilter Today's Reminder sesuai tanggal yang diklik.
        }
        rvDates.adapter = adapter

        // GULIR OTOMATIS: Langsung geser kalender ke posisi tanggal hari ini
        rvDates.scrollToPosition(todayIndex)
    }

    private fun setupReminders(view: View) {
        val rvReminders = view.findViewById<RecyclerView>(R.id.rv_reminders)

        // 1. Pastikan data dummy awal terisi jika list masih kosong
        NoteManager.addDummyDataIfNeeded()

        // 2. Ambil waktu saat ini sebagai jangkar/patokan perhitungan
        val todayCal = Calendar.getInstance()
        val currentYear = todayCal.get(Calendar.YEAR)
        val currentMonth = todayCal.get(Calendar.MONTH)
        val currentDay = todayCal.get(Calendar.DAY_OF_MONTH)

        // Buat batas atas waktu (7 hari dari sekarang)
        val upperLimitCal = Calendar.getInstance()
        upperLimitCal.add(Calendar.DAY_OF_MONTH, 7)

        // 3. Filter catatan dari NoteManager yang masuk dalam rentang 7 hari ke depan
        val filteredReminders = NoteManager.noteList.filter { note ->
            // Buat objek Calendar tiruan berdasarkan jadwal tanggal dari note tersebut
            val noteCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, note.year)
                set(Calendar.MONTH, note.month)
                set(Calendar.DAY_OF_MONTH, note.dateNumber)
                // Set jam/menit ke 0 agar perbandingan tanggal angka menjadi murni dan akurat
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Set juga jam patokan hari ini ke 0 demi keakuratan perbandingan hari
            val cleanTodayCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, currentYear)
                set(Calendar.MONTH, currentMonth)
                set(Calendar.DAY_OF_MONTH, currentDay)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Kondisi: Tanggal note harus sama atau setelah hari ini AND sebelum atau sama dengan 7 hari ke depan
            (!noteCal.before(cleanTodayCal)) && (!noteCal.after(upperLimitCal))
        }.map { note ->
            // 4. Konversi objek Note yang lolos filter menjadi objek Reminder untuk kebutuhan Adapter
            val formatBulan = SimpleDateFormat("MMM", Locale.getDefault())
            val calTemp = Calendar.getInstance().apply { set(Calendar.MONTH, note.month) }
            val namaBulan = formatBulan.format(calTemp.time).uppercase()

            Reminder(
                iconResId = R.drawable.ic_notes, // Ikon default bawaan sistem navigasi Anda
                time = "${note.time} (${namaBulan} ${note.dateNumber})", // Menampilkan jam sekaligus info tanggal pengingat
                title = note.title,
                description = note.preview
            )
        }

        // 5. Pasang hasil saringan ke RecyclerView horizontal
        rvReminders.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvReminders.adapter = ReminderAdapter(filteredReminders)
    }

    private fun setupRecentNotes(view: View) {
        val rvRecentNotes: RecyclerView = view.findViewById(R.id.rv_recent_notes)
        NoteManager.addDummyDataIfNeeded()
        rvRecentNotes.layoutManager = LinearLayoutManager(requireContext())
        rvRecentNotes.adapter = NoteAdapter(NoteManager.noteList)
    }

    override fun onResume() {
        super.onResume()
        setupGreeting()
    }

    private fun setupGreeting() {
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        val greetingWord = when (timeOfDay) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            in 18..20 -> "Good Evening"
            else -> "Good Night"
        }

        // AMBIL NAMA LANGSUNG DARI AuthManager
        val userName = authManager.getUserName()

        tvGreeting.text = "$greetingWord, $userName"
    }
}