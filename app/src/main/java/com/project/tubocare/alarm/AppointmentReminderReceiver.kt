package com.project.tubocare.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.project.tubocare.MainActivity
import com.project.tubocare.R
import com.project.tubocare.notification.NotificationPreferences
import com.project.tubocare.notification.domain.model.NotificationData
import java.time.LocalDateTime
import java.util.Locale

class AppointmentReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AppointmentReminder", "onReceive called")

        val userId = intent.getStringExtra("userId")
        val appointmentName = intent.getStringExtra("appointmentName")?.lowercase(Locale.ROOT)
        val appointmentTime = intent.getStringExtra("appointmentTime")
        val appointmentLocation = intent.getStringExtra("appointmentLocation")
        val appointmentId = intent.getStringExtra("appointmentId")
        val reminderType = intent.getStringExtra("reminderType")

        val notificationData = NotificationData(
            id = appointmentId.hashCode().toString(),
            name = "Jadwal Kontrol",
            desc = "Jangan lupa jadwal $appointmentName pukul $appointmentTime di $appointmentLocation",
            timestamp = LocalDateTime.now()
        )
        val notificationPreferences = NotificationPreferences(context)
        notificationPreferences.saveNotification(userId.orEmpty(), notificationData)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val mainPendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, "appointment_reminder_channel")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)

        if (reminderType == "H-1") {
            notificationBuilder
                .setContentTitle("Jadwal Kontrol Besok!")
                .setContentText("Jangan lupa jadwal $appointmentName besok pukul $appointmentTime di $appointmentLocation")
        } else if (reminderType == "H-H") {
            notificationBuilder
                .setContentTitle("Jadwal Kontrol Hari Ini!")
                .setContentText("Jangan lupa jadwal $appointmentName hari ini pukul $appointmentTime di $appointmentLocation")
        }

        notificationManager.notify(appointmentId.hashCode(), notificationBuilder.build())
        Log.d("AppointmentReminder", "Notification displayed for $reminderType")
    }
}