package com.example.noteme.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.noteme.R
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // Mengambil data yang dikirim dari CreateNoteFragment
        val title = inputData.getString("note_title") ?: "Note Reminder"
        val day = inputData.getInt("note_day", -1)
        val month = inputData.getInt("note_month", -1)
        val year = inputData.getInt("note_year", -1)

        if (day != -1) {
            // Waktu Hari Ini (Clean: jam 00:00)
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }

            // Waktu Target Agenda (Clean: jam 00:00)
            val targetDate = Calendar.getInstance().apply {
                set(year, month, day, 0, 0, 0); set(Calendar.MILLISECOND, 0)
            }

            // Hitung selisih hari
            val diffInMillis = targetDate.timeInMillis - today.timeInMillis
            val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

            // Tentukan pesan berdasarkan selisih hari
            val message = when {
                diffInDays == 0L -> "Ada agenda hari ini!"
                diffInDays > 0 -> "$diffInDays hari lagi menuju agenda ini."
                else -> "Agenda ini sudah terlewat."
            }

            showNotification("Reminder: $title", message)
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "noteme_reminder_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "NoteMe Reminders", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notes)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}