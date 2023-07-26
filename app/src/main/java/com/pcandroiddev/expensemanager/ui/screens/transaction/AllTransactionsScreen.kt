package com.pcandroiddev.expensemanager.ui.screens.transaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor

@Composable
fun AllTransactions(
    onBackPressed: () -> Unit
) {

    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = SurfaceBackgroundColor
        ) {
            Text(
                text = "All Transactions",
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }

    }


    BackHandler {
        onBackPressed()
    }
}
