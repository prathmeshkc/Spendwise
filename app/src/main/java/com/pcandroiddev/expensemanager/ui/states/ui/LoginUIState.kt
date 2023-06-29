package com.pcandroiddev.expensemanager.ui.states.ui

data class LoginUIState(
    var email: String = "",
    var password: String = "",

    var emailError: Pair<Boolean, String> = Pair(false, "Email must not be empty!"),
    var passwordError: Pair<Boolean, String> = Pair(false, "Password must not be empty!")
)
