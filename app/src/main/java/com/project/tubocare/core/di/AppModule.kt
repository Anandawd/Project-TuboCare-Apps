package com.project.tubocare.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.tubocare.appointment.data.repository.AppointmentRepositoryImpl
import com.project.tubocare.appointment.domain.repository.AppointmentRepository
import com.project.tubocare.article.data.local.ArticleDao
import com.project.tubocare.article.data.repository.ArticleRepositoryImpl
import com.project.tubocare.article.domain.repository.ArticleRepository
import com.project.tubocare.auth.data.local.UserDao
import com.project.tubocare.auth.data.repository.AuthRepositoryImpl
import com.project.tubocare.auth.domain.repository.AuthRepository
import com.project.tubocare.core.data.local.AppDatabase
import com.project.tubocare.core.data.repository.LocalUserManagerImpl
import com.project.tubocare.core.domain.repository.LocalUserManager
import com.project.tubocare.core.domain.usecase.app_entry.AppEntryUseCase
import com.project.tubocare.core.domain.usecase.app_entry.ReadAppEntry
import com.project.tubocare.core.domain.usecase.app_entry.SaveAppEntry
import com.project.tubocare.core.util.Constants.APP_DATABASE_NAME
import com.project.tubocare.healthcare.data.repository.HealthcareRepositoryImpl
import com.project.tubocare.healthcare.domain.repository.HealthcareRepository
import com.project.tubocare.medication.data.repository.MedicationRepositoryImpl
import com.project.tubocare.medication.domain.repository.MedicationRepository
import com.project.tubocare.profile.data.repository.ProfileRepositoryImpl
import com.project.tubocare.profile.domain.repository.ProfileRepository
import com.project.tubocare.symptom.data.repository.SymptomRepositoryImpl
import com.project.tubocare.symptom.domain.repository.SymptomRepository
import com.project.tubocare.weight.data.repository.WeightRepositoryImpl
import com.project.tubocare.weight.domain.repository.WeightRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(context = application)

    @Provides
    @Singleton
    fun provideAppEntryUseCase(
        localUserManager: LocalUserManager
    ) = AppEntryUseCase(
        saveAppEntry = SaveAppEntry(localUserManager),
        readAppEntry = ReadAppEntry(localUserManager)
    )

    // Auth
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        userDao: UserDao
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, firestore, userDao)

    @Provides
    @Singleton
    fun provideProfileRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage,
        userDao: UserDao
    ): ProfileRepository = ProfileRepositoryImpl(firebaseAuth, firestore, firebaseStorage, userDao)

    @Provides
    @Singleton
    fun provideMedicationRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ): MedicationRepository = MedicationRepositoryImpl(firebaseAuth, firestore, firebaseStorage)

    @Provides
    @Singleton
    fun provideAppointmentRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): AppointmentRepository = AppointmentRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideSymptomRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): SymptomRepository = SymptomRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideWeightRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): WeightRepository = WeightRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideArticleRepository(
        firestore: FirebaseFirestore,
        articleDao: ArticleDao
    ): ArticleRepository = ArticleRepositoryImpl(firestore, articleDao)

    @Provides
    @Singleton
    fun provideHealthcareRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): HealthcareRepository = HealthcareRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext application: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = AppDatabase::class.java,
            name = APP_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(
        appDatabase: AppDatabase
    ): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideArticleDao(
        appDatabase: AppDatabase
    ): ArticleDao = appDatabase.articleDao()
}

