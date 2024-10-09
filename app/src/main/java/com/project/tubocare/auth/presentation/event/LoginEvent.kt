package com.project.tubocare.auth.presentation.event

import android.content.Context

sealed class LoginEvent {
    object Login: LoginEvent()
    object ResetPassword: LoginEvent()
    object ClearToast: LoginEvent()
    data class OnEmailChanged(val email: String): LoginEvent()
    data class OnPasswordChanged(val password: String): LoginEvent()
    data class OnResetPasswordChanged(val email: String): LoginEvent()
}