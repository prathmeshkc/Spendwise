package com.pcandroiddev.expensemanager.ui.states

data class ResultState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
