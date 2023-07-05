package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pcandroiddev.expensemanager.ui.rules.ValidationResult
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.ui.AddTransactionUIState
import com.pcandroiddev.expensemanager.ui.uievents.AddTransactionUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddTransactionViewModel @Inject constructor() : ViewModel() {


    var addTransactionUIState = mutableStateOf(AddTransactionUIState())

    var allValidationPassed = mutableStateOf(false)
        private set

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

    private fun saveTransaction() {
        Log.d(TAG, "Inside_saveTransaction")
        validateDataWithRules()
        printState()
    }

    private fun validateDataWithRules() {

        val titleResult = Validator.validateTitle(title = addTransactionUIState.value.title)
        val amountResult =
            try {
                if (addTransactionUIState.value.amount.isEmpty() || addTransactionUIState.value.amount.isBlank()) {
                    ValidationResult(status = false)
                } else {
                    Validator.validateAmount(amount = addTransactionUIState.value.amount.toDouble())
                }
            } catch (numberFormatException: NumberFormatException) {
                ValidationResult(status = false)
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
            titleError = titleResult.status,
            amountError = amountResult.status,
            categoryError = categoryResult.status,
            noteError = noteResult.status
        )

        allValidationPassed.value =
            titleResult.status && amountResult.status && categoryResult.status && noteResult.status

        printState()


    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, addTransactionUIState.value.toString())
    }

    companion object {
        private const val TAG = "AddTransactionViewModel"
    }
}
