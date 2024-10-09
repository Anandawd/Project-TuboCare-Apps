package com.project.tubocare.appointment.domain.repository

import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface  AppointmentRepository {
    fun hasUser(): Boolean
    fun getUserId(): String
    fun getAppointmentId(): String
    suspend fun getAppointments(userId: String): Flow<Resource<List<Appointment>>>
    suspend fun getAppointmentById(id: String) : Flow<Resource<Appointment?>>
    suspend fun addAppointment(appointment: Appointment): Resource<Unit>
    suspend fun updateAppointment(appointment: Appointment): Resource<Unit>
    suspend fun updateAppointmentCheckStatus(id: String, isChecked: Boolean): Resource<Unit>
    suspend fun deleteAppointment(id: String): Resource<Unit>
}