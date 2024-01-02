package com.pcandroiddev.expensemanager.repository.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.pcandroiddev.expensemanager.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun loginUser(email: String, password: String): Flow<ApiResult<AuthResult>> {
        return flow {
            emit(ApiResult.Loading())
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(ApiResult.Success(data = authResult))
        }.catch {
            emit(ApiResult.Error(message = it.message.toString()))
        }
    }

    override fun registerUser(email: String, password: String): Flow<ApiResult<AuthResult>> {
        return flow {
            emit(ApiResult.Loading())
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(ApiResult.Success(data = authResult))
        }.catch {
            emit(ApiResult.Error(message = it.message.toString()))
        }

    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun getIdToken(): String? {
        val tokenResult = firebaseAuth.currentUser?.getIdToken(true)?.await()
        return tokenResult?.token
    }

    /*override fun googleSignIn(credential: AuthCredential): Flow<ApiResult<AuthResult>> {
        TODO("Not yet implemented")
    }*/
}
