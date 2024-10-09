package com.project.tubocare.weight.domain.repository

import com.project.tubocare.core.util.Resource
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.weight.domain.model.Weight
import kotlinx.coroutines.flow.Flow

interface WeightRepository {
    fun hasUser(): Boolean
    fun getUserId(): String
    fun getWeightId(): String
    suspend fun getWeights(userId: String): Flow<Resource<List<Weight>>>
    suspend fun getWeightById(id: String) : Flow<Resource<Weight?>>
    suspend fun addWeight(weight: Weight): Resource<Unit>
    suspend fun updateWeight(weight: Weight): Resource<Unit>
    suspend fun deleteWeight(id: String): Resource<Unit>
}