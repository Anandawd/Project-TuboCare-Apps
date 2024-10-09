package com.project.tubocare.core.di

import com.project.tubocare.auth.domain.repository.AuthRepository
import com.project.tubocare.auth.presentation.LoginViewModel
import com.project.tubocare.auth.presentation.RegisterViewModel
import com.project.tubocare.profile.domain.repository.ProfileRepository
import com.project.tubocare.profile.presentation.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
/*

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideLoginViewModel(
        authRepository: AuthRepository
    ): LoginViewModel {
        return LoginViewModel(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideRegisterViewModel(
        authRepository: AuthRepository
    ): RegisterViewModel {
        return RegisterViewModel(authRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideProfileViewModel(
        profileRepository: ProfileRepository,

    ): ProfileViewModel {
        return ProfileViewModel(profileRepository)
    }
}*/
