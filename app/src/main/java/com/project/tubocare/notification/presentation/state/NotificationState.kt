package com.project.tubocare.notification.presentation.state

import com.project.tubocare.core.util.Resource
import com.project.tubocare.notification.domain.model.NotificationData
import java.time.LocalTime
import java.util.Date

data class NotificationState(
    val notificationDataList: List<NotificationData> = emptyList(),
    val id: String = "",
    val name: String = "",
    val desc: String = "",

    val isLoadingDelete: Boolean = false,

    val isSuccessDelete: Boolean = false,
    val isSuccessGetNotifications: Boolean = false,

    val errorDelete: String? = null,
    val errorGetNotifications: String? = null,
)
