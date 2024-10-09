package com.project.tubocare.auth.presentation.event

import java.util.Date

sealed class RegisterEvent {
    object Register: RegisterEvent()
    object Save: RegisterEvent()
    object ClearToast: RegisterEvent()
    data class OnEmailChanged(val value: String): RegisterEvent()
    data class OnPasswordChanged(val value: String): RegisterEvent()
    data class OnConfirmPasswordChanged(val value: String): RegisterEvent()
    data class OnNameChanged(val value: String): RegisterEvent()
    data class OnPuskesmasChanged(val value: String): RegisterEvent()
    data class OnRoleChanged(val value: String): RegisterEvent()
    data class OnBdateChanged(val value: Date?): RegisterEvent()
    data class OnAddressChanged(val value: String): RegisterEvent()
    data class OnGenderChanged(val value: String): RegisterEvent()
    data class OnPhoneChanged(val value: String): RegisterEvent()
    data class OnCodeChanged(val value: String): RegisterEvent()
}