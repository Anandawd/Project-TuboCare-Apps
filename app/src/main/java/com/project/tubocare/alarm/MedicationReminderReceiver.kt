package com.project.tubocare.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.project.tubocare.MainActivity
import com.project.tubocare.R
import com.project.tubocare.notification.NotificationPreferences
import com.project.tubocare.notification.domain.model.NotificationData
import com.project.tubocare.notification.presentation.NotificationViewModel
import java.time.LocalDateTime
import java.util.Locale

class MedicationReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MedicationReminder", "onReceive called")

        val userId = intent.getStringExtra("userId")
        val medicationId = intent.getStringExtra("medicationId")
        val medicationName = intent.getStringExtra("medicationName")?.lowercase(Locale.ROOT)
        val index = intent.getStringExtra("index")
        val day = intent.getStringExtra("day")
        val time = intent.getStringExtra("time")

        val notificationData = NotificationData(
            id = medicationId.toString(),
            name = "Minum Obat",
            desc = "Jadwal minum obat $medicationName pukul $time",
            timestamp = LocalDateTime.now()
        )
        val notificationPreferences = NotificationPreferences(context)
        notificationPreferences.saveNotification(userId.orEmpty(), notificationData)

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(longArrayOf(0,500,1000,500), -1 )

        val soundUri: Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.notification_sound_6)
        val ringtone = RingtoneManager.getRingtone(context, soundUri)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val takeIntent = Intent(context, MainActivity::class.java).apply {
            action = "ACTION_TAKE_MEDICATION"
            putExtra("notificationId", medicationId.hashCode())
        }

        val takePendingIntent = PendingIntent.getActivity(
            context,
            0,
            takeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeIntent = Intent(context, SnoozeReceiver::class.java).apply {
            action = "ACTION_SNOOZE_MEDICATION"
            putExtra("medicationName", medicationName)
            putExtra("time", time)
            putExtra("notificationId", medicationId.hashCode())
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "medication_reminder_channel")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Sudah waktunya minum obat!")
            .setContentText("Jadwal minum obat $medicationName pukul $time")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(true)
            .setContentIntent(takePendingIntent)
            .setVibrate(longArrayOf(0, 500, 1000, 500))
            .setSound(soundUri)
            .addAction(0, "Ambil", takePendingIntent)
            .addAction(0, "Tunda", snoozePendingIntent)
            .build()

        notificationManager.notify(medicationId.hashCode(), notification)
        ringtone.play()
        Log.d("MedicationReminder", "Notification displayed")
    }
}