package com.project.tubocare.medication.domain.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.project.tubocare.core.util.Resource
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun hasUser(): Boolean
    fun currentUser(): FirebaseUser?
    fun getUserId(): String
    fun getMedicationId(): String
    suspend fun getMedications(userId: String): Flow<Resource<List<Medication>>>
    suspend fun getMedicationById(medicationId: String) : Flow<Resource<Medication?>>
    suspend fun addMedication(medication: Medication): Resource<Unit>
    suspend fun updateMedication(medication: Medication): Resource<Unit>
    suspend fun deleteMedication(medicationId: String): Resource<Unit>
    suspend fun uploadImage(imageUri: Uri): Resource<String>
    suspend fun updateChecklistEntry(medicationId: String, day: String, updateChecklist: List<ChecklistEntry>): Resource<Unit>
}
