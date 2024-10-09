package com.project.tubocare.profile.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun currentUser(): FirebaseUser?
    fun getUserId(): String
    suspend fun getUser(userId: String): Flow<Resource<User?>>
    suspend fun updateUser(user: User): Resource<Unit>
    suspend fun updateProfileImage(userId: String, imagePath: String): Resource<Unit>
    fun logout()
}