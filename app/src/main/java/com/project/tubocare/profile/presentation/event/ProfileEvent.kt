package com.project.tubocare.profile.presentation.event

import com.project.tubocare.auth.presentation.event.RegisterEvent
import java.util.Date

sealed class ProfileEvent {
    object LoadData: ProfileEvent()
    object Update: ProfileEvent()
    object Logout: ProfileEvent()
    object ClearSuccessUpdate: ProfileEvent()
    object ClearToast: ProfileEvent()

    data class OnNameChanged(val value: String): ProfileEvent()
    data class OnBdateChanged(val value: Date?): ProfileEvent()
    data class OnAddressChanged(val value: String): ProfileEvent()
    data class OnPhoneChanged(val value: String): ProfileEvent()
    data class OnImageUrlChanged(val value: String): ProfileEvent()
    data class OnImagePathChanged(val value: String): ProfileEvent()
    data class updateImagePath(val value: String): ProfileEvent()
}