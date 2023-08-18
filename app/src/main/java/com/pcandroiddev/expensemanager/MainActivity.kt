package com.pcandroiddev.expensemanager

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pcandroiddev.expensemanager.data.local.datastore.UserPreferencesManager
import com.pcandroiddev.expensemanager.navigation.NavigationGraph
import com.pcandroiddev.expensemanager.navigation.Screen
import com.pcandroiddev.expensemanager.navigation.Screen.DashboardScreen
import com.pcandroiddev.expensemanager.navigation.Screen.AllTransactions
import com.pcandroiddev.expensemanager.ui.screens.network.NetworkStateScreen
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemIndicatorColor
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemSelectedColor
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemUnselectedColor
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.ExpenseManagerTheme
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.utils.networkstate.NetworkState
import com.pcandroiddev.expensemanager.utils.orientationstate.OrientationState
import com.pcandroiddev.expensemanager.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var userPreferencesManager: UserPreferencesManager

    @Inject
    lateinit var currencyInstanceNumberFormat: NumberFormat

    private var token: String? = null
    private var isEmailVerified: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        CoroutineScope(Dispatchers.IO).launch {
            token = userPreferencesManager.getToken()
            isEmailVerified = userPreferencesManager.getEmailVerificationStatus()
        }

        setContent {

            ExpenseManagerTheme {

                val networkState by mainViewModel.networkState.collectAsStateWithLifecycle()
                when (networkState) {
                    NetworkState.Available -> {
                        /*val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
                        val symbol = numberFormat.currency?.symbol
                        NavigationGraph(accessToken = token, symbol = symbol ?: "$")*/
                        ExpenseManagerApp(
                            accessToken = token,
                            isEmailVerified = isEmailVerified,
                            currencyInstanceNumberFormat = currencyInstanceNumberFormat
                        )
                    }

                    NetworkState.Losing -> {
                        NetworkStateScreen(networkState = "Losing Network")
                    }

                    NetworkState.Lost -> {
                        NetworkStateScreen(networkState = "Network Lost")
                    }

                    NetworkState.Unavailable -> {
                        NetworkStateScreen(networkState = "Network Unavailable")
                    }
                }

                //                val orientationState by mainViewModel.orientationState.collectAsStateWithLifecycle()

                /*when (orientationState) {
                    OrientationState.Portrait -> {

                    }

                    OrientationState.Landscape -> {
                        when (networkState) {
                            NetworkState.Available -> {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = SurfaceBackgroundColor) {

                                }
                            }

                            NetworkState.Losing -> {
                                NetworkStateScreen(networkState = "Losing Network")
                            }

                            NetworkState.Lost -> {
                                NetworkStateScreen(networkState = "Network Lost")
                            }

                            NetworkState.Unavailable -> {
                                NetworkStateScreen(networkState = "Network Unavailable")
                            }
                        }
                    }
                }*/
            }
        }
    }
}


@Composable
fun ExpenseManagerApp(
    modifier: Modifier = Modifier,
    accessToken: String? = null,
    isEmailVerified: Boolean = false,
    currencyInstanceNumberFormat: NumberFormat
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentDestination?.route == "dashboard" || currentDestination?.route == "allTransactions") {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = SurfaceBackgroundColor
    ) { innerPadding ->
        val symbol = currencyInstanceNumberFormat.currency?.symbol
        NavigationGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            accessToken = accessToken,
            isEmailVerified = isEmailVerified,
            symbol = symbol ?: "$",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    val destinations = listOf(DashboardScreen, AllTransactions)

    NavigationBar(
        modifier = modifier, tonalElevation = 8.dp,
        containerColor = ComponentsBackgroundColor
    ) {
        destinations.forEach { screen: Screen ->
            BottomNavigationItem(
                destination = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }

}


@Composable
fun RowScope.BottomNavigationItem(
    destination: Screen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {

    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == destination.route
        } == true,
        onClick = {
            navController.navigate(route = destination.route) {
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                imageVector = destination.icon!!,
                contentDescription = "${destination.label!!} Icon",
            )
        },
        label = {
            Text(
                text = destination.label!!,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold))
                )
            )
        },
        alwaysShowLabel = false,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = BottomNavigationBarItemIndicatorColor,
            selectedIconColor = BottomNavigationBarItemSelectedColor,
            selectedTextColor = BottomNavigationBarItemSelectedColor,
            unselectedIconColor = DetailsTextColor,
            unselectedTextColor = BottomNavigationBarItemUnselectedColor
        )
    )

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}
