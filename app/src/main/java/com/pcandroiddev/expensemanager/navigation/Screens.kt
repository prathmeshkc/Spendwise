package com.pcandroiddev.expensemanager.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Available screens/destinations in the app
 */
sealed class Screen(
    val route: String,
    val label: String?,
    val icon: ImageVector?
) {

    object RegisterScreen : Screen(route = "register", label = null, icon = null)
    object LoginScreen : Screen(route = "login", label = null, icon = null)
    object DashboardScreen :
        Screen(route = "dashboard", label = "Dashboard", icon = Icons.Outlined.Home)

    object AddTransactionScreen : Screen(route = "addTransaction", label = null, icon = null)
    object EditTransactionScreen : Screen(route = "editTransaction", label = null, icon = null)
    object TransactionDetailsScreen :
        Screen(route = "transactionDetails", label = null, icon = null)

    object AllTransactions :
        Screen(route = "allTransactions", label = "Transactions", icon = Icons.Outlined.CreditCard)

    object Profile : Screen(route = "profile", label = "Profile", icon = Icons.Outlined.Person)

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
