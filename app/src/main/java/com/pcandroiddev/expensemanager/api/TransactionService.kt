package com.pcandroiddev.expensemanager.api

import com.pcandroiddev.expensemanager.data.remote.TransactionRequest
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionService {

    @POST("transaction")
    suspend fun createTransaction(
        @Body transactionRequest: TransactionRequest
    ): Response<TransactionResponse>

    @GET("transaction")
    suspend fun getAllTransaction(): Response<List<TransactionResponse>>

    @PUT("transaction/{transactionId}")
    suspend fun updateTransaction(
        @Path("transactionId") transactionId: String,
        @Body transactionRequest: TransactionRequest
    ): Response<TransactionResponse>

    @DELETE("transaction/{transactionId}")
    suspend fun deleteTransaction(
        @Path("transactionId") transactionId: String
    ): Response<TransactionResponse>

    @GET("transaction/filter")
    suspend fun getAllTransactionBetweenDates(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<TransactionResponse>>

    @GET("transaction/search")
    suspend fun searchTransactionsByText(
        @Query("searchQuery") searchText: String
    ): Response<List<TransactionResponse>>

    @GET("transaction/search/type")
    suspend fun searchTransactionByTypeAndText(
        @Query("searchQuery") searchText: String,
        @Query("type") transactionType: String
    ): Response<List<TransactionResponse>>


}
