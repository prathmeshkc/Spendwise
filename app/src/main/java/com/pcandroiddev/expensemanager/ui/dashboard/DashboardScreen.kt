package com.pcandroiddev.expensemanager.ui.dashboard

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.navigation.ExpenseManagerRouter
import com.pcandroiddev.expensemanager.navigation.Screen
import com.pcandroiddev.expensemanager.navigation.SystemBackButtonHandler
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor

@Composable
fun DashboardScreen() {
    Box(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {


        Column(modifier = Modifier.fillMaxSize()) {
            DashboardTopBar()

            TotalBalanceCard(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                    .fillMaxWidth()

            )

            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TotalIncomeCard()
                TotalExpenseCard()
            }

            Text(
                modifier = Modifier.padding(top = 23.dp, start = 24.dp),
                text = "Recent Transaction",
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = DetailsTextColor
            )

            LazyColumn(content = {
                items(9) {
                    TransactionListItem()
                }
            })


        }

        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = 31.dp, end = 33.dp)
                .align(Alignment.BottomEnd),
            containerColor = FABColor,
            onClick = {
                ExpenseManagerRouter.navigateTo(destination = Screen.AddTransactionScreen)
            }

        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add Transaction",
                tint = DetailsTextColor

            )

        }

    }
}


@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}