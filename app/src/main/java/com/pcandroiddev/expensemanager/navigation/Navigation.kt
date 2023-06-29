package com.pcandroiddev.expensemanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pcandroiddev.expensemanager.ui.screens.dashboard.DashboardScreen
import com.pcandroiddev.expensemanager.ui.screens.login.LoginScreen
import com.pcandroiddev.expensemanager.ui.screens.register.RegisterScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.AddTransactionScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.EditTransactionScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.TransactionDetailsScreen


//TODO: Add arguments to required screens
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    accessToken: String? = null
) {

    NavHost(
        navController = navController,
        startDestination = if (accessToken != null) Screen.DashboardScreen.route else Screen.RegisterScreen.route
    ) {

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(
                onLoginTextClicked = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                onRegistrationSuccessful = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(route = Screen.RegisterScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                onRegisterTextClicked = {
                    navController.navigate(Screen.RegisterScreen.route) {
                        popUpTo(route = Screen.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onLoginSuccessful = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(route = Screen.RegisterScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.DashboardScreen.route) {
            DashboardScreen(
                onAddTransactionFABClicked = {
                    navController.navigate(Screen.AddTransactionScreen.route)
                },
                onTransactionListItemClicked = {
                    navController.navigate(Screen.TransactionDetailsScreen.route)
                }
            )
        }

        composable(route = Screen.AddTransactionScreen.route) {
            AddTransactionScreen(
                onNavigateUpClicked = {
                    navController.navigateUp()
                },
                onSaveTransactionClicked = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(route = Screen.AddTransactionScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.EditTransactionScreen.route
        ) {
            EditTransactionScreen(
                onNavigateUpClicked = {
                    navController.navigateUp()
                },
                onSaveTransactionClicked = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(route = Screen.EditTransactionScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        //TODO: Modify to Accept arguments of type Transaction or else multiple args
        composable(
            route = Screen.TransactionDetailsScreen.route
        ) {
            TransactionDetailsScreen(
                onNavigateUpClicked = {
                    navController.navigateUp()
                },
                onEditFABClicked = {
                    navController.navigate(Screen.EditTransactionScreen.route)
                },
                onShareButtonClicked = {
                    //TODO: Create a function here to share note
                },
                onDeleteTransactionButtonClicked = {
                    //TODO: Modify this lambda to take appropriate transaction object
                }
            )
        }
    }
}
