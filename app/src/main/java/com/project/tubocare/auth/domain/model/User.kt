package com.project.tubocare.auth.domain.model

import java.util.Date

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val puskesmas: String,
    val role: String,
    val bdate: Date?,
    val gender: String,
    val address: String,
    val phone: String,
    val imageUrl: String = "",
    var localImagePath: String? = null
)