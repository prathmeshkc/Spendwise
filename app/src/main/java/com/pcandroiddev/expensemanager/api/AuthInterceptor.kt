package com.pcandroiddev.expensemanager.api

import android.util.Log
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

const val TAG = "AuthInterceptor"


class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) : Interceptor {


    /* override fun intercept(chain: Interceptor.Chain): Response {
         val originalRequest = chain.request()
         val newRequest = originalRequest.newBuilder()


         val token: String? = runBlocking { tokenManager.getToken() }

         if (token != null) {
             newRequest.addHeader("Authorization", "Bearer $token")
             Log.d("AuthInterceptor", "intercept: $token")
             Log.d("AuthInterceptor", "intercept: ${newRequest.build()}")
             return chain.proceed(newRequest.build())
         }

         return chain.proceed(originalRequest)
     }*/


    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = runBlocking { tokenManager.getToken() }
        val request = chain.request()
        if (token != null) {
            val newRequest = request
                .newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            val response = chain.proceed(newRequest)

            return if (response.code() == 401) {
                response.close()
                refreshToken(chain, request)
            } else {
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


}
