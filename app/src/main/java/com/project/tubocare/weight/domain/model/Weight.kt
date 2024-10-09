package com.project.tubocare.weight.domain.model

import com.project.tubocare.weight.presentation.util.generateWeightNote
import java.time.LocalTime
import java.util.Date

data class Weight(
    val weightId: String,
    val userId: String,
    val date: Date?,
    val time: LocalTime?,
    val weight: Int?,
    val height: Int?,
    val note: String?
)