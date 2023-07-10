package com.pcandroiddev.expensemanager.di

import com.pcandroiddev.expensemanager.api.AuthInterceptor
import com.pcandroiddev.expensemanager.api.TransactionService
import com.pcandroiddev.expensemanager.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesTransactionAPI(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): TransactionService {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(TransactionService::class.java)
    }

}
