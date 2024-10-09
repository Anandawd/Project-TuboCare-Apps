package com.project.tubocare.symptom.data.remote

import com.project.tubocare.core.data.local.TimeDto
import com.project.tubocare.symptom.domain.model.Symptom
import java.util.Date

data class SymptomDto(
    val symptomId: String = "",
    val userId: String = "",
    val name: String = "",
    val date: Date? = null,
    val time: TimeDto? = null,
    val note: String = "",
) {
    fun toSymptom(): Symptom {
        return Symptom(
            symptomId = symptomId,
            userId = userId,
            name = name,
            date = date,
            time = time?.toLocalTime(),
            note = note
        )
    }

    companion object {
        fun fromSymptom(symptom: Symptom): SymptomDto {
            return SymptomDto(
                symptomId = symptom.symptomId,
                userId = symptom.userId,
                name = symptom.name,
                date = symptom.date,
                time = symptom.time?.let { TimeDto.fromLocalTime(it) },
                note = symptom.note
            )
        }
    }
}
