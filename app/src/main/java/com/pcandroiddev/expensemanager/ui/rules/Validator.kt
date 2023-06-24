package com.pcandroiddev.expensemanager.ui.rules

import android.util.Log
import android.util.Patterns


/**
Returns true if validation is successful
 */
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


    fun validateNote(note: String): ValidationResult {
        return ValidationResult(
            status = note.isNotBlank()
        )
    }

    fun validateEmail(email: String): ValidationResult {
        return ValidationResult(
            status = (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
        )
    }


}

data class ValidationResult(
    val status: Boolean = false
)