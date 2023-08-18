package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.pcandroiddev.expensemanager.data.local.datastore.UserPreferencesManager
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.RegistrationState
import com.pcandroiddev.expensemanager.ui.states.ResultState
import com.pcandroiddev.expensemanager.ui.states.ui.LoginUIState
import com.pcandroiddev.expensemanager.ui.uievents.LoginUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    var loginUIState = mutableStateOf(LoginUIState())
        private set

    var allValidationPassed = mutableStateOf(false)
        private set

    private val _signInState = Channel<RegistrationState>()
    val singInState = _signInState.receiveAsFlow()

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
                loginWithGoogleSignIn()
            }
        }
        validateWithRules()
    }

    private fun loginUserWithEmailPassword() {
        Log.d(TAG, "Inside_loginUserWithEmailPassword")
        printState()

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginUser(
                email = loginUIState.value.email,
                password = loginUIState.value.password
            ).collect { authResult: ApiResult<AuthResult> ->

                when (authResult) {
                    is ApiResult.Loading -> {
                        _signInState.send(RegistrationState(isLoading = true))
                    }

                    is ApiResult.Success -> {
                        val tokenResult = authResult.data?.user?.getIdToken(false)?.await()
                        val token = tokenResult?.token
                        val user = authResult.data?.user

                        if (user?.isEmailVerified!!) {
                            userPreferencesManager.saveEmailVerificationStatus(isVerified = true)
                            userPreferencesManager.saveToken(token = token!!)
                            Log.d(
                                TAG,
                                "loginUserWithEmailPassword: ${userPreferencesManager.getToken()}"
                            )
                            _signInState.send(RegistrationState(isSuccess = "Sign In Success!"))
                        }else {
                            _signInState.send(RegistrationState(verify = "Please Verify Your Email!", isLoading = false))
                        }
                    }

                    is ApiResult.Error -> {
                        _signInState.send(RegistrationState(isError = authResult.message))
                    }
                }


            }
        }

    }

    private fun loginWithGoogleSignIn() {
        Log.d(TAG, "Inside_loginWithGoogleSignIn")
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
        private const val TAG = "LoginViewModel"
    }


}