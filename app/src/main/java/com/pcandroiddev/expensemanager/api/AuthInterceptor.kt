package com.pcandroiddev.expensemanager.api

import android.util.Log
import com.auth0.android.jwt.JWT
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.time.Instant
import javax.inject.Inject

const val TAG = "AuthInterceptor"


class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = runBlocking { tokenManager.getToken() }
        val request = chain.request()
        if (token != null) {
            val jwt = JWT(token)
            val exp = jwt.claims["exp"]
            return if (isTokenExpired(exp?.asLong()!!)) {
                Log.d("AuthInterceptor", "exp: true")
                refreshToken(chain, request)
            } else {
                val newRequest = request
                    .newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()

                val response = chain.proceed(newRequest)
                Log.d("AuthInterceptor", "intercept: $token")
                response
            }

        } else {
            return refreshToken(chain, request)
        }
    }

    private fun refreshToken(chain: Interceptor.Chain, request: Request): Response {
        val newToken: String? = runBlocking { authRepository.getIdToken() }

        return if (!newToken.isNullOrEmpty()) {
            runBlocking {
                tokenManager.deleteToken()
                tokenManager.saveToken(newToken)
            }
            val newRequest = request
                .newBuilder()
                .addHeader("Authorization", "Bearer $newToken")
                .build()
            Log.d("AuthInterceptor", "intercept: $newToken")
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }

    private fun isTokenExpired(expirationTime: Long): Boolean {
        val currentTime = Instant.now().epochSecond
        return currentTime > expirationTime
    }


}
