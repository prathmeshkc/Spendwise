package com.pcandroiddev.expensemanager.ui.states

data class RegistrationState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val verify: String? = "",
    val isError: String? = ""
)
