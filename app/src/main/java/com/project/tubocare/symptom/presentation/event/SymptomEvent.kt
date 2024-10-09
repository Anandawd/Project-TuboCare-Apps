package com.project.tubocare.symptom.presentation.event

import java.time.LocalTime
import java.util.Date

sealed class SymptomEvent {
    object GetSymptoms: SymptomEvent()
    object AddSymptom: SymptomEvent()
    object UpdateSymptom: SymptomEvent()
    object ResetStatus: SymptomEvent()
    object ClearToast: SymptomEvent()
    data class GetSymptomById(val value: String): SymptomEvent()
    data class DeleteSymptom(val value: String): SymptomEvent()

    data class OnNameChanged(val value: String): SymptomEvent()
    data class OnDateChanged(val value: Date?): SymptomEvent()
    data class OnTimeChanged(val value: LocalTime?): SymptomEvent()
    data class OnNoteChanged(val value: String): SymptomEvent()
}