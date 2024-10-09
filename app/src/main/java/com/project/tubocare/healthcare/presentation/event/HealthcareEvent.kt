package com.project.tubocare.healthcare.presentation.event

import com.project.tubocare.profile.presentation.event.ProfileEvent
import java.util.Date

sealed class HealthcareEvent {
    data class GetPatientsByPuskemas(val value: String): HealthcareEvent()
    object getHealthcare: HealthcareEvent()
    object ClearToast: HealthcareEvent()

    data class GetPatientById(val value: String): HealthcareEvent()
    data class GetMedications(val value: String): HealthcareEvent()
    data class GetMedicationById(val value: String): HealthcareEvent()

    data class GetAppointments(val value: String): HealthcareEvent()

    data class GetSymptoms(val value: String): HealthcareEvent()

    data class GetWeights(val value: String): HealthcareEvent()
}