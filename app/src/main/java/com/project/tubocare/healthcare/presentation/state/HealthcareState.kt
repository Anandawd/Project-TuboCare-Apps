package com.project.tubocare.healthcare.presentation.state

import com.project.tubocare.auth.domain.model.User
import java.util.Date

data class  HealthcareState(
    val patients: List<User> = emptyList(),

    val name: String = "",
    val puskesmas: String = "",

    val isLoading: Boolean = false,

    val errorGetHealthcare: String? = null,
    val errorGetPatients: String? = null,
    val errorGetDetailPatient: String? = null,
)
