package com.pcandroiddev.expensemanager.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {
    object DashboardScreen : Screen()
    object AddTransactionScreen : Screen()
    object EditTransactionScreen : Screen()
    object TransactionDetailsScreen : Screen()
}

object ExpenseManagerRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.DashboardScreen)

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
    }
}
