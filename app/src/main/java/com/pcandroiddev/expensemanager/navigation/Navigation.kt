package com.pcandroiddev.expensemanager.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.ui.screens.dashboard.DashboardScreen
import com.pcandroiddev.expensemanager.ui.screens.login.LoginScreen
import com.pcandroiddev.expensemanager.ui.screens.register.RegisterScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.AddTransactionScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.AllTransactionsScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.EditTransactionScreen
import com.pcandroiddev.expensemanager.ui.screens.transaction.TransactionDetailsScreen
import java.io.File


@Composable
fun NavigationGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    accessToken: String? = null,
    isEmailVerified: Boolean = false,
    symbol: String,
    modifier: Modifier
) {

    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current


    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (accessToken != null && isEmailVerified) Screen.DashboardScreen.route else Screen.RegisterScreen.route
    ) {

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(
                snackbarHostState = snackbarHostState,
                onLoginTextClicked = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                onRegistrationSuccessful = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(route = Screen.RegisterScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onBackPressedCallback = { activity?.finish() }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                snackbarHostState = snackbarHostState,
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
                snackbarHostState = snackbarHostState,
                symbol = symbol,
                onSeeAllTransactionClicked = {
                    navController.navigate(Screen.AllTransactions.route)
                },
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
                    activity?.finish()
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
                    /*navController.navigate(route = Screen.DashboardScreen.route) {
                        popUpTo(Screen.TransactionDetailsScreen.route) {
                            inclusive = true
                        }
                    }*/

                    navController.popBackStack()
                },
                onEditFABClicked = {
                    navController.navigate(
                        Screen.EditTransactionScreen.withArgs(
                            jsonTransactionResponse
                        )
                    )
                },
                onShareButtonClicked = { transactionResponse ->
                    shareTransaction(context, transactionResponse)
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
        ) {

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

        composable(route = Screen.AllTransactions.route) {
            AllTransactionsScreen(
                snackbarHostState = snackbarHostState,
                symbol = symbol,
                onTransactionListItemClicked = { transactionResponse ->
                    val transactionResponseString = Gson().toJson(transactionResponse)
                    navController.navigate(
                        Screen.TransactionDetailsScreen.withArgs(transactionResponseString)
                    )
                },
                onStatementGenerated = { file ->
                    openStatement(context, file)
                },
                onBackPressed = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(route = Screen.AllTransactions.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

private fun shareTransaction(
    context: Context,
    transactionResponse: TransactionResponse
) {

    val sharedContent = StringBuilder()
    sharedContent.append("${transactionResponse.transactionType} - ${transactionResponse.title}")
        .append("\n\n")
    sharedContent.append("Title: ").append(transactionResponse.title).append("\n\n")
    sharedContent.append("Amount: $").append(transactionResponse.amount.toString()).append("\n\n")
    sharedContent.append("Transaction Type: ").append(transactionResponse.transactionType)
        .append("\n\n")
    sharedContent.append("Category: ").append(transactionResponse.category).append("\n\n")
    sharedContent.append("Transaction Date: ").append(transactionResponse.transactionDate)
        .append("\n\n")
    sharedContent.append("Note: ").append(transactionResponse.note).append("\n\n")

    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(
            Intent.EXTRA_SUBJECT,
            "${transactionResponse.transactionType} - ${transactionResponse.title}"
        )
        putExtra(Intent.EXTRA_TEXT, sharedContent.toString())
        type = "text/plain"
    }


    context.startActivity(
        Intent.createChooser(
            intent,
            "Share Transaction To:"
        )
    )
}


private fun openStatement(context: Context, file: File) {
    val uri = Uri.fromFile(file)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, "application/pdf")
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    try {
        context.startActivity(intent)
    } catch (exception: Exception) {
        Log.d(TAG, "Exception in startActivity: ${exception.message}")
    }

}

private const val TAG = "Navigation"


