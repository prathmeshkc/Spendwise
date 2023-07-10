package com.pcandroiddev.expensemanager.repository.transaction

import android.util.Log
import com.pcandroiddev.expensemanager.api.TransactionService
import com.pcandroiddev.expensemanager.data.remote.TransactionRequest
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

private const val TAG = "TransactionRepository"

//TODO: Implement a TransactionRepository interface and make this TransactionRepositoryImpl. Get this class's instance from the AppModule

class TransactionRepository @Inject constructor(
    private val transactionService: TransactionService
) {


    suspend fun createTransaction(transactionRequest: TransactionRequest): Flow<NetworkResult<String>> {
        return flow {
            emit(NetworkResult.Loading())
            val response =
                transactionService.createTransaction(transactionRequest = transactionRequest)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(data = "Transaction Created!"))
                Log.d(TAG, "createTransaction: $response")
            } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("message")))
            } else {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("Something Went Wrong!")))
            }
        }
    }

    suspend fun getAllTransaction(): Flow<NetworkResult<List<TransactionResponse>>> {
        return flow {

            emit(NetworkResult.Loading())
            val response = transactionService.getAllTransaction()

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(data = response.body()!!))
                Log.d(TAG, "getAllTransaction: $response")
            } else if ((response.code() == 404 || response.code() == 500) && response.body() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("message")))
            } else {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("Something Went Wrong!")))
            }
        }
    }

    suspend fun getAllTransactionBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<NetworkResult<List<TransactionResponse>>> {
        return flow {
            emit(NetworkResult.Loading())

            val response = transactionService.getAllTransactionBetweenDates(
                startDate = startDate,
                endDate = endDate
            )

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(data = response.body()!!))
                Log.d(TAG, "getAllTransactionBetweenDates: $response")
            } else if ((response.code() == 404 || response.code() == 500) && response.body() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("message")))
            } else {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("Something Went Wrong!")))
            }

        }
    }

    suspend fun updateTransaction(
        transactionId: String,
        transactionRequest: TransactionRequest
    ): Flow<NetworkResult<String>> {
        return flow {
            emit(NetworkResult.Loading())

            val response = transactionService.updateTransaction(
                transactionId = transactionId,
                transactionRequest = transactionRequest
            )

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(data = "Transaction Updated!"))
                Log.d(TAG, "updateTransaction: $response")
            } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("message")))
            } else {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("Something Went Wrong!")))
            }
        }
    }

    suspend fun deleteTransaction(
        transactionId: String
    ): Flow<NetworkResult<String>> {
        return flow {
            emit(NetworkResult.Loading())

            val response = transactionService.deleteTransaction(transactionId = transactionId)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(data = "Transaction Deleted!"))
                Log.d(TAG, "deleteTransaction: $response")
            } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("message")))
            } else {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                emit(NetworkResult.Error(message = errorObj.getString("Something Went Wrong!")))
            }
        }
    }


}
