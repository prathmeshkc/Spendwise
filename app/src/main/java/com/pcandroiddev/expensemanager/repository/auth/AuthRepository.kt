package com.pcandroiddev.expensemanager.repository.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.pcandroiddev.expensemanager.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<NetworkResult<AuthResult>>
    fun registerUser(email: String, password: String): Flow<NetworkResult<AuthResult>>

//    fun googleSignIn(credential: AuthCredential): Flow<NetworkResult<AuthResult>>
}
