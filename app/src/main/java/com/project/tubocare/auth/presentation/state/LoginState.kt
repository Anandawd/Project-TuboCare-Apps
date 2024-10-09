package com.project.tubocare.auth.presentation.state


data class LoginState(
    var email: String = "",
    var password: String = "",
    var resetPassword: String = "",

    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val isSuccessResetPassword: Boolean = false,

    val loginErrorEmail: String? = null,
    val loginErrorPassword: String? = null,
    val resetErrorEmail: String? = null,
    val loginError: String? = null,
    val resetError: String? = null,
)