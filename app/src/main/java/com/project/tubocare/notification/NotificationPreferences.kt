package com.project.tubocare.notification

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.tubocare.notification.domain.model.NotificationData

class NotificationPreferences(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("notification_preferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveNotification(userId: String,notification: NotificationData) {
        val notifications = getNotification(userId).toMutableList()
        notifications.add(notification)
        val json = gson.toJson(notifications)
        sharedPreferences.edit().putString("notifications_$userId", json).apply()
    }

    fun getNotification(userId: String): List<NotificationData> {
        val json = sharedPreferences.getString("notifications_$userId", null) ?: return emptyList()
        val type = object : TypeToken<List<NotificationData>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearNotifications(userId: String){
        sharedPreferences.edit().remove("notifications_$userId").apply()
    }
}
