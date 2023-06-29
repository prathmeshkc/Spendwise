package com.pcandroiddev.expensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import com.pcandroiddev.expensemanager.navigation.NavigationGraph
import com.pcandroiddev.expensemanager.ui.theme.ExpenseManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
                NavigationGraph(accessToken = token)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    ExpenseManagerApp()
}
