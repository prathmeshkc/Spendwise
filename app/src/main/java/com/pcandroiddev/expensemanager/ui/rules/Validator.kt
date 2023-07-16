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

    fun validateName(name: String): Pair<Boolean, String> {
        if (name.isEmpty() || name.isBlank()) {
            return Pair(first = false, second = "Name must not be empty!")
        }

        if (name.length <= 3) {
            return Pair(first = false, second = "Name should contain more than 3 characters!")
        }

        return Pair(first = true, second = "")
    }

    fun validateEmail(email: String): Pair<Boolean, String> {

        if (email.isEmpty() || email.isBlank()) {
            return Pair(first = false, second = "Email must not be empty!")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Pair(first = false, second = "Invalid email address!")
        }

        return Pair(first = true, second = "")
    }

    fun validatePassword(password: String): Pair<Boolean, String> {
        if (password.isEmpty()) {
            return Pair(first = false, second = "Password must not be empty!")
        }

        if (password.length <= 4) {
            return Pair(first = false, second = "Password should be more than 4 characters!")
        }

        return Pair(first = true, second = "")
    }

    fun validateSearchText(searchText: String): ValidationResult {
        return ValidationResult(status = searchText.isNotBlank() && searchText.isNotEmpty())
    }


}

data class ValidationResult(
    val status: Boolean = false
)