package com.pcandroiddev.expensemanager.repository.transaction

import android.util.Log
import com.pcandroiddev.expensemanager.api.TransactionService
import com.pcandroiddev.expensemanager.data.remote.TransactionRequest
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.utils.ApiResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

private const val TAG = "TransactionRepository"

//TODO: Implement a TransactionRepository interface and make this TransactionRepositoryImpl. Get this class's instance from the AppModule

class TransactionRepository @Inject constructor(
    private val transactionService: TransactionService
) {


    suspend fun createTransaction(transactionRequest: TransactionRequest): Flow<ApiResult<String>> {
        return flow {
            try {
                emit(ApiResult.Loading())
                val response =
                    transactionService.createTransaction(transactionRequest = transactionRequest)
                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(data = "Transaction Created!"))
                    Log.d(TAG, "createTransaction: $response")
                } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("message")))
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("Something Went Wrong!")))
                }
            } catch (exception: JSONException) {
                emit(ApiResult.Error(message = "Something Went Wrong!"))
            }
        }
    }

    suspend fun getAllTransaction(): Flow<ApiResult<List<TransactionResponse>>> {
        return flow {

            try {
                emit(ApiResult.Loading())
                val response = transactionService.getAllTransaction()

                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(data = response.body()!!))
                    Log.d(TAG, "getAllTransaction: $response")
                } else if ((response.code() == 404 || response.code() == 500) && response.body() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("message")))
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("Something Went Wrong!")))
                }
            } catch (exception: JSONException) {
                emit(ApiResult.Error(message = "Something Went Wrong!"))
            }
        }
    }

    suspend fun getAllTransactionBetweenDates(
        startDate: String,
        endDate: String
    ): Flow<ApiResult<List<TransactionResponse>>> {
        return flow {
            try {
                emit(ApiResult.Loading())

                val response = transactionService.getAllTransactionBetweenDates(
                    startDate = startDate,
                    endDate = endDate
                )

                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(data = response.body()!!))
                    Log.d(TAG, "getAllTransactionBetweenDates: $response")
                } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("message")))
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("Something Went Wrong!")))
                }
            } catch (exception: JSONException) {
                emit(ApiResult.Error(message = "Something Went Wrong!"))
            }

        }
    }

    suspend fun searchTransactionsByText(searchText: String): Flow<ApiResult<List<TransactionResponse>>> {
        return flow {
            try {
                emit(ApiResult.Loading())

                val response = transactionService.searchTransactionsByText(searchText)

                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(data = response.body()!!))
                    Log.d(TAG, "searchTransactionsByText: $response")
                } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("message")))
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("Something Went Wrong!")))
                }
            } catch (exception: JSONException) {
                emit(ApiResult.Error(message = "Something Went Wrong!"))
            }
        }
    }

    suspend fun searchTransactionByTypeAndText(
        searchText: String,
        transactionType: String
    ): Flow<ApiResult<List<TransactionResponse>>> {
        return flow {
            try {
                emit(ApiResult.Loading())

                val response = transactionService.searchTransactionByTypeAndText(
                    searchText = searchText,
                    transactionType = transactionType
                )

                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(data = response.body()!!))
                    Log.d(TAG, "searchTransactionByTypeAndText: $response")
                } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("message")))
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("Something Went Wrong!")))
                }
            } catch (exception: JSONException) {
                emit(ApiResult.Error(message = "Something Went Wrong!"))
            }
        }
    }

    suspend fun updateTransaction(
        transactionId: String,
        transactionRequest: TransactionRequest
    ): Flow<ApiResult<String>> {
        return flow {
            try {
                emit(ApiResult.Loading())

                val response = transactionService.updateTransaction(
                    transactionId = transactionId,
                    transactionRequest = transactionRequest
                )

                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(data = "Transaction Updated!"))
                    Log.d(TAG, "updateTransaction: $response")
                } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("message")))
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("Something Went Wrong!")))
                }
            } catch (exception: JSONException) {
                emit(ApiResult.Error(message = "Something Went Wrong!"))
            }
        }
    }

    suspend fun deleteTransaction(
        transactionId: String
    ): Flow<ApiResult<String>> {
        return flow {
            try {
                emit(ApiResult.Loading())

                val response = transactionService.deleteTransaction(transactionId = transactionId)

                if (response.isSuccessful && response.body() != null) {
                    emit(ApiResult.Success(data = "Transaction Deleted!"))
                    Log.d(TAG, "deleteTransaction: $response")
                } else if ((response.code() == 400 || response.code() == 500) && response.body() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("message")))
                } else {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    emit(ApiResult.Error(message = errorObj.getString("Something Went Wrong!")))
                }
            } catch (exception: JSONException) {
                emit(ApiResult.Error(message = "Something Went Wrong!"))
            }
        }
    }


}
