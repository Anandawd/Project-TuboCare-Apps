package com.project.tubocare.medication.domain.model
import java.time.LocalDateTime
import java.time.LocalTime

data class Medication(
    val userId:  String,
    val medicationId: String,
    val name: String,
    val weeklySchedule: Map<String, List<ChecklistEntry>>,
    var frequency: String,
    val instruction: String,
    val remain: Int?,
    val dosage: Int?,
    val note: String,
    val image: String = "",
)
