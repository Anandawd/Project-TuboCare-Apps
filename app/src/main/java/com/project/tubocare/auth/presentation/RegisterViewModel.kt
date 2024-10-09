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
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.auth.domain.repository.AuthRepository
import com.project.tubocare.auth.presentation.event.RegisterEvent
import com.project.tubocare.auth.presentation.state.RegisterState
import com.project.tubocare.core.util.Resource
import com.project.tubocare.auth.util.puskesmasCodes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModel() {

    val currentUser = authRepository.currentUser()

    val hasUser = authRepository.hasUser()

    var registerState by mutableStateOf(RegisterState())
        private set

    fun onEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.Register -> {
                register()
            }

            RegisterEvent.Save -> {
                saveUser()
            }

            RegisterEvent.ClearToast -> {
                clearToastMessage()
            }

            is RegisterEvent.OnRoleChanged -> {
                onRoleChanged(event.value)
            }

            is RegisterEvent.OnPuskesmasChanged -> {
                onPuskesmasChanged(event.value)
            }

            is RegisterEvent.OnEmailChanged -> {
                onEmailChanged(event.value)
            }

            is RegisterEvent.OnPasswordChanged -> {
                onPasswordChanged(event.value)
            }

            is RegisterEvent.OnConfirmPasswordChanged -> {
                onConfirmPasswordChanged(event.value)
            }

            is RegisterEvent.OnNameChanged -> {
                onNameChanged(event.value)
            }

            is RegisterEvent.OnBdateChanged -> {
                onBdateChanged(event.value)
            }

            is RegisterEvent.OnGenderChanged -> {
                onGenderChanged(event.value)
            }

            is RegisterEvent.OnAddressChanged -> {
                onAddressChanged(event.value)
            }

            is RegisterEvent.OnPhoneChanged -> {
                onPhoneChanged(event.value)
            }

            is RegisterEvent.OnCodeChanged -> {
                onCodeChanged(event.value)
            }
        }
    }


    // repository functions
    private fun register() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            registerState = registerState.copy(registerError = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            registerState = registerState.copy(isLoading = true)

            val user = User(
                userId = registerState.userId,
                email = registerState.email,
                name = currentUser?.displayName ?: "",
                bdate = registerState.bdate,
                gender = registerState.gender,
                address = registerState.address,
                phone = registerState.phone,
                role = registerState.role,
                puskesmas = registerState.puskesmas,
                imageUrl = registerState.imagePath
            )
            if (validateRegisterFormOne()) {
                val isEmailAvailable = authRepository.isEmailAvailable(registerState.email)
                if (!isEmailAvailable) {
                    throw IllegalArgumentException("Email sudah terdaftar")
                }

                val result =
                    authRepository.register(user, registerState.password)
                when (result) {
                    is Resource.Success -> {
                        registerState = registerState.copy(
                            isSuccessRegister = true,
                            registerError = null,
                        )
                    }

                    is Resource.Error -> {
                        registerState = registerState.copy(
                            isSuccessRegister = false,
                            registerError = result.message
                        )
                    }
                    is Resource.Loading -> {
                        registerState = registerState.copy(isLoading = true)
                    }
                }

            } else {
                throw IllegalArgumentException("Formulir pendaftaran tidak boleh kosong")
            }
        } catch (e: Exception) {
            registerState = registerState.copy(
                isSuccessRegister = false,
                registerError = e.message
            )
        } finally {
            registerState = registerState.copy(
                isLoading = false
            )
        }
    }

    private fun saveUser() = viewModelScope.launch {
        try {
            registerState = registerState.copy(isLoading = true)

            val user = User(
                userId = currentUser?.uid.orEmpty(),
                email = currentUser?.email.orEmpty(),
                name = registerState.name,
                bdate = registerState.bdate,
                gender = registerState.gender,
                address = registerState.address,
                phone = registerState.phone,
                puskesmas = registerState.puskesmas,
                role = registerState.role,
                imageUrl = registerState.imagePath
            )

            if (validateRegisterFormTwo()) {
                if (registerState.role == "Tenaga Kesehatan" && !validateCode(registerState.puskesmas, registerState.code)){
                    throw IllegalArgumentException("Kode puskesmas tidak valid")
                }

                val result = authRepository.saveUser(user)
                when (result) {
                    is Resource.Success -> {
                        delay(500)
                        registerState = registerState.copy(
                            isSuccessSave = true,
                            registerSaveError = null
                        )
                    }

                    is Resource.Error -> {
                        registerState = registerState.copy(
                            isSuccessSave = false,
                            registerSaveError = result.message
                        )
                    }
                    is Resource.Loading -> {
                        registerState = registerState.copy(isLoading = true)
                    }
                }
            } else {
                throw IllegalArgumentException("Formulir lengkapi profil tidak boleh kosong")
            }
        } catch (e: Exception) {
            registerState = registerState.copy(
                registerSaveError = e.localizedMessage
            )
        } finally {
            registerState = registerState.copy(isLoading = false)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun clearToastMessage() {
        registerState = registerState.copy(
            registerError = null,
            registerSaveError = null
        )
    }

    // register functions
    private fun onEmailChanged(email: String) {
        registerState = registerState.copy(email = email)
        if (email.isBlank()) {
            registerState = registerState.copy(registerErrorEmail = "Email tidak boleh kosong")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registerState = registerState.copy(registerErrorEmail = "Format email tidak valid")
        } else {
            registerState = registerState.copy(registerErrorEmail = null)
        }
    }

    private fun onPasswordChanged(password: String) {
        registerState = registerState.copy(password = password)
        if (password.isBlank()) {
            registerState =
                registerState.copy(registerErrorPassword = "Password tidak boleh kosong")
        } else if (password.length < 8) {
            registerState = registerState.copy(registerErrorPassword = "Password harus minimal 8 karakter")
        } else {
            registerState = registerState.copy(registerErrorPassword = null)
        }
    }

    private fun onConfirmPasswordChanged(password: String) {
        registerState = registerState.copy(confirmPassword = password)
        if (password != registerState.password) {
            registerState = registerState.copy(registerErrorConfirm = "Password tidak cocok")
        } else {
            registerState = registerState.copy(registerErrorConfirm = null)
        }
    }

    private fun onNameChanged(name: String) {
        registerState = registerState.copy(name = name)
        if (name.isBlank()) {
            registerState = registerState.copy(registerErrorName = "Nama tidak boleh kosong")
        } else {
            registerState = registerState.copy(registerErrorName = null)
        }
    }

    private fun onBdateChanged(date: Date?) {
        registerState = registerState.copy(bdate = date)
        if (date == null){
            registerState = registerState.copy(registerErrorBdate = "Tanggal lahir tidak boleh kosong")
        } else {
            registerState = registerState.copy(registerErrorBdate = null)
        }
    }

    private fun onGenderChanged(gender: String) {
        registerState = registerState.copy(gender = gender)
        if (gender.isBlank()) {
            registerState = registerState.copy(registerErrorGender = "Jenis kelamin tidak boleh kosong")
        } else {
            registerState = registerState.copy(registerErrorGender = null)
        }
    }

    private fun onAddressChanged(address: String) {
        registerState = registerState.copy(address = address)
        if (address.isBlank()) {
            registerState = registerState.copy(registerErrorAddress = "Alamat tidak boleh kosong")
        } else {
            registerState =  registerState.copy(registerErrorAddress = null)
        }
    }

    private fun onPhoneChanged(phone: String) {
        registerState = registerState.copy(phone = phone)
        if (phone.isBlank()) {
            registerState =  registerState.copy(registerErrorPhone = "Nomor handphone tidak boleh kosong")
        } else if (!Patterns.PHONE.matcher(phone).matches()){
            registerState =  registerState.copy(registerErrorPhone = "Format nomor handphone tidak valid")
        } else if (phone.length < 10 || phone.length > 13) {
            registerState = registerState.copy(registerErrorPhone = "Nomor handphone minimal 10 dan maksimal 13 karakter")
        } else {
            registerState =  registerState.copy(registerErrorPhone = null)
        }
    }

    private fun onPuskesmasChanged(puskesmas: String) {
        registerState = registerState.copy(puskesmas = puskesmas)
        if (puskesmas.isBlank()) {
            registerState = registerState.copy(registerErrorPuskesmas = "Puskesmas tidak boleh kosong")
        } else {
            registerState =  registerState.copy(registerErrorPuskesmas = null)
        }
    }

    private fun onRoleChanged(role: String) {
        registerState = registerState.copy(role = role)
        if (role.isBlank()) {
            registerState = registerState.copy(registerErrorRole = "Peran tidak boleh kosong")
        } else {
            registerState =  registerState.copy(registerErrorRole = null)
        }
    }

    private fun onCodeChanged(code: String) {
        registerState = registerState.copy(code = code)
        if (code.isBlank()) {
            registerState = registerState.copy(registerErrorCode = "Kode tidak boleh kosong")
        } else {
            registerState =  registerState.copy(registerErrorCode = null)
        }
    }

    // validate functions
    private fun validateRegisterFormOne(): Boolean {
        return registerState.registerErrorEmail == null &&
                registerState.registerErrorPassword == null &&
                registerState.registerErrorConfirm == null &&
                registerState.email.isNotBlank() &&
                registerState.password.isNotBlank() &&
                registerState.confirmPassword.isNotBlank()
    }

    private fun validateRegisterFormTwo(): Boolean {
        return registerState.registerErrorName == null &&
                registerState.registerErrorBdate == null &&
                registerState.registerErrorGender == null &&
                registerState.registerErrorAddress == null &&
                registerState.registerErrorPhone == null &&
                registerState.registerErrorRole == null &&
                registerState.registerErrorPuskesmas == null &&
                registerState.name.isNotBlank() &&
                registerState.bdate != null &&
                registerState.gender.isNotBlank() &&
                registerState.address.isNotBlank() &&
                registerState.phone.isNotBlank() &&
                registerState.role.isNotBlank()
    }

    private fun validateCode(puskesmas: String, code: String): Boolean {
        return puskesmasCodes[puskesmas] == code
    }
}