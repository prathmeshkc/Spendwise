package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pcandroiddev.expensemanager.data.remote.TransactionRequest
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.ui.rules.ValidationResult
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.ResultState
import com.pcandroiddev.expensemanager.ui.states.ui.AddTransactionUIState
import com.pcandroiddev.expensemanager.ui.uievents.EditTransactionUIEvent
import com.pcandroiddev.expensemanager.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var editTransactionUIState: MutableState<AddTransactionUIState> = mutableStateOf(
        AddTransactionUIState()
    )
    var allValidationPassed = mutableStateOf(false)
        private set

    private val _updateTransactionState = Channel<ResultState>()
    val updateTransactionState = _updateTransactionState.receiveAsFlow()

    private var transactionId: String? = null

    init {
        val transactionResponseJsonString = savedStateHandle.get<String>("transactionResponse")
        val transactionResponse =
            Gson().fromJson(transactionResponseJsonString, TransactionResponse::class.java)
        if (transactionResponse != null) {
            Log.d(TAG, "EditTransactionViewModel init: $transactionResponse")
            transactionId = transactionResponse.transactionId
            editTransactionUIState.value = AddTransactionUIState(transactionResponse)
        } else {
            Log.d(TAG, "EditTransactionViewModel init: transactionResponse == null")
        }
    }

    fun onEventChange(event: EditTransactionUIEvent) {
        when (event) {
            is EditTransactionUIEvent.TitleChanged -> {
                editTransactionUIState.value =
                    editTransactionUIState.value.copy(title = event.title)
                printState()
            }

            is EditTransactionUIEvent.AmountChanged -> {
                editTransactionUIState.value =
                    editTransactionUIState.value.copy(amount = event.amount)
                printState()
            }

            is EditTransactionUIEvent.TransactionTypeChanged -> {
                editTransactionUIState.value =
                    editTransactionUIState.value.copy(transactionType = event.transactionType)
                printState()
            }

            is EditTransactionUIEvent.CategoryChanged -> {
                editTransactionUIState.value =
                    editTransactionUIState.value.copy(category = event.category)
                printState()
            }

            is EditTransactionUIEvent.DateChanged -> {
                editTransactionUIState.value = editTransactionUIState.value.copy(date = event.date)
                printState()
            }

            is EditTransactionUIEvent.NoteChanged -> {
                editTransactionUIState.value = editTransactionUIState.value.copy(note = event.note)
                printState()
            }

            is EditTransactionUIEvent.EditTransactionButtonClicked -> {
                editTransaction(transactionId!!)
                printState()
            }
        }
        validateDataWithRules()
    }

    private fun validateDataWithRules() {

        val titleResult = Validator.validateTitle(title = editTransactionUIState.value.title)
        val amountResult = try {
            if (editTransactionUIState.value.amount <= 0) {
                ValidationResult(status = false)
            } else {
                Validator.validateAmount(amount = editTransactionUIState.value.amount)
            }
        } catch (numberFormatException: NumberFormatException) {
            ValidationResult(status = false)
        }
        val categoryResult =
            Validator.validateCategory(category = editTransactionUIState.value.category)
        val noteResult = Validator.validateNote(note = editTransactionUIState.value.note)

        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "titleResult: $titleResult")
        Log.d(TAG, "amountResult: $amountResult")
        Log.d(TAG, "categoryResult: $categoryResult")
        Log.d(TAG, "noteResult: $noteResult")

        /*
        These results will return the validation status, i.e. if validated -> true else false.
        So, the errors(titleError, amountError, etc) will hold true if validation is successful.
        Don't confuse it with unsuccessful validation
         */

        editTransactionUIState.value = editTransactionUIState.value.copy(
            titleError = titleResult.status,
            amountError = amountResult.status,
            categoryError = categoryResult.status,
            noteError = noteResult.status
        )

        allValidationPassed.value =
            titleResult.status && amountResult.status && categoryResult.status && noteResult.status

        printState()


    }

    private fun editTransaction(transactionId: String) {
        Log.d(TAG, "Inside_editTransaction")
        validateDataWithRules()
        printState()

        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.updateTransaction(
                transactionId = transactionId, transactionRequest = TransactionRequest(
                    title = editTransactionUIState.value.title,
                    amount = editTransactionUIState.value.amount,
                    transactionType = editTransactionUIState.value.transactionType,
                    category = editTransactionUIState.value.category,
                    transactionDate = editTransactionUIState.value.date,
                    note = editTransactionUIState.value.note
                )
            ).collect { updateTransactionResult: NetworkResult<String> ->
                when (updateTransactionResult) {
                    is NetworkResult.Loading -> {
                        _updateTransactionState.send(ResultState(isLoading = true))
                    }

                    is NetworkResult.Success -> {
                        _updateTransactionState.send(ResultState(isSuccess = updateTransactionResult.data))
                    }

                    is NetworkResult.Error -> {
                        _updateTransactionState.send(ResultState(isError = updateTransactionResult.message))
                    }
                }

            }
        }
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, editTransactionUIState.value.toString())
    }


    companion object {
        private const val TAG = "EditTransactionViewModel"
    }


}