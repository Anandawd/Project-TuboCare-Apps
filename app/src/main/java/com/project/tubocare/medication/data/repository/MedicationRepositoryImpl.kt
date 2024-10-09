package com.project.tubocare.medication.data.repository

import MedicationDto
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.project.tubocare.auth.util.Constants.MEDICATIONS_COLLECTION
import com.project.tubocare.core.util.Resource
import com.project.tubocare.medication.data.remote.ChecklistEntryDto
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.domain.repository.MedicationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait


class MedicationRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : MedicationRepository {
    override fun hasUser(): Boolean = firebaseAuth.currentUser != null

    override fun currentUser(): FirebaseUser? = firebaseAuth.currentUser

    override fun getUserId(): String = firebaseAuth.currentUser?.uid.orEmpty()

    override fun getMedicationId(): String = firestore.collection(MEDICATIONS_COLLECTION).document().id

    override suspend fun getMedications(userId: String): Flow<Resource<List<Medication>>> =
        callbackFlow {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                snapshotStateListener = firestore.collection(MEDICATIONS_COLLECTION)
                    .whereEqualTo("userId", userId)
                    .addSnapshotListener { snapshot, error ->
                        val response = if (snapshot != null) {
                            val medications = snapshot.toObjects(MedicationDto::class.java).map {
                                Log.d("MedicationRepository", "Received MedicationDto: $it")
                                it.toMedication()
                            }
                            Resource.Success(medications)
                        } else {
                            Resource.Error(error?.message)
                        }
                        trySend(response)
                    }

            } catch (e: Exception) {
                trySend(Resource.Error(e.message))
            }
            awaitClose {
                snapshotStateListener?.remove()
            }
        }

    override suspend fun getMedicationById(medicationId: String): Flow<Resource<Medication?>> = flow {
        try {
            val snapshot = firestore.collection(MEDICATIONS_COLLECTION)
                .document(medicationId)
                .get()
                .await()
            val medicationDto = snapshot.toObject(MedicationDto::class.java)
            val medication = medicationDto?.toMedication()
            emit(Resource.Success(medication))
        } catch (e: Exception){
           emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga."))
        }
    }

    override suspend fun addMedication(medication: Medication): Resource<Unit> {
        return try {
            val medicationDto = MedicationDto.fromMedication(medication)
            firestore.collection(MEDICATIONS_COLLECTION)
                .document(medicationDto.medicationId)
                .set(medicationDto)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun updateMedication(medication: Medication): Resource<Unit> {
        return try {
            val medicationDto = MedicationDto.fromMedication(medication)

            firestore.collection(MEDICATIONS_COLLECTION)
                .document(medicationDto.medicationId)
                .set(medicationDto)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun deleteMedication(medicationId: String): Resource<Unit> {
        return try {
            firestore.collection(MEDICATIONS_COLLECTION)
                .document(medicationId)
                .delete()
                .await()

            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun uploadImage(imageUri: Uri): Resource<String> {
        return try {
            val storageRef = firebaseStorage.reference
            val imageRef = storageRef.child("medication_images/${imageUri.lastPathSegment}")
            imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await().toString()
            Resource.Success(downloadUrl)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan saat mengunggah gambar.")
        }
    }

    override suspend fun updateChecklistEntry(
        medicationId: String,
        day: String,
        updateChecklist: List<ChecklistEntry>
    ): Resource<Unit> {
        return try {
            val snapshot = firestore.collection(MEDICATIONS_COLLECTION).document(medicationId)
            val updateMap = mapOf("weeklySchedule.$day" to updateChecklist.map { ChecklistEntryDto.fromChecklistEntry(it) })
            snapshot.update(updateMap).await()

            Resource.Success(Unit)

        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan saat memperbarui data ceklis.")
        }
    }
}

/*
class MedicationRepositoryImpl(
    private val firestore: FirebaseFirestore
) : MedicationRepository {
    override fun getMedications(patientId: String): Flow<Resource<List<Medication>>> = flow {
//        emit(Resource.Loading)
        try {
            val snapshot = firestore.collection("medications")
                .whereEqualTo("patientId", patientId)
                .get()
                .await()
            val medications = snapshot.documents.map { document ->
                document.toObject(MedicationEntity::class.java)?.toMedication()
                    ?: throw Exception("Invalid medication data")
            }
            emit(Resource.Success(medications))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun addMedication(medication: Medication): Resource<Unit> {
        return try {
            val medicationEntity = MedicationEntity.fromMedication(medication)
            firestore.collection("medications")
                .document(medicationEntity.id)
                .set(medicationEntity)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun updateMedication(medication: Medication): Resource<Unit> {
        return try {
            val medicationEntity = MedicationEntity.fromMedication(medication)
            firestore.collection("medications")
                .document(medicationEntity.id)
                .set(medicationEntity)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun deleteMedication(medicationId: String): Resource<Unit> {
        return try {
            firestore.collection("medications")
                .document(medicationId)
                .delete()
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}*/
