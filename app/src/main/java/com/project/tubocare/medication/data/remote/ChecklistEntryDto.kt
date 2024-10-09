package com.project.tubocare.medication.data.remote

import com.google.firebase.Timestamp
import com.project.tubocare.core.data.local.TimeDto
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.presentation.util.toLocalDateTime
import com.project.tubocare.medication.presentation.util.toTimestamp

data class ChecklistEntryDto(
    val timeDto: TimeDto? = null,
    val checked: Boolean? = false,
    val timestamp: Timestamp? = null
) {

    fun toChecklistEntry(): ChecklistEntry {
        return ChecklistEntry(
            time = timeDto?.toLocalTime(),
            checked = checked,
            timestamp = timestamp?.toLocalDateTime()
        )
    }
    companion object{
        fun fromChecklistEntry(entry: ChecklistEntry): ChecklistEntryDto {
            return ChecklistEntryDto(
                timeDto = entry.time?.let { TimeDto.fromLocalTime(it) },
                checked = entry.checked,
                timestamp = entry.timestamp?.toTimestamp()
            )
        }
    }
}
