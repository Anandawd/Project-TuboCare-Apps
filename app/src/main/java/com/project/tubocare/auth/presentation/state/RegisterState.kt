package com.project.tubocare.auth.presentation.state

import java.util.Date


data class RegisterState(
    val userId: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name: String = "",
    val bdate: Date? = null,
    val gender: String = "",
    val address: String = "",
    val phone: String = "",
    val puskesmas: String = "",
    val role: String = "",
    val imagePath: String = "",
    val code : String = "",

    val isLoading: Boolean = false,
    val isSuccessRegister: Boolean = false,
    val isSuccessSave: Boolean = false,

    val registerErrorEmail: String? = null,
    val registerErrorPassword: String? = null,
    val registerErrorConfirm: String? = null,
    val registerErrorName: String? = null,
    val registerErrorBdate: String? = null,
    val registerErrorGender: String? = null,
    val registerErrorAddress: String? = null,
    val registerErrorPhone: String? = null,
    val registerErrorPuskesmas: String? = null,
    val registerErrorRole: String? = null,
    val registerErrorCode: String? = null,
    val registerSaveError: String? = null,
    val registerError: String? = null,
)

/*
data class RegisterState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: String = "",
    val puskesmas: String = "",
    val name: String = "",
    val bdate: Date = Date(),
    val gender: String = "",
    val address: String = "",
    val phone: String = "",

    val isInputValid: Boolean = false,
    val isPasswordShown: Boolean = false,
    val isConfirmPasswordShown: Boolean = false,

    val errorMessageInput: String? = null,

    val isLoading: Boolean = false,
    val isSuccessfullyRegistered: Boolean = false,

    val errorMessageRegisterProcess: String? = null,

    val toastMessage: String? = null
)
*/
