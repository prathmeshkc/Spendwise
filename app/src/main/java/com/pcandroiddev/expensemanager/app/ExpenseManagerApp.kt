package com.pcandroiddev.expensemanager.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pcandroiddev.expensemanager.navigation.ExpenseManagerRouter
import com.pcandroiddev.expensemanager.navigation.Screen
import com.pcandroiddev.expensemanager.ui.screens.dashboard.DashboardScreen
import com.pcandroiddev.expensemanager.ui.screens.login.LoginScreen
import com.pcandroiddev.expensemanager.ui.screens.register.RegisterScreen
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.screens.transaction.AddTransactionScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.EditTransactionScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.TransactionDetailsScreen

@Composable
fun ExpenseManagerApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceBackgroundColor
    ) {

        Crossfade(targetState = ExpenseManagerRouter.currentScreen) { currentScreen ->
            when (currentScreen.value) {

                Screen.RegisterScreen -> {
                    RegisterScreen()
                }

                Screen.LoginScreen -> {
                    LoginScreen()
                }

                is Screen.DashboardScreen -> {
                    DashboardScreen()
                }

                is Screen.AddTransactionScreen -> {
                    AddTransactionScreen()
                }

                is Screen.TransactionDetailsScreen -> {
                    TransactionDetailsScreen()
                }

                is Screen.EditTransactionScreen -> {
                    EditTransactionScreen()
                }


            }
        }


    }
}