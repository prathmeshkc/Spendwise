package com.pcandroiddev.expensemanager.ui.rules

import android.util.Log
import android.util.Patterns


/**
Returns true if validation is successful
 */
object Validator {

    fun validateTitle(title: String): Pair<Boolean, String> {
        Log.d("Validator", "validateTitle: $title")
        if (title.isEmpty() || title.isBlank()) {
            return Pair(false, "Title must not be empty!")
        }

        return Pair(true, "")

    }

    fun validateAmount(amount: Double): Pair<Boolean, String> {

        if (amount < 0) {
            return Pair(first = false, second = "Amount cannot be less than 0!")
        }

        return Pair(first = true, second = "")
    }

    fun validateCategory(category: String): Pair<Boolean, String> {

        if (category.isEmpty() || category.isBlank()) {
            return Pair(first = false, second = "")
        }

        return Pair(first = true, second = "")
    }


    fun validateNote(note: String): Pair<Boolean, String> {

        if (note.isBlank()) {
            return Pair(first = false, second = "Only whitespaces not allowed!")
        }

        return Pair(first = true, second = "")
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