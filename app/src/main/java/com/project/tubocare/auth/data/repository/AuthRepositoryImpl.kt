package com.project.tubocare.auth.data.repository

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.project.tubocare.auth.data.local.UserDao
import com.project.tubocare.auth.data.local.UserEntity
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.auth.domain.repository.AuthRepository
import com.project.tubocare.auth.util.Constants.USERS_COLLECTION
import com.project.tubocare.core.util.Resource
import kotlinx.coroutines.tasks.await


class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) : AuthRepository {
    override fun currentUser(): FirebaseUser? = firebaseAuth.currentUser
    override fun hasUser(): Boolean = firebaseAuth.currentUser != null
    override fun getUserId(): String = firebaseAuth.currentUser?.uid.orEmpty()

    override suspend fun saveUser(user: User): Resource<Unit> {
        return try {
            val userEntity = UserEntity.fromUser(user)
            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("Pengguna belum login.")

            /* simpan ke remote database */
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(userEntity)
                .await()

            /* simpan ke local database */
            userDao.insertUser(UserEntity.fromUser(user))

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Resource<AuthResult> {
        return try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            Resource.Success(result)
        } catch (e: FirebaseAuthInvalidUserException) {
            Resource.Error("Email tidak terdaftar.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Resource.Error("Email atau password yang Anda masukkan salah.")
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga.")
        }
    }

    override suspend fun register(
        user: User,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(user.email, password)
                .await()
            val userResult = authResult.user ?: throw Exception("Pendaftaran pengguna gagal")
            val userId = userResult.uid

            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(user)
                .await()

            Resource.Success(userResult)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga")
        }
    }

    override suspend fun isEmailAvailable(email: String): Boolean {
        return try {
            val querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("email", email)
                .get()
                .await()
            querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun resetPassword(email: String): Resource<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan yang tidak terduga")
        }
    }
}