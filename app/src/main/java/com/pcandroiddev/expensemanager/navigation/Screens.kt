package com.pcandroiddev.expensemanager.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen(val route: String) {

    object RegisterScreen : Screen(route = "register")
    object LoginScreen : Screen(route = "login")
    object DashboardScreen : Screen(route = "dashboard")
    object AddTransactionScreen : Screen(route = "addTransaction")
    object EditTransactionScreen : Screen(route = "editTransaction")
    object TransactionDetailsScreen : Screen(route = "transactionDetails")
}

object ExpenseManagerRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.RegisterScreen)

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
    }
}
