package com.project.tubocare.core.domain.usecase.app_entry

import com.project.tubocare.core.domain.repository.LocalUserManager

class SaveAppEntry(
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}