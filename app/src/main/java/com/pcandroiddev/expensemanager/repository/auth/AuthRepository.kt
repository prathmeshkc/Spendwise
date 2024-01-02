package com.pcandroiddev.expensemanager.repository.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.pcandroiddev.expensemanager.utils.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<ApiResult<AuthResult>>
    fun registerUser(email: String, password: String): Flow<ApiResult<AuthResult>>

//    fun googleSignIn(credential: AuthCredential): Flow<ApiResult<AuthResult>>
    fun logout()

    suspend fun getIdToken(): String?
}
