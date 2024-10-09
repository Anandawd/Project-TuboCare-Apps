package com.project.tubocare.weight.presentation.event

import com.project.tubocare.symptom.presentation.event.SymptomEvent
import java.time.LocalTime
import java.util.Date

sealed class WeightEvent{
    object GetWeights: WeightEvent()
    object AddWeight: WeightEvent()
    object UpdateWeight: WeightEvent()
    object ResetStatus: WeightEvent()
    object ClearToast: WeightEvent()
    data class GetWeightById(val value: String): WeightEvent()
    data class DeleteWeight(val value: String): WeightEvent()

    data class OnDateChanged(val value: Date?): WeightEvent()
    data class OnTimeChanged(val value: LocalTime?): WeightEvent()
    data class OnWeightChanged(val value: String): WeightEvent()
    data class OnHeightChanged(val value: String): WeightEvent()
}