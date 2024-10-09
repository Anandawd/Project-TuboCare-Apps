package com.project.tubocare.medication.presentation.util

import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import java.time.LocalTime

fun getUniqueTimeMedication(
    medications: List<Medication>
): List<Pair<Medication, ChecklistEntry>> {
    val uniqueTimeMedication = mutableListOf<Pair<Medication, ChecklistEntry>>()
    medications.forEach { medication ->
        medication.weeklySchedule.forEach { (_, times) ->
            times.forEach { time ->
                uniqueTimeMedication.add(Pair(medication, time))
            }
        }
    }
    return uniqueTimeMedication
}