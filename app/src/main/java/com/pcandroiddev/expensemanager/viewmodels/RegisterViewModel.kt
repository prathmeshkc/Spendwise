package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.RegisterUIState
import com.pcandroiddev.expensemanager.ui.uievents.RegisterUIEvent

class RegisterViewModel : ViewModel() {

    var registerUIState = mutableStateOf(RegisterUIState())
        private set

    var allValidationPassed = mutableStateOf(false)
        private set

    fun onEventChange(event: RegisterUIEvent) {
        when (event) {

            is RegisterUIEvent.NameChanged -> {
                registerUIState.value = registerUIState.value.copy(name = event.name)
            }

            is RegisterUIEvent.EmailChanged -> {
                registerUIState.value = registerUIState.value.copy(email = event.email)
            }

            is RegisterUIEvent.PasswordChanged -> {
                registerUIState.value = registerUIState.value.copy(password = event.password)
            }

            is RegisterUIEvent.RegisterButtonClicked -> {
                registerUserWithEmailPassword()
            }

            is RegisterUIEvent.GoogleSignUpClicked -> {
                registerWithGoogleSignUp()
            }
        }
        validateWithRules()
    }

    private fun registerUserWithEmailPassword() {
        Log.d(TAG, "Inside_registerUserWithEmailPassword")
        validateWithRules()
        printState()
    }

    private fun registerWithGoogleSignUp() {
        Log.d(TAG, "Inside_registerWithGoogleSignUp")
        validateWithRules()
        printState()
    }

    private fun validateWithRules() {
        val nameResult = Validator.validateName(name = registerUIState.value.name)
        val emailResult = Validator.validateEmail(email = registerUIState.value.email)
        val passwordResult = Validator.validatePassword(password = registerUIState.value.password)

        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "nameResult: $nameResult")
        Log.d(TAG, "emailResult: $emailResult")
        Log.d(TAG, "passwordResult: $passwordResult")

        registerUIState.value = registerUIState.value.copy(
            nameError = nameResult,
            emailError = emailResult,
            passwordError = passwordResult
        )

        allValidationPassed.value = nameResult.first && emailResult.first && passwordResult.first

        printState()

    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registerUIState.value.toString())
    }


    companion object {
        private const val TAG = "RegisterViewModel"
    }

}