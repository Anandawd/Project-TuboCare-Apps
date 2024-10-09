package com.project.tubocare.healthcare.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.project.tubocare.auth.data.local.UserEntity
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.auth.util.Constants.USERS_COLLECTION
import com.project.tubocare.core.util.Resource
import com.project.tubocare.healthcare.domain.repository.HealthcareRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class HealthcareRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : HealthcareRepository {

    override fun getHealthcareId(): String = firebaseAuth.currentUser?.uid.orEmpty()

    override suspend fun getPatients(unit: String, role: String): Flow<Resource<List<User>>> =
        callbackFlow {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                snapshotStateListener = firestore.collection(USERS_COLLECTION)
                    .whereEqualTo("role", role)
                    .whereEqualTo("puskesmas", unit)
                    .addSnapshotListener { snapshot, error ->
                        val response = if (snapshot != null) {
                            val patients = snapshot.toObjects(UserEntity::class.java).map { it.toUser() }
                            Resource.Success(patients)
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
}