package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.data.local.SearchFilters
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.ui.SearchUIState
import com.pcandroiddev.expensemanager.ui.uievents.SearchTransactionUIEvent
import com.pcandroiddev.expensemanager.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _transactionList = MutableStateFlow<NetworkResult<List<TransactionResponse>>>(
        NetworkResult.Success(
            emptyList()
        )
    )
    val transactionList: StateFlow<NetworkResult<List<TransactionResponse>>> get() = _transactionList

    private val _searchedTransactionList =
        MutableStateFlow<NetworkResult<List<TransactionResponse>>>(NetworkResult.Success(emptyList()))
    val searchedTransactionList: StateFlow<NetworkResult<List<TransactionResponse>>> get() = _searchedTransactionList

    var searchTransactionUIState = mutableStateOf(SearchUIState())

    private val allSearchValidationPassed = mutableStateOf(false)

    private var searchJob: Job? = null

    init {
        getAllTransaction()
    }


    private fun getAllTransaction() = viewModelScope.launch {
        transactionRepository.getAllTransaction()
            .collect { result: NetworkResult<List<TransactionResponse>> ->
                _transactionList.value = result
            }
    }


    fun onEventChange(event: SearchTransactionUIEvent) {
        when (event) {
            is SearchTransactionUIEvent.SearchFilterChanged -> {
                searchTransactionUIState.value =
                    searchTransactionUIState.value.copy(filterType = event.searchFilter)
                printState()
            }

            is SearchTransactionUIEvent.SearchTextChanged -> {
                searchTransactionUIState.value =
                    searchTransactionUIState.value.copy(searchText = event.searchText)

                /*if (allSearchValidationPassed.value) {
                    searchTransactions()
                }*/
                printState()
            }

            SearchTransactionUIEvent.SearchTransactionButtonClicked -> {
                /* if (allSearchValidationPassed.value) {
                     searchTransactions()
                 }
                 printState()*/
            }
        }
        validateDataWithRules()

        if (allSearchValidationPassed.value) {
            // Debounce the search text changes by delaying execution for 500 milliseconds
            if (event is SearchTransactionUIEvent.SearchTextChanged) {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    searchTransactions()
                }
            } else {
                searchTransactions()
            }
        }
        printState()
    }

    private fun validateDataWithRules() {
        val searchTextError =
            Validator.validateSearchText(searchText = searchTransactionUIState.value.searchText)

        allSearchValidationPassed.value = searchTextError.status == true
    }


    private fun searchTransactions() {
        Log.d(TAG, "Inside_saveTransaction")
        validateDataWithRules()
        printState()

        searchJob?.cancel()

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            when (searchTransactionUIState.value.filterType) {
                SearchFilters.All.name -> {
                    transactionRepository.searchTransactionsByText(searchText = searchTransactionUIState.value.searchText)
                        .collect { result: NetworkResult<List<TransactionResponse>> ->
                            _searchedTransactionList.value = result
                        }
                }

                SearchFilters.Income.name -> {
                    transactionRepository.searchTransactionByTypeAndText(
                        searchText = searchTransactionUIState.value.searchText,
                        transactionType = searchTransactionUIState.value.filterType
                    ).collect { result: NetworkResult<List<TransactionResponse>> ->
                        _searchedTransactionList.value = result
                    }
                }

                SearchFilters.Expense.name -> {
                    transactionRepository.searchTransactionByTypeAndText(
                        searchText = searchTransactionUIState.value.searchText,
                        transactionType = searchTransactionUIState.value.filterType
                    ).collect { result: NetworkResult<List<TransactionResponse>> ->
                        _searchedTransactionList.value = result
                    }
                }


            }
        }
    }

    fun resetSearchState() {
        _searchedTransactionList.value = NetworkResult.Success(emptyList())
        searchJob?.cancel()
    }

/*
    fun deleteToken() = viewModelScope.launch {
        tokenManager.deleteToken()
    }
*/

    fun logout() = viewModelScope.launch {
        tokenManager.deleteToken()
        authRepository.logout()
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, searchTransactionUIState.value.toString())
    }

    companion object {
        private const val TAG = "DashboardViewModel"
    }


}