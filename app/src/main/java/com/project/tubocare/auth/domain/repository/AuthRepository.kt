package com.project.tubocare.auth.domain.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.core.util.Resource

interface AuthRepository {
    fun currentUser(): FirebaseUser?
    fun hasUser(): Boolean
    fun getUserId(): String
    suspend fun saveUser(user: User): Resource<Unit>
    suspend fun login(
        email: String,
        password: String,
    ): Resource<AuthResult>

    suspend fun register(
        user: User,
        password: String
    ): Resource<FirebaseUser>

    suspend fun isEmailAvailable(email: String): Boolean
    suspend fun resetPassword(email: String): Resource<Unit>
}