package com.example.noteme.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.noteme.R
import com.example.noteme.model.NoteManager
import com.example.noteme.utils.AuthManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // 1. PENTING: Muat data dari memori HP agar Worker punya daftar catatan terbaru
        NoteManager.loadNotes(context)

        // 2. Identifikasi siapa pengguna yang sedang login
        val authManager = AuthManager(context)
        val currentUserEmail = authManager.getUserEmail()

        // 3. Ambil waktu hari ini (set ke jam 00:00 agar perbandingan tanggal akurat)
        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 4. Saring catatan: HANYA milik user aktif
        val userNotes = NoteManager.noteList.filter { it.ownerEmail == currentUserEmail }

        userNotes.forEach { note ->
            val noteCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, note.year)
                set(Calendar.MONTH, note.month)
                set(Calendar.DAY_OF_MONTH, note.dateNumber)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Hitung selisih hari
            val diffInMillis = noteCal.timeInMillis - todayCal.timeInMillis
            val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

            // Munculkan notifikasi jika jadwalnya adalah HARI INI
            if (diffInDays == 0L) {
                showNotification(
                    title = "Reminder: ${note.title}",
                    message = "Jangan lupa! Kamu punya agenda untuk hari ini."
                )
            }
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "noteme_reminder_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "NoteMe Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel untuk pengingat catatan NoteMe"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notes) // Pastikan ikon ini ada
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Tampilkan notifikasi dengan ID unik
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}