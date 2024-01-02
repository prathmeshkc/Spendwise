package com.pcandroiddev.expensemanager.ui.uievents

sealed class LoginUIEvent {
    data class EmailChanged(val email: String): LoginUIEvent()
    data class PasswordChanged(val password: String): LoginUIEvent()

    object LoginButtonClicked: LoginUIEvent()
    object GoogleSignInClicked: LoginUIEvent()
}
