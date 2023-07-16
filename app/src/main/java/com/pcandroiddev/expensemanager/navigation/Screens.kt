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

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
