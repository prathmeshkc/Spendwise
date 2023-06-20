package com.pcandroiddev.expensemanager.ui.rules

import android.util.Log

object Validator {

    fun validateTitle(title: String): ValidationResult {
        Log.d("Validator", "validateTitle: $title")
        return ValidationResult(
            status = (title.isNotEmpty() && title.isNotBlank())
        )
    }

    fun validateAmount(amount: Double): ValidationResult {
        return ValidationResult(
            status = (amount >= 0)
        )
    }

    fun validateCategory(category: String): ValidationResult {
        return ValidationResult(
            status = (category.isNotEmpty() || category.isNotBlank())
        )
    }

    //TODO: In case we decide to go with monthly refresh approach, change the validation logic for date to be not more than that months end date

    fun validateNote(note: String): ValidationResult {
        return ValidationResult(
            status = note.isNotBlank()
        )
    }

}

data class ValidationResult(
    val status: Boolean = false
)