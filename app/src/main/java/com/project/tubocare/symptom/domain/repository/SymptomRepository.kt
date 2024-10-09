package com.project.tubocare.symptom.domain.repository

import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.core.util.Resource
import com.project.tubocare.symptom.domain.model.Symptom
import kotlinx.coroutines.flow.Flow

interface SymptomRepository {
    fun hasUser(): Boolean
    fun getUserId(): String
    fun getSymptomId(): String
    suspend fun getSymptoms(userId: String): Flow<Resource<List<Symptom>>>
    suspend fun getSymptomById(id: String) : Flow<Resource<Symptom?>>
    suspend fun addSymptom(symptom: Symptom): Resource<Unit>
    suspend fun updateSymptom(symptom: Symptom): Resource<Unit>
    suspend fun deleteSymptom(id: String): Resource<Unit>
}