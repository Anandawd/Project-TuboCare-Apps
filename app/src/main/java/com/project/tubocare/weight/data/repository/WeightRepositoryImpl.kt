package com.project.tubocare.weight.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObjects
import com.project.tubocare.auth.util.Constants.SYMPTOMS_COLLECTION
import com.project.tubocare.auth.util.Constants.WEIGHTS_COLLECTION
import com.project.tubocare.core.util.Resource
import com.project.tubocare.symptom.data.remote.SymptomDto
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.weight.data.remote.WeightDto
import com.project.tubocare.weight.domain.model.Weight
import com.project.tubocare.weight.domain.repository.WeightRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class WeightRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): WeightRepository {
    override fun hasUser(): Boolean = firebaseAuth.currentUser != null

    override fun getUserId(): String = firebaseAuth.currentUser?.uid.orEmpty()

    override fun getWeightId(): String = firestore.collection(WEIGHTS_COLLECTION).document().id

    override suspend fun getWeights(userId: String): Flow<Resource<List<Weight>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null
        try {
            snapshotStateListener = firestore.collection(WEIGHTS_COLLECTION)
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, error ->
                    val response = if (snapshot != null){
                        val weights = snapshot.toObjects(WeightDto::class.java).map { it.toWeight() }
                        Resource.Success(weights)
                    } else {
                        Resource.Error(error?.message)
                    }
                    trySend(response)
                }
        } catch (e: Exception){
            trySend(Resource.Error(e.message))
        }
        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    override suspend fun getWeightById(id: String): Flow<Resource<Weight?>> = flow {
        try {
            val snapshot = firestore.collection(WEIGHTS_COLLECTION)
                .document(id)
                .get()
                .await()

            val weightDto = snapshot.toObject(WeightDto::class.java)
            val weight = weightDto?.toWeight()
            emit(Resource.Success(weight))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga."))
        }
    }

    override suspend fun addWeight(weight: Weight): Resource<Unit> {
        return try {
            val weightDto = WeightDto.fromWeight(weight)
            firestore.collection(WEIGHTS_COLLECTION)
                .document(weightDto.weightId)
                .set(weightDto)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun updateWeight(weight: Weight): Resource<Unit> {
        return try {
            val weightDto = WeightDto.fromWeight(weight)
            firestore.collection(WEIGHTS_COLLECTION)
                .document(weightDto.weightId)
                .set(weightDto)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun deleteWeight(id: String): Resource<Unit> {
        return try {
            firestore.collection(WEIGHTS_COLLECTION)
                .document(id)
                .delete()
                .await()
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }
}