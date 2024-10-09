package com.project.tubocare.notification.domain.model

import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

data class NotificationData(
    val id: String,
    val name: String,
    val desc: String,
    val timestamp: LocalDateTime
)
