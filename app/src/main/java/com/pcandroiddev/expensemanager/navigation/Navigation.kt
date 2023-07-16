package com.pcandroiddev.expensemanager.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
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

    val activity = (LocalContext.current as? Activity)

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
                onTransactionListItemClicked = { transactionResponse ->
                    val transactionResponseString = Gson().toJson(transactionResponse)
                    navController.navigate(
                        Screen.TransactionDetailsScreen.withArgs(transactionResponseString)
                    )
                },
                onSearchedTransactionListItemClicked = { transactionResponse ->
                    val transactionResponseString = Gson().toJson(transactionResponse)
                    navController.navigate(
                        Screen.TransactionDetailsScreen.withArgs(transactionResponseString)
                    )
                },
                onLogOutButtonClicked = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.DashboardScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onBackPressedCallback = {
                    activity?.finishAffinity()
                    /*navController.popBackStack(
                        route = Screen.DashboardScreen.route,
                        inclusive = true
                    )*/
                }
            )
        }

        composable(
            route = Screen.TransactionDetailsScreen.route + "/{transactionResponse}",
            arguments = listOf(
                navArgument(name = "transactionResponse") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { navBackStackEntry: NavBackStackEntry ->
            val jsonTransactionResponse =
                navBackStackEntry.arguments?.getString("transactionResponse")!!
            TransactionDetailsScreen(
                jsonTransactionResponse = jsonTransactionResponse,
                onNavigateUpClicked = {
                    navController.navigate(route = Screen.DashboardScreen.route) {
                        popUpTo(Screen.TransactionDetailsScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onEditFABClicked = {
                    navController.navigate(
                        Screen.EditTransactionScreen.withArgs(
                            jsonTransactionResponse
                        )
                    )
                },
                onShareButtonClicked = {
                    //TODO: Create a function here to share note
                },
                onDeleteTransactionButtonClicked = {
                    navController.navigate(route = Screen.DashboardScreen.route) {
                        popUpTo(Screen.TransactionDetailsScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.EditTransactionScreen.route + "/{transactionResponse}",
            arguments = listOf(
                navArgument(name = "transactionResponse") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { navBackStackEntry: NavBackStackEntry ->

            val transactionResponseJsonString =
                navBackStackEntry.arguments?.getString("transactionResponse")!!

            val transactionResponse =
                Gson().fromJson(transactionResponseJsonString, TransactionResponse::class.java)

            EditTransactionScreen(
                onNavigateUpClicked = {
                    navController.navigateUp()
                },
                onSuccessfulUpdateCallback = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(route = Screen.EditTransactionScreen.route) {
                            inclusive = true
                        }
                    }
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


    }
}
