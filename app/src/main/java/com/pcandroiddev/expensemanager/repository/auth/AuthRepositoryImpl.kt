package com.pcandroiddev.expensemanager.repository.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.pcandroiddev.expensemanager.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun loginUser(email: String, password: String): Flow<NetworkResult<AuthResult>> {
        return flow {
            emit(NetworkResult.Loading())
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(NetworkResult.Success(data = authResult))
        }.catch {
            emit(NetworkResult.Error(message = it.message.toString()))
        }
    }

    override fun registerUser(email: String, password: String): Flow<NetworkResult<AuthResult>> {
        return flow {
            emit(NetworkResult.Loading())
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(NetworkResult.Success(data = authResult))
        }.catch {
            emit(NetworkResult.Error(message = it.message.toString()))
        }

    }

    override fun logout() {
        firebaseAuth.signOut()
    }


    /*override fun googleSignIn(credential: AuthCredential): Flow<NetworkResult<AuthResult>> {
        TODO("Not yet implemented")
    }*/
}
