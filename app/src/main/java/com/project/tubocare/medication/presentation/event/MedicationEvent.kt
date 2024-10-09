package com.project.tubocare.medication.presentation.event

import android.content.Context
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime

sealed class MedicationEvent {
    object GetMedications: MedicationEvent()
    object GetUserName: MedicationEvent()
    object AddMedication: MedicationEvent()
    object ResetStatus: MedicationEvent()
    object ClearToast: MedicationEvent()
    object UpdateMedication: MedicationEvent()
    data class ScheduleAlarms(val value: Medication): MedicationEvent()
    data class GetMedicationById(val value: String): MedicationEvent()
    data class DeleteMedication(val value: Medication?): MedicationEvent()
    data class UploadImage(val value: String): MedicationEvent()
    data class UpdateChecklistEntry(val medicationId: String, val day: String, val updatedChecklist: List<ChecklistEntry>): MedicationEvent()

    data class OnNameChanged(val value: String): MedicationEvent()
    data class OnScheduleChanged(val value: Map<String, List<ChecklistEntry>>): MedicationEvent()
    data class OnFrequencyChanged(val value: String): MedicationEvent()
    data class OnInstructionChanged(val value: String): MedicationEvent()
    data class OnRemainChanged(val value: Int): MedicationEvent()
    data class OnDosageChanged(val value: Int): MedicationEvent()
    data class OnNoteChanged(val value: String): MedicationEvent()
    data class OnImageChanged(val value: String): MedicationEvent()
    data class OnTempTimesChanged(val value: List<LocalTime?>): MedicationEvent()
    data class SelectMedication(val data: Medication): MedicationEvent()
}