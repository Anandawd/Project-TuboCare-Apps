package com.project.tubocare.appointment.presentation.event

import android.content.Context
import com.project.tubocare.medication.domain.model.Medication
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

sealed class AppointmentEvent {
    object GetAppointments: AppointmentEvent()
    object AddAppointment: AppointmentEvent()
    object UpdateAppointment: AppointmentEvent()
    object ResetStatus: AppointmentEvent()
    object ClearToast: AppointmentEvent()
    data class GetAppointmentById(val value: String): AppointmentEvent()
    data class DeleteAppointment(val value: String): AppointmentEvent()

    data class OnNameChanged(val value: String): AppointmentEvent()
    data class OnDateChanged(val value: Date?): AppointmentEvent()
    data class OnTimeChanged(val value: LocalTime?): AppointmentEvent()
    data class OnLocationChanged(val value: String): AppointmentEvent()
    data class OnNoteChanged(val value: String): AppointmentEvent()
    data class OnCheckChanged(val id: String, val value: Boolean): AppointmentEvent()
}