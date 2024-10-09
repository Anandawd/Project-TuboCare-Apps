package com.project.tubocare.notification.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.notification.NotificationPreferences
import com.project.tubocare.notification.presentation.event.NotificationEvent
import com.project.tubocare.notification.presentation.state.NotificationState
import com.project.tubocare.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val application: Application
) : ViewModel() {

    val userId = profileRepository.getUserId()

    var notificationState by mutableStateOf(NotificationState())
        private set

    init {
        getNotifications()
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            NotificationEvent.GetNotifications -> {
                getNotifications()
            }
            NotificationEvent.DeleteNotifications -> {
                deleteNotifications()
            }
            NotificationEvent.ClearToast -> {
                clearToastMessage()
            }
            NotificationEvent.ResetStatus -> {
                resetStatus()
            }

        }
    }

    private fun getNotifications() = viewModelScope.launch{
        try {
            val notificationPreferences = NotificationPreferences(application)
            notificationState = notificationState.copy(notificationDataList = notificationPreferences.getNotification(userId = userId))
            notificationState = notificationState.copy(isSuccessGetNotifications = true)
        } catch (e: Exception){
            notificationState = notificationState.copy(errorGetNotifications = e.localizedMessage)
        }
    }

    private fun deleteNotifications() = viewModelScope.launch {
        try {
            notificationState = notificationState.copy(isLoadingDelete = true)
            val notificationPreferences = NotificationPreferences(application)
            notificationPreferences.clearNotifications(userId = userId)
            notificationPreferences.getNotification(userId = userId)
            notificationState = notificationState.copy(isSuccessDelete = true)
        } catch (e: Exception){
            notificationState = notificationState.copy(errorDelete = e.localizedMessage)
        } finally {
            notificationState = notificationState.copy(isLoadingDelete = false)
        }
    }

    private fun resetStatus() {
        notificationState = notificationState.copy(
            isSuccessDelete = false,
            isSuccessGetNotifications = false,
        )
    }

    private fun clearToastMessage() {
        notificationState = notificationState.copy(
            errorDelete = null,
            errorGetNotifications = null,
        )
    }
}
