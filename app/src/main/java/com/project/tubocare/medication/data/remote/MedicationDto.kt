import com.project.tubocare.medication.data.remote.ChecklistEntryDto
import com.project.tubocare.medication.domain.model.Medication

data class MedicationDto(
    val medicationId: String = "",
    val userId: String = "",
    val name: String = "",
    val weeklySchedule: Map<String, List<ChecklistEntryDto>> = emptyMap(),
    var frequency: String = "",
    val instruction: String = "",
    val remain: Int? = null,
    val dosage: Int? = null,
    val note: String = "",
    val image: String = "",
) {
    fun toMedication(): Medication {
        return Medication(
            userId = userId,
            medicationId = medicationId,
            name = name,
            weeklySchedule = weeklySchedule.mapValues { entry ->
                entry.value.map { it.toChecklistEntry() }
            },
            frequency = frequency,
            instruction = instruction,
            remain = remain,
            dosage = dosage,
            note = note,
            image = image,
        )
    }

    companion object {
        fun fromMedication(medication: Medication): MedicationDto {
            return MedicationDto(
                userId = medication.userId,
                medicationId = medication.medicationId,
                name = medication.name,
                weeklySchedule = medication.weeklySchedule.mapValues { entry ->
                    entry.value.map { ChecklistEntryDto.fromChecklistEntry(it) }
                },
                frequency = medication.frequency,
                instruction = medication.instruction,
                remain = medication.remain,
                dosage = medication.dosage,
                note = medication.note,
                image = medication.image,
            )
        }
    }
}

