package com.project.tubocare.medication.domain.model

import java.time.LocalDateTime
import java.time.LocalTime

data class ChecklistEntry(
    val time: LocalTime? = null,
    val checked: Boolean? = false,
    val timestamp: LocalDateTime? = null
)
