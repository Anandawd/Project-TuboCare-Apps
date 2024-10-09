package com.project.tubocare.notification.presentation.event

sealed class NotificationEvent {
    object GetNotifications: NotificationEvent()
    object DeleteNotifications: NotificationEvent()
    object ResetStatus: NotificationEvent()
    object ClearToast: NotificationEvent()
}