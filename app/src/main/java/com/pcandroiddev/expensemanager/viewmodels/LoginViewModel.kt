package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.LoginUIState
import com.pcandroiddev.expensemanager.ui.uievents.LoginUIEvent

class LoginViewModel : ViewModel() {

    var loginUIState = mutableStateOf(LoginUIState())
        private set

    var allValidationPassed = mutableStateOf(false)
        private set

    fun onEventChange(event: LoginUIEvent) {
        when (event) {

            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(email = event.email)
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(password = event.password)
            }

            is LoginUIEvent.LoginButtonClicked -> {
                loginUserWithEmailPassword()
            }

            is LoginUIEvent.GoogleSignInClicked -> {

            }
        }
        validateWithRules()
    }

    private fun loginUserWithEmailPassword() {
        Log.d(TAG, "Inside_loginUserWithEmailPassword")
        validateWithRules()
        printState()
    }

    private fun loginWithGoogleSignIn() {
        Log.d(TAG, "Inside_loginWithGoogleSignIn")
        validateWithRules()
        printState()
    }

    private fun validateWithRules() {
        val emailResult = Validator.validateEmail(email = loginUIState.value.email)
        val passwordResult = Validator.validatePassword(password = loginUIState.value.password)

        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "emailResult: $emailResult")
        Log.d(TAG, "passwordResult: $passwordResult")

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult,
            passwordError = passwordResult
        )

        allValidationPassed.value = emailResult.first && passwordResult.first

        printState()

    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, loginUIState.value.toString())
    }


    companion object {
        private const val TAG = "RegisterViewModel"
    }


}