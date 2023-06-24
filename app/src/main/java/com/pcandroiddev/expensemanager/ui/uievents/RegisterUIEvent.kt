package com.pcandroiddev.expensemanager.ui.uievents

sealed class RegisterUIEvent {
    data class EmailChanged(val email: String): RegisterUIEvent()
    data class PasswordChanged(val password: String): RegisterUIEvent()

    object RegisterButtonClicked: RegisterUIEvent()
}
