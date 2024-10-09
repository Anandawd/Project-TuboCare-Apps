package com.project.tubocare.profile.presentation.state

import com.project.tubocare.auth.domain.model.User
import java.util.Date

data class ProfileState(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val bdate: Date? = null,
    val gender: String = "",
    val address: String = "",
    val phone: String = "",
    val puskesmas: String = "",
    val role: String = "",
    val imageUrl: String = "",
    val localImagePath: String? = null,

    val isLoading: Boolean = false,
    val isSuccessUpdate: Boolean = false,
    val isSuccessLogout: Boolean = false,

    val profileErrorName: String? = null,
    val profileErrorBdate: String? = null,
    val profileErrorAddress: String? = null,
    val profileErrorPhone: String? = null,

    val errorGetUser: String? = null,
    val updateError: String? = null,
    val logoutError: String? = null,
)

fun ProfileState.toUser(): User {
    return User(
        userId = userId,
        email = email,
        name = name,
        bdate = bdate,
        gender = gender,
        address = address,
        phone = phone,
        puskesmas = puskesmas,
        role = role,
        imageUrl = imageUrl,
        localImagePath = localImagePath
    )
}
