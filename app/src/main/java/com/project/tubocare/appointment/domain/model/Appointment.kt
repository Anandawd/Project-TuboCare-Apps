package com.project.tubocare.appointment.domain.model

import java.time.LocalTime
import java.util.Date

data class Appointment(
    val appointmentId: String,
    val userId: String,
    val name: String,
    val date: Date?,
    val time: LocalTime?,
    val location: String,
    val note: String? = null,
    val done: Boolean? = false,
)