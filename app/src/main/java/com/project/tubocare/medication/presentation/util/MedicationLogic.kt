package com.project.tubocare.medication.presentation.util

import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class MedicationData(
    val todayMedications: List<Pair<Medication, ChecklistEntry>>,
    val tomorrowMedications: List<Pair<Medication, ChecklistEntry>>,
    val nextMedication: Pair<Medication, ChecklistEntry>?,
    val otherMedications: Map<String, List<Pair<Medication, ChecklistEntry>>>,
)

fun processMedications(medications: List<Medication>): MedicationData {
    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)
    val currentTime = LocalTime.now()

    val todayMedications = mutableListOf<Pair<Medication, ChecklistEntry>>()
    val tomorrowMedications = mutableListOf<Pair<Medication, ChecklistEntry>>()
    val otherMedications = mutableMapOf<String, MutableList<Pair<Medication, ChecklistEntry>>>()

    DayOfWeek.entries.forEach { day ->
        val dayInIndonesian = getDayOfWeekInIndonesian(day)
        otherMedications[dayInIndonesian] = mutableListOf()
    }

    medications.forEach { medication ->
        medication.weeklySchedule.forEach { (day, checklistEntries) ->
            checklistEntries.forEach { checklistEntry ->
                when (day) {
                    getDayOfWeekInIndonesian(today.dayOfWeek) -> {
                        todayMedications.add(Pair(medication, checklistEntry))
                    }
                    getDayOfWeekInIndonesian(tomorrow.dayOfWeek) -> {
                        tomorrowMedications.add(Pair(medication, checklistEntry))
                    }
                    else -> otherMedications[day]?.add(Pair(medication, checklistEntry))
                }
            }
        }
    }

    val todayDayInIndonesian = getDayOfWeekInIndonesian(today.dayOfWeek)
    val tomorrowDayInIndonesian = getDayOfWeekInIndonesian(tomorrow.dayOfWeek)
    otherMedications.remove(todayDayInIndonesian)
    otherMedications.remove(tomorrowDayInIndonesian)

    val orderedDays = getOrderedDaysAfter(tomorrow.dayOfWeek)
    val sortedOtherMedications = orderedDays.associateWith { otherMedications[it] ?: emptyList() }

    val nextMedications = todayMedications.filter { it.second.time?.isAfter(currentTime) == true }
        .minByOrNull { it.second.time ?: LocalTime.MAX }

    return MedicationData(
        todayMedications = todayMedications,
        tomorrowMedications = tomorrowMedications,
        nextMedication = nextMedications,
        otherMedications = sortedOtherMedications
    )
}

data class MedicationsByDay(
    val medications: Map<String, List<Pair<Medication, ChecklistEntry>>>
)

fun groupMedicationsByDay(medications: List<Medication>): MedicationsByDay {
    val medicationsByDay = mutableMapOf<String, MutableList<Pair<Medication, ChecklistEntry>>>()

    // Inisialisasi map untuk setiap hari dalam seminggu
    DayOfWeek.entries.forEach { day ->
        val dayInIndonesian = getDayOfWeekInIndonesian(day)
        medicationsByDay[dayInIndonesian] = mutableListOf()
    }

    // Kelompokkan obat-obatan berdasarkan hari
    medications.forEach { medication ->
        medication.weeklySchedule.forEach { (day, checklistEntries) ->
            checklistEntries.forEach { checklistEntry ->
                medicationsByDay[day]?.add(Pair(medication, checklistEntry))
            }
        }
    }

    // Konversi map menjadi MedicationsByDay
    return MedicationsByDay(
        medications = medicationsByDay.mapValues { it.value.toList() }
    )
}

fun getOrderedDaysAfter(day: DayOfWeek): List<String> {
    val days = DayOfWeek.entries
    val startIndex = days.indexOf(day)
    val orderedDays = days.subList(startIndex + 1, days.size) + days.subList(0, startIndex + 1)
    return orderedDays.map { getDayOfWeekInIndonesian(it) }
}