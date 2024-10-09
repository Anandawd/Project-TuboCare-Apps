package com.project.tubocare.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar

class SnoozeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicationName = intent.getStringExtra("medicationName")
        val time = intent.getStringExtra("time")
        val notificationId = intent.getIntExtra("notificationId", 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val snoozeIntent = Intent(context, MedicationReminderReceiver::class.java).apply {
            putExtra("medicationName", medicationName)
            putExtra("time", time)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, notificationId, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 5)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}