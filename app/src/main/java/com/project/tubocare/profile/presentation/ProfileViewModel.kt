package com.project.tubocare.profile.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.core.util.Resource
import com.project.tubocare.profile.presentation.event.ProfileEvent
import com.project.tubocare.profile.presentation.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.notification.NotificationPreferences
import com.project.tubocare.profile.domain.repository.ProfileRepository
import com.project.tubocare.profile.presentation.state.toUser
import com.project.tubocare.profile.presentation.util.downloadAndSaveImageProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val application: Application
) : ViewModel() {

    val currentUser = profileRepository.currentUser()

    val userId = profileRepository.getUserId()
    var profileState by mutableStateOf(ProfileState())
        private set

    init {
        loadUserData()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.LoadData -> {
                loadUserData()
            }

            ProfileEvent.Update -> {
                updateUserProfile()
            }

            ProfileEvent.ClearSuccessUpdate -> {
                resetStatus()
            }

            ProfileEvent.ClearToast -> {
                clearToastMessage()
            }

            ProfileEvent.Logout -> {
                logout()
            }

            is ProfileEvent.OnNameChanged -> {
                onNameChange(event.value)
            }

            is ProfileEvent.OnBdateChanged -> {
                onBdateChange(event.value)
            }

            is ProfileEvent.OnAddressChanged -> {
                onAddressChange(event.value)
            }

            is ProfileEvent.OnPhoneChanged -> {
                onPhoneChange(event.value)
            }

            is ProfileEvent.OnImageUrlChanged -> {
                onImageUrlChange(event.value)
            }

            is ProfileEvent.OnImagePathChanged -> {
                onImagePathChange(event.value)
            }

            is ProfileEvent.updateImagePath -> {
                updateProfileLocalImagePath(event.value)
            }
        }
    }

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        if (userId.isNotEmpty()) {
            profileRepository.getUser(userId)
                .catch { e ->
                    withContext(Dispatchers.Main) {
                        profileState = profileState.copy(
                            errorGetUser = "Terjadi kesalahan saat memuat data: ${e.localizedMessage}"
                        )
                    }
                }
                .collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { user ->
                                withContext(Dispatchers.Main) {
                                    setUserState(user)
                                    if (user.imageUrl.isNotBlank() && user.imageUrl.startsWith("https://") && user.localImagePath.isNullOrBlank()) {
                                        val imagePath = downloadAndSaveImageProfile(application, user.imageUrl, user.userId)
                                        updateProfileLocalImagePath(imagePath)
                                    }
                                }
                            }
                        }

                        is Resource.Error -> {
                            withContext(Dispatchers.Main) {
                                profileState = profileState.copy(
                                    errorGetUser = "Terjadi kesalahan saat memuat data: ${result.message}"
                                )
                            }
                        }
                        is Resource.Loading -> {}
                    }
                }
        }
    }

    private fun updateUserProfile() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            profileState = profileState.copy(updateError = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        profileState = profileState.copy(isLoading = true)

        if (!validateUpdateForm()) {
            profileState = profileState.copy(
                updateError = "Formulir ubah profil tidak boleh kosong",
                isLoading = false
            )
            return@launch
        }

        val user = profileState.toUser()
        when (val result = profileRepository.updateUser(user)) {
            is Resource.Success -> {
                withContext(Dispatchers.Main) {
                    if (user.imageUrl.isNotBlank() && !user.imageUrl.startsWith("https://")) {
                        loadUserData()
                        val imagePath = downloadAndSaveImageProfile(application, user.imageUrl, user.userId)
                        updateProfileLocalImagePath(imagePath)
                    }
                    profileState = profileState.copy(isSuccessUpdate = true)
                }
            }

            is Resource.Error -> {
                withContext(Dispatchers.Main) {
                    profileState = profileState.copy(updateError = result.message)
                }
            }
            is Resource.Loading -> {}
        }
        profileState = profileState.copy(isLoading = false)
    }

    private fun updateProfileLocalImagePath(localImagePath: String) = viewModelScope.launch(Dispatchers.IO) {
        if (userId.isNotEmpty()) {
            when (val result = profileRepository.updateProfileImage(userId, localImagePath)) {
                is Resource.Success -> {
                    withContext(Dispatchers.Main) {
                        profileState = profileState.copy(localImagePath = localImagePath)
                    }
                }
                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        throw Exception("Terjadi kesalahan saat memuat foto profile: ${result.message}")
                    }
                }
                else -> {}
            }
        }
    }

    private fun logout() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            profileState = profileState.copy(logoutError = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        profileState = profileState.copy(isLoading = true)
        try {
            val notificationPreferences = NotificationPreferences(application)
            notificationPreferences.clearNotifications(userId = userId)

            updateProfileLocalImagePath("")
            Log.d("ProfileVM", "logout state: $profileState")
            delay(500)

            profileRepository.logout()
            profileState = profileState.copy(isSuccessLogout = true)
        } catch (e: Exception) {
            profileState = profileState.copy(logoutError = e.localizedMessage)
        } finally {
            profileState = profileState.copy(isLoading = false)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun setUserState(user: User) {
        profileState = profileState.copy(
            userId = user.userId,
            email = user.email,
            name = user.name,
            bdate = user.bdate,
            gender = user.gender,
            address = user.address,
            phone = user.phone,
            puskesmas = user.puskesmas,
            role = user.role,
            imageUrl = user.imageUrl,
            localImagePath = user.localImagePath
        )
    }

    private fun resetStatus() {
        profileState = profileState.copy(
            isSuccessUpdate = false
        )
    }

    private fun clearToastMessage() {
        profileState = profileState.copy(
            updateError = null,
            logoutError = null
        )
    }

    // profile update functions
    private fun onNameChange(name: String) {
        profileState = profileState.copy(name = name)
        if (name.isBlank()) {
            profileState = profileState.copy(profileErrorName = "Nama tidak boleh kosong")
        } else {
            profileState = profileState.copy(profileErrorName = null)
        }
    }

    private fun onBdateChange(date: Date?) {
        profileState = profileState.copy(bdate = date)
        if (date == null) {
            profileState = profileState.copy(profileErrorBdate = "Tanggal lahir tidak boleh kosong")
        } else {
            profileState = profileState.copy(profileErrorBdate = null)
        }
    }

    private fun onAddressChange(address: String) {
        profileState = profileState.copy(address = address)
        if (address.isBlank()) {
            profileState = profileState.copy(profileErrorAddress = "Alamat tidak boleh kosong")
        } else {
            profileState = profileState.copy(profileErrorAddress = null)
        }
    }

    private fun onPhoneChange(phone: String) {
        profileState = profileState.copy(phone = phone)
        if (phone.isBlank()) {
            profileState = profileState.copy(profileErrorPhone = "Nomor telepon tidak boleh kosong")
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            profileState = profileState.copy(profileErrorPhone = "Format nomor telepon tidak valid")
        } else {
            profileState = profileState.copy(profileErrorPhone = null)
        }
    }

    private fun onImageUrlChange(imageUrl: String) {
        profileState = profileState.copy(imageUrl = imageUrl)
    }

    private fun onImagePathChange(imagePath: String) {
        profileState = profileState.copy(localImagePath = imagePath)
    }

    private fun validateUpdateForm(): Boolean {
        return profileState.profileErrorName == null &&
                profileState.profileErrorBdate == null &&
                profileState.profileErrorAddress == null &&
                profileState.profileErrorPhone == null &&
                profileState.name.isNotBlank() &&
                profileState.bdate != null &&
                profileState.address.isNotBlank() &&
                profileState.phone.isNotBlank()
    }
}
