package com.project.tubocare.healthcare.domain.repository

import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.core.util.Resource
import com.project.tubocare.medication.domain.model.Medication
import kotlinx.coroutines.flow.Flow

interface HealthcareRepository {
    fun getHealthcareId(): String
    suspend fun getPatients(unit: String, role: String): Flow<Resource<List<User>>>
}