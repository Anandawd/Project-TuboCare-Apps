package com.project.tubocare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import com.project.tubocare.medication.presentation.util.scheduleWeeklyReset
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TuboCareApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationMedicationChannel()
        createNotificationAppointmentChannel()
        scheduleWeeklyReset(this)
    }

    private fun createNotificationMedicationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_medication_name)
            val descriptionText = getString(R.string.notification_channel_medication_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("medication_reminder_channel", name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 1000, 500)
                setSound(Uri.parse("android.resource://${packageName}/${R.raw.notification_sound_5}"), null)
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationAppointmentChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_appointment_name)
            val descriptionText = getString(R.string.notification_channel_appointment_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("appointment_reminder_channel", name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 1000, 500)
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
