package com.pcandroiddev.expensemanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.navigation.NavigationGraph
import com.pcandroiddev.expensemanager.ui.screens.network.NetworkStateScreen
import com.pcandroiddev.expensemanager.ui.theme.ExpenseManagerTheme
import com.pcandroiddev.expensemanager.utils.NetworkState
import com.pcandroiddev.expensemanager.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val mainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            token = tokenManager.getToken()
        }
        setContent {

            ExpenseManagerTheme {


                val networkState by mainViewModel.networkState.collectAsStateWithLifecycle()
                when (networkState) {
                    NetworkState.Available -> {
                        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
                        val symbol = numberFormat.currency?.symbol
                        NavigationGraph(accessToken = token, symbol = symbol ?: "$")
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
}
