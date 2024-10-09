package com.project.tubocare.symptom.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.project.tubocare.auth.util.Constants.SYMPTOMS_COLLECTION
import com.project.tubocare.core.util.Resource
import com.project.tubocare.symptom.data.remote.SymptomDto
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.symptom.domain.repository.SymptomRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class SymptomRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): SymptomRepository {
    override fun hasUser(): Boolean = firebaseAuth.currentUser != null

    override fun getUserId(): String = firebaseAuth.currentUser?.uid.orEmpty()

    override fun getSymptomId(): String = firestore.collection(SYMPTOMS_COLLECTION).document().id

    override suspend fun getSymptoms(userId: String): Flow<Resource<List<Symptom>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null
        try {
            snapshotStateListener = firestore.collection(SYMPTOMS_COLLECTION)
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, error ->
                    val response = if (snapshot != null){
                        val symptoms = snapshot.toObjects(SymptomDto::class.java).map { it.toSymptom() }
                        Resource.Success(symptoms)
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

    override suspend fun getSymptomById(id: String): Flow<Resource<Symptom?>> = flow {
        try {
            val snapshot = firestore.collection(SYMPTOMS_COLLECTION)
                .document(id)
                .get()
                .await()

            val symptomDto = snapshot.toObject(SymptomDto::class.java)
            val symptom = symptomDto?.toSymptom()
            emit(Resource.Success(symptom))
        } catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga."))
        }
    }

    override suspend fun addSymptom(symptom: Symptom): Resource<Unit> {
        return try {
            val symptomDto = SymptomDto.fromSymptom(symptom)
            firestore.collection(SYMPTOMS_COLLECTION)
                .document(symptomDto.symptomId)
                .set(symptomDto)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun updateSymptom(symptom: Symptom): Resource<Unit> {
        return try {
            val symptomDto = SymptomDto.fromSymptom(symptom)
            firestore.collection(SYMPTOMS_COLLECTION)
                .document(symptomDto.symptomId)
                .set(symptomDto)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun deleteSymptom(id: String): Resource<Unit> {
        return try {
            firestore.collection(SYMPTOMS_COLLECTION)
                .document(id)
                .delete()
                .await()
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }
}