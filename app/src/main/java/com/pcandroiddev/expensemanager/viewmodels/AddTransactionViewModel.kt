package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.data.remote.TransactionRequest
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.ui.rules.ValidationResult
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.ResultState
import com.pcandroiddev.expensemanager.ui.states.ui.AddTransactionUIState
import com.pcandroiddev.expensemanager.ui.uievents.AddTransactionUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {


    var addTransactionUIState = mutableStateOf(AddTransactionUIState())

    private val _allValidationPassed = mutableStateOf(false)
    val allValidationPassed get() = _allValidationPassed

    private val _createTransactionState = Channel<ResultState>()
    val createTransactionState = _createTransactionState.receiveAsFlow()

    fun onEventChange(event: AddTransactionUIEvent) {
        when (event) {
            is AddTransactionUIEvent.TitleChanged -> {
                addTransactionUIState.value = addTransactionUIState.value
                    .copy(title = event.title)
                printState()
            }

            is AddTransactionUIEvent.AmountChanged -> {
                addTransactionUIState.value = addTransactionUIState.value
                    .copy(amount = event.amount)
                printState()
            }

            is AddTransactionUIEvent.TransactionTypeChanged -> {
                addTransactionUIState.value = addTransactionUIState.value
                    .copy(transactionType = event.transactionType)
                printState()
            }

            is AddTransactionUIEvent.CategoryChanged -> {
                addTransactionUIState.value = addTransactionUIState.value
                    .copy(category = event.category)
                printState()
            }

            is AddTransactionUIEvent.DateChanged -> {
                addTransactionUIState.value = addTransactionUIState.value
                    .copy(date = event.date)
                printState()
            }

            is AddTransactionUIEvent.NoteChanged -> {
                addTransactionUIState.value = addTransactionUIState.value
                    .copy(note = event.note)
                printState()
            }

            is AddTransactionUIEvent.SaveTransactionButtonClicked -> {
                saveTransaction()
                printState()
            }
        }
        validateDataWithRules()

    }

    private fun validateDataWithRules() {

        val titleResult = Validator.validateTitle(title = addTransactionUIState.value.title)
        val amountResult =
            try {
                if (addTransactionUIState.value.amount <= 0) {
                    /*ValidationResult(status = false)*/
                    Pair(first = false, second = "Amount cannot be less than or equal to 0!")
                } else {
                    Validator.validateAmount(amount = addTransactionUIState.value.amount)
                }
            } catch (numberFormatException: NumberFormatException) {
                /*ValidationResult(status = false)*/
                Pair(first = false, second = "Enter numbers only!")
            }
        val categoryResult =
            Validator.validateCategory(category = addTransactionUIState.value.category)
        val noteResult = Validator.validateNote(note = addTransactionUIState.value.note)

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

        addTransactionUIState.value = addTransactionUIState.value.copy(
            titleError = titleResult,
            amountError = amountResult,
            categoryError = categoryResult,
            noteError = noteResult
        )

        _allValidationPassed.value =
            titleResult.first && amountResult.first && categoryResult.first

        printState()


    }


    private fun saveTransaction() {
        Log.d(TAG, "Inside_saveTransaction")
        validateDataWithRules()
        printState()

        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.createTransaction(
                TransactionRequest(
                    title = addTransactionUIState.value.title,
                    amount = addTransactionUIState.value.amount,
                    transactionType = addTransactionUIState.value.transactionType,
                    category = addTransactionUIState.value.category,
                    transactionDate = addTransactionUIState.value.date,
                    note = addTransactionUIState.value.note
                )
            ).collect { createTransactionResult: ApiResult<String> ->
                when (createTransactionResult) {
                    is ApiResult.Loading -> {
                        _createTransactionState.send(ResultState(isLoading = true))
                    }

                    is ApiResult.Success -> {
                        _createTransactionState.send(ResultState(isSuccess = createTransactionResult.data))
                    }

                    is ApiResult.Error -> {
                        _createTransactionState.send(ResultState(isError = createTransactionResult.message))
                    }
                }

            }

        }
    }


    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, addTransactionUIState.value.toString())
    }

    companion object {
        private const val TAG = "AddTransactionViewModel"
    }
}
