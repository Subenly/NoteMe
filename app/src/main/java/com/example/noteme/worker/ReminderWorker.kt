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
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // Ambil waktu hari ini dan set jam ke 00:00 agar perhitungan hari akurat
        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Cek semua catatan yang ada di NoteManager
        NoteManager.noteList.forEach { note ->
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

            // --- KODE TES: Munculkan notifikasi jika catatan untuk hari ini atau ke depan ---
            if (diffInDays >= 0L) {
                showNotification(
                    title = "Test Reminder: ${note.title}",
                    message = "Notifikasi berfungsi! Sisa waktu: $diffInDays hari."
                )
            }
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "noteme_reminder_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Untuk Android 8.0 (Oreo) ke atas, wajib membuat Notification Channel
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

        // Desain bentuk notifikasinya
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ganti dengan ikon aplikasi Anda (misal R.drawable.ic_notes)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Munculkan notifikasi dengan ID unik (menggunakan waktu agar tidak saling menimpa)
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}