package com.project.tubocare.auth.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.auth.domain.repository.AuthRepository
import com.project.tubocare.auth.presentation.event.LoginEvent
import com.project.tubocare.auth.presentation.state.LoginState
import com.project.tubocare.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    val currentUser = authRepository.currentUser()
    val hasUser = authRepository.hasUser()

    var loginState by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.Login -> {
                login()
            }

            LoginEvent.ResetPassword -> {
                resetPassword()
            }

            LoginEvent.ClearToast -> {
                clearToastMessage()
            }

            is LoginEvent.OnEmailChanged -> {
                onLoginEmailChange(email = event.email)
            }

            is LoginEvent.OnPasswordChanged -> {
                onLoginPasswordChange(password = event.password)
            }

            is LoginEvent.OnResetPasswordChanged -> {
                onResetPasswordChange(email = event.email)
            }
        }
    }

    // repository functions
    private fun login() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            loginState = loginState.copy(loginError = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            loginState = loginState.copy(isLoading = true)

            if (!validateLoginForm()) {
                throw IllegalArgumentException("Formulir login belum lengkap")
            }

            val result = authRepository.login(loginState.email, loginState.password)
            when (result) {
                is Resource.Success -> {
                    loginState = loginState.copy(
                        isSuccessLogin = true,
                        loginError = null
                    )
                }

                is Resource.Error -> {
                    loginState = loginState.copy(
                        isSuccessLogin = false,
                        loginError = result.message
                    )
                }
                is Resource.Loading -> {
                    loginState = loginState.copy(isLoading = true)
                }
            }
        } catch (e: Exception) {
            loginState = loginState.copy(
                isSuccessLogin = false,
                loginError = e.message
            )
        } finally {
            loginState = loginState.copy(
                isLoading = false
            )
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun resetPassword() = viewModelScope.launch {
        try {
            loginState = loginState.copy(isLoading = true)
            if (validateResetPasswordForm()) {
                authRepository.resetPassword(loginState.resetPassword)
                loginState = loginState.copy(
                    isSuccessResetPassword = true,
                    resetError = null
                )
            } else {
                loginState = loginState.copy(isLoading = false)
                throw IllegalArgumentException("Gagal reset password")
            }
        } catch (e: Exception) {
            loginState = loginState.copy(
                isSuccessResetPassword = false,
                resetError = e.message
            )
        } finally {
            loginState = loginState.copy(isLoading = false)
        }
    }

    private fun clearToastMessage() {
        loginState = loginState.copy(
            loginError = null,
            resetError = null
        )
    }

    // login functions
    private fun onLoginEmailChange(email: String) {
        loginState = loginState.copy(email = email)
        if (email.isBlank()) {
            loginState = loginState.copy(loginErrorEmail = "Email tidak boleh kosong")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginState = loginState.copy(loginErrorEmail = "Format email tidak valid")
        } else {
            loginState = loginState.copy(loginErrorEmail = null)
        }
    }

    private fun onLoginPasswordChange(password: String) {
        loginState = loginState.copy(password = password)
        if (password.isBlank()) {
            loginState = loginState.copy(loginErrorPassword = "Password tidak boleh kosong")
        } else if (password.length < 8) {
            loginState = loginState.copy(loginErrorPassword = "Password harus minimal 8 karakter")
        } else {
            loginState = loginState.copy(loginErrorPassword = null)
        }
    }

    private fun onResetPasswordChange(email: String) {
        loginState = loginState.copy(resetPassword = email)
        if (email.isBlank()) {
            loginState = loginState.copy(resetErrorEmail = "Email tidak boleh kosong")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginState = loginState.copy(resetErrorEmail = "Format email tidak valid")
        } else {
            loginState = loginState.copy(resetErrorEmail = null)
        }
    }

    // validate functions
    private fun validateLoginForm(): Boolean {
        return loginState.loginErrorEmail == null &&
                loginState.loginErrorPassword == null &&
                loginState.email.isNotBlank() &&
                loginState.password.isNotBlank()
    }

    private fun validateResetPasswordForm(): Boolean {
       return loginState.resetErrorEmail == null &&
               loginState.resetPassword.isNotBlank()
    }
}