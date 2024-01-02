package com.pcandroiddev.expensemanager.ui.states

import com.google.firebase.auth.AuthResult

data class GoogleSignInState(
    val isSuccess: AuthResult? = null,
    val isError: String? = "",
    val isLoading: Boolean = false
)
