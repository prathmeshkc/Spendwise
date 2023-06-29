package com.pcandroiddev.expensemanager.ui.states

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
