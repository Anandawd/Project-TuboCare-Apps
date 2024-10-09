package com.project.tubocare.symptom.domain.model

import java.time.LocalTime
import java.util.Date

data class Symptom(
    val symptomId: String,
    val userId: String,
    val name: String,
    val date: Date?,
    val time: LocalTime?,
    val note: String,
)