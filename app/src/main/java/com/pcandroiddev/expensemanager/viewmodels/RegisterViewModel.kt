package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.ResultState
import com.pcandroiddev.expensemanager.ui.states.ui.RegisterUIState
import com.pcandroiddev.expensemanager.ui.uievents.RegisterUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//TODO: Add HiltViewModel when injecting into the constructor
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val firebaseClientId: String
) : ViewModel() {


    var registerUIState = mutableStateOf(RegisterUIState())
        private set

    var allValidationPassed = mutableStateOf(false)
        private set

    private val _signUpState = Channel<ResultState>()
    val signUpState = _signUpState.receiveAsFlow()


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
        printState()
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.registerUser(
                email = registerUIState.value.email,
                password = registerUIState.value.password
            ).collect { authResult: ApiResult<AuthResult> ->
                when (authResult) {
                    is ApiResult.Loading -> {
                        _signUpState.send(ResultState(isLoading = true))
                    }

                    is ApiResult.Success -> {
                        val tokenResult = authResult.data?.user?.getIdToken(true)?.await()
                        val token = tokenResult?.token
                        val userId = tokenResult?.claims?.get("user_id")
                        Log.d(TAG, "registerUserWithEmailPassword: $userId")
                        tokenManager.saveToken(token = token!!)
                        Log.d(TAG, "registerUserWithEmailPassword: ${tokenManager.getToken()}")
                        _signUpState.send(ResultState(isSuccess = "Sign Up Success!"))
                    }

                    is ApiResult.Error -> {
                        _signUpState.send(ResultState(isError = authResult.message))
                    }
                }
            }
        }
    }

    private fun registerWithGoogleSignUp() {
        Log.d(TAG, "Inside_registerWithGoogleSignUp")
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