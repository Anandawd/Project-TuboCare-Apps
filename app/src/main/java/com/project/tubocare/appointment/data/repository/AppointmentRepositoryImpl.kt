package com.project.tubocare.appointment.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.project.tubocare.appointment.data.remote.AppointmentDto
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.appointment.domain.repository.AppointmentRepository
import com.project.tubocare.auth.util.Constants.APPOINTMENTS_COLLECTION
import com.project.tubocare.core.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AppointmentRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AppointmentRepository {
    override fun hasUser(): Boolean = firebaseAuth.currentUser != null

    override fun getUserId(): String = firebaseAuth.currentUser?.uid.orEmpty()

    override fun getAppointmentId(): String = firestore.collection(APPOINTMENTS_COLLECTION).document().id

    override suspend fun getAppointments(userId: String): Flow<Resource<List<Appointment>>> =
        callbackFlow {
            var snapshotStateListener: ListenerRegistration? = null
            try {
                snapshotStateListener = firestore.collection(APPOINTMENTS_COLLECTION)
                    .whereEqualTo("userId", userId)
                    .addSnapshotListener { snapshot, error ->
                        val response = if (snapshot != null) {
                            val appointments = snapshot.toObjects(AppointmentDto::class.java).map { it.toAppointment() }
                            Resource.Success(appointments)
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


    override suspend fun getAppointmentById(id: String): Flow<Resource<Appointment?>> = flow {
        try {
            val snapshot = firestore.collection(APPOINTMENTS_COLLECTION)
                .document(id)
                .get()
                .await()

            val appointmentDto = snapshot.toObject(AppointmentDto::class.java)
            val appointment = appointmentDto?.toAppointment()

            emit(Resource.Success(appointment))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga."))
        }
    }

    override suspend fun addAppointment(appointment: Appointment): Resource<Unit> {
        return try {
            val appointmentDto = AppointmentDto.fromAppointment(appointment)
            firestore.collection(APPOINTMENTS_COLLECTION)
                .document(appointmentDto.appointmentId)
                .set(appointmentDto)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun updateAppointment(appointment: Appointment): Resource<Unit> {
        return try {
            val appointmentDto = AppointmentDto.fromAppointment(appointment)

            firestore.collection(APPOINTMENTS_COLLECTION)
                .document(appointmentDto.appointmentId)
                .set(appointmentDto)
                .await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun updateAppointmentCheckStatus(
        id: String,
        isChecked: Boolean
    ): Resource<Unit> {
        return try {
            firestore.collection(APPOINTMENTS_COLLECTION)
                .document(id)
                .update("done", isChecked)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun deleteAppointment(id: String): Resource<Unit> {
        return try {
            firestore.collection(APPOINTMENTS_COLLECTION)
                .document(id)
                .delete()
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }
}