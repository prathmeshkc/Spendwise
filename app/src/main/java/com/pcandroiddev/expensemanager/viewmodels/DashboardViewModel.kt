package com.pcandroiddev.expensemanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _transactionList = MutableStateFlow<NetworkResult<List<TransactionResponse>>>(
        NetworkResult.Success(
            emptyList()
        )
    )
    val transactionList: StateFlow<NetworkResult<List<TransactionResponse>>> get() = _transactionList

    init {
        getAllTransaction()
    }


    private fun getAllTransaction() = viewModelScope.launch {
        transactionRepository.getAllTransaction()
            .collect { result: NetworkResult<List<TransactionResponse>> ->
                _transactionList.value = result
            }
    }

    fun deleteToken() = viewModelScope.launch {
        tokenManager.deleteToken()
    }


}