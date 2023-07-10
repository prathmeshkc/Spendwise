package com.pcandroiddev.expensemanager.api

import android.util.Log
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()


        val token: String? = runBlocking { tokenManager.getToken() }

        if(token != null) {
            request.addHeader("Authorization", "Bearer $token")
            Log.d("AuthInterceptor", "intercept: $token")
            Log.d("AuthInterceptor", "intercept: ${request.build()}")
            return chain.proceed(request.build())
        }

        return chain.proceed(originalRequest)
    }
}
