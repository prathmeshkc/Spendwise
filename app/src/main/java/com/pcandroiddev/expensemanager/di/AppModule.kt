package com.pcandroiddev.expensemanager.di

import com.google.firebase.auth.FirebaseAuth
import com.pcandroiddev.expensemanager.BuildConfig
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import com.pcandroiddev.expensemanager.repository.auth.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun providesFirebaseClientId(): String = BuildConfig.CLIENT_ID

    @Provides
    @Singleton
    fun providesAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(firebaseAuth = firebaseAuth)

}