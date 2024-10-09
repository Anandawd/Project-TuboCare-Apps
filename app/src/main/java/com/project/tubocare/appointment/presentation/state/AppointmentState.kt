package com.project.tubocare.appointment.presentation.state

import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.core.util.Resource
import java.time.LocalTime
import java.util.Date

data class AppointmentState(
    val appointmentList: Resource<List<Appointment>> = Resource.Loading(),

    val appointmentId: String = "",
    val userId: String = "",
    val name: String = "",
    val date: Date? = null,
    val time: LocalTime? = null,
    val location: String = "",
    val note: String? = null,
    val isChecked: Boolean? = false,

    val isLoading: Boolean = false,
    val isLoadingDelete: Boolean = false,
    val isSuccessAdd: Boolean = false,
    val isSuccessGetAppointments: Boolean = false,
    val isSuccessGetDetail: Boolean = false,
    val isSuccessUpdate: Boolean = false,
    val isSuccessDelete: Boolean = false,

    val errorName: String? = null,
    val errorDate: String? = null,
    val errorTime: String? = null,
    val errorLocation: String? = null,

    val errorGetAppointments: String? = null,
    val errorGetDetail: String? = null,
    val errorAdd: String? = null,
    val errorUpdate: String? = null,
    val errorDelete: String? = null,
)

fun AppointmentState.toAppointment(): Appointment {
    return Appointment(
        appointmentId = appointmentId,
        userId = userId,
        name = name,
        date = date,
        time = time,
        location = location,
        note = note,
        done = isChecked
    )
}
