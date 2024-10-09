/*
package com.project.tubocare.medication.domain.usecase

import com.project.tubocare.medication.domain.model.Medication
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class GetMedicationUseCase {
    fun execute(medications: List<Medication>): MedicationUseCase {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val currentTime = LocalTime.now()

        val todayMedications = mutableListOf<Pair<Medication, LocalTime>>()
        val tomorrowMedications = mutableListOf<Pair<Medication, LocalTime>>()
        val otherMedications = mutableMapOf<DayOfWeek, MutableList<Pair<Medication, LocalTime>>>()

        for (day in DayOfWeek.entries) {
            otherMedications[day] = mutableListOf()
        }

        medications.forEach { medication ->
            medication.weeklySchedule.forEach { (day, times) ->
                times.forEach { time ->
                    when (day) {
                        today.dayOfWeek -> todayMedications.add(Pair(medication, time))
                        tomorrow.dayOfWeek -> tomorrowMedications.add(Pair(medication, time))
                        else -> otherMedications[day]?.add(Pair(medication, time))
                    }
                }
            }
        }

        val nextMedication = todayMedications.filter { it.second.isAfter(currentTime) }
            .minByOrNull { it.second }

        return MedicationUseCase(
            todayMedications = todayMedications,
            tomorrowMedications = tomorrowMedications,
            nextMedication = nextMedication
        )
    }
}*/
