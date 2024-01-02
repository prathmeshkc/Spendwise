package com.pcandroiddev.expensemanager.ui.states.ui

data class RegisterUIState(
    var name: String = "",
    var email: String = "",
    var password: String = "",

    var nameError:  Pair<Boolean, String> = Pair(false, "Name must not be empty!"),
    var emailError: Pair<Boolean, String> = Pair(false, "Email must not be empty!"),
    var passwordError: Pair<Boolean, String> = Pair(false, "Password must not be empty!")
)
