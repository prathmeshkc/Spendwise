package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.AuthState
import com.pcandroiddev.expensemanager.ui.states.ui.RegisterUIState
import com.pcandroiddev.expensemanager.ui.uievents.RegisterUIEvent
import com.pcandroiddev.expensemanager.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//TODO: Add HiltViewModel when injecting into the constructor
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {


    var registerUIState = mutableStateOf(RegisterUIState())
        private set

    var allValidationPassed = mutableStateOf(false)
        private set

    private val _signUpState = Channel<AuthState>()
    val signUpState = _signUpState.receiveAsFlow()

    var token: String? = null

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
            ).collect { authResult: NetworkResult<AuthResult> ->
                when (authResult) {
                    is NetworkResult.Loading -> {
                        _signUpState.send(AuthState(isLoading = true))
                    }

                    is NetworkResult.Success -> {
                        val tokenResult = authResult.data?.user?.getIdToken(false)?.await()
                        val token = tokenResult?.token
                        tokenManager.saveToken(token = token!!)
                        Log.d(TAG, "registerUserWithEmailPassword: ${tokenManager.getToken()}")
                        _signUpState.send(AuthState(isSuccess = "Sign Up Success!"))
                    }

                    is NetworkResult.Error -> {
                        _signUpState.send(AuthState(isError = authResult.message))
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