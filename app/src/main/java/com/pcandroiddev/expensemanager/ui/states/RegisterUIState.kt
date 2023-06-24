package com.pcandroiddev.expensemanager.ui.states

data class RegisterUIState(
    var email: String = "",
    var password: String = "",

    var emailError: Boolean = false,
    var passwordError: Boolean = false
)
