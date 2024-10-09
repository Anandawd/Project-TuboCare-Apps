package com.project.tubocare.core.domain.repository

import kotlinx.coroutines.flow.Flow


interface LocalUserManager {
    suspend fun saveAppEntry()

    fun readAppEntry(): Flow<Boolean>

    suspend fun saveCheckboxState(isChecked: Boolean)

    fun readCheckboxState(): Flow<Boolean>
}