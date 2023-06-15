package com.pcandroiddev.expensemanager.app

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pcandroiddev.expensemanager.navigation.ExpenseManagerRouter
import com.pcandroiddev.expensemanager.navigation.Screen
import com.pcandroiddev.expensemanager.ui.dashboard.DashboardScreen
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.transaction.AddTransactionScreen
import com.pcandroiddev.expensemanager.ui.transaction.EditTransactionScreen
import com.pcandroiddev.expensemanager.ui.transaction.TransactionDetailsScreen

@Composable
fun ExpenseManagerApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceBackgroundColor
    ) {

        Crossfade(targetState = ExpenseManagerRouter.currentScreen) { currentScreen ->
            when (currentScreen.value) {
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