package com.project.tubocare.medication.presentation.util.ResetChecklist

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.project.tubocare.medication.domain.repository.MedicationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDateTime

@HiltWorker
class ResetChecklistWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val medicationRepository: MedicationRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val userId = medicationRepository.getUserId()
            val medications = medicationRepository.getMedications(userId).firstOrNull()?.data ?: listOf()

            val updateMedications = medications.map { medication ->
                val updateWeeklySchedule = medication.weeklySchedule.mapValues { entry ->
                    entry.value.map { checklist ->
                        if (checklist.timestamp != null && checklist.timestamp.isBefore(LocalDateTime.now().minusDays(7))){
                            checklist.copy(checked = false, timestamp = null)
                        } else {
                            checklist
                        }
                    }
                }
                medication.copy(weeklySchedule = updateWeeklySchedule)
            }

            updateMedications.forEach { medication ->
                medicationRepository.updateMedication(medication)
            }

            Result.success()
        }catch (e: Exception){
            Result.failure()
        }
    }
}