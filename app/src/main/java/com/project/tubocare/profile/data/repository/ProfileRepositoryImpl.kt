package com.project.tubocare.profile.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.project.tubocare.auth.data.local.UserDao
import com.project.tubocare.auth.data.local.UserEntity
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.auth.util.Constants.USERS_COLLECTION
import com.project.tubocare.core.data.local.AppDatabase
import com.project.tubocare.core.util.Resource
import com.project.tubocare.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class ProfileRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val userDao: UserDao
) : ProfileRepository {
    override fun currentUser(): FirebaseUser? = firebaseAuth.currentUser

    override fun getUserId(): String = firebaseAuth.currentUser?.uid.orEmpty()

    override suspend fun getUser(userId: String): Flow<Resource<User?>> = flow {

        val localUserEntity = userDao.getUserById(userId)

        if (localUserEntity != null) {
            emit(Resource.Success(localUserEntity.toUser()))
        } else {
            val snapshot = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            val remoteUserEntity = snapshot.toObject(UserEntity::class.java)
            if (remoteUserEntity != null) {
                userDao.insertUser(remoteUserEntity)
                emit(Resource.Success(remoteUserEntity.toUser()))
            } else {
                emit(Resource.Error("Pengguna tidak ditemukan"))
            }
        }
    }.catch { e ->
        emit(Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak diketahui"))
    }

    override suspend fun updateUser(user: User): Resource<Unit> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("Pengguna belum login")
            var imageUrl = user.imageUrl

            if (user.imageUrl.isNotBlank() && !user.imageUrl.startsWith("https://")) {
                val fileUri = Uri.parse(user.imageUrl)
                val imageRef = firebaseStorage.reference.child("profile_images/$userId.jpg")
                imageRef.putFile(fileUri).await()
                imageUrl = imageRef.downloadUrl.await().toString()
            }

            val userEntity = UserEntity.fromUser(user.copy(imageUrl = imageUrl))

            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(userEntity)
                .await()

            userDao.insertUser(userEntity)

            Resource.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            Resource.Error("Kesalahan Penyimpanan: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan yang tidak diketahui: ${e.localizedMessage}")
        }
    }

    override suspend fun updateProfileImage(userId: String, imagePath: String): Resource<Unit> {
        return try {
            val userEntity = userDao.getUserById(userId)
            userEntity?.let {
                val updatedUser = it.copy(localImagePath = imagePath)
                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .set(updatedUser)
                    .await()
                userDao.insertUser(updatedUser)
            }
            Resource.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            Resource.Error("Kesalahan Penyimpanan: ${e.localizedMessage}")
        } catch (e: Exception) {
            Resource.Error("Terjadi kesalahan yang tidak diketahui: ${e.localizedMessage}")
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
