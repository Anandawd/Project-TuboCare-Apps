package com.project.tubocare.appointment.data.remote

import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.core.data.local.TimeDto
import java.util.Date

data class AppointmentDto(
    val appointmentId: String = "",
    val userId: String = "",
    val name: String = "",
    val date: Date? = null,
    val time: TimeDto? = null,
    val location: String = "",
    val note: String? = null,
    val done: Boolean? = false
) {
    fun toAppointment(): Appointment {
        return Appointment(
            appointmentId = appointmentId,
            userId = userId,
            name = name,
            date = date,
            time = time?.toLocalTime(),
            location = location,
            note = note,
            done = done
        )
    }

    companion object {
        fun fromAppointment(appointment: Appointment): AppointmentDto {
            return AppointmentDto(
                appointmentId = appointment.appointmentId,
                userId = appointment.userId,
                name = appointment.name,
                date = appointment.date,
                time = appointment.time?.let { TimeDto.fromLocalTime(it) },
                location = appointment.location,
                note = appointment.note,
                done = appointment.done
            )
        }
    }
}