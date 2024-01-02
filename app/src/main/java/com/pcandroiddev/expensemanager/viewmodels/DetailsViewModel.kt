package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.ui.states.ResultState
import com.pcandroiddev.expensemanager.ui.uievents.TransactionDetailsUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _deleteTransactionState = Channel<ResultState>()
    val deleteTransactionState = _deleteTransactionState.receiveAsFlow()

    fun onEventChange(event: TransactionDetailsUIEvent) {
        when (event) {
            is TransactionDetailsUIEvent.DeleteTransactionButtonClicked -> {
                deleteTransaction(event.transactionId)
            }
        }
    }


    private fun deleteTransaction(transactionId: String) {
        Log.d(TAG, "deleteTransaction")
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.deleteTransaction(transactionId = transactionId)
                .collect { deleteTransactionResult: ApiResult<String> ->
                    when (deleteTransactionResult) {
                        is ApiResult.Loading -> {
                            _deleteTransactionState.send(ResultState(isLoading = true))
                        }

                        is ApiResult.Success -> {
                            _deleteTransactionState.send(ResultState(isSuccess = deleteTransactionResult.data))
                        }

                        is ApiResult.Error -> {
                            _deleteTransactionState.send(ResultState(isError = deleteTransactionResult.message))
                        }
                    }

                }
        }
    }

    companion object {
        const val TAG = "DetailsViewModel"
    }

}
