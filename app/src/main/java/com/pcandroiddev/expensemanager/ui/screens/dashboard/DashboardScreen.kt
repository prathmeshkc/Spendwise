package com.pcandroiddev.expensemanager.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
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
import com.pcandroiddev.expensemanager.ui.components.ExpensesSearchBar
import com.pcandroiddev.expensemanager.ui.components.TotalBalanceCard
import com.pcandroiddev.expensemanager.ui.components.TotalExpenseCard
import com.pcandroiddev.expensemanager.ui.components.TotalIncomeCard
import com.pcandroiddev.expensemanager.ui.components.TransactionListItem
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.utils.isScrollingUp

@Composable
fun DashboardScreen() {
    val listState = rememberLazyListState()
    var isSearchBarActive by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
//            DashboardTopBar(isDarkMode = true)
        },
        floatingActionButton = {
            if (!isSearchBarActive) {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = 30.dp, end = 30.dp),
                    containerColor = FABColor,
                    text = {
                        Text(
                            text = "Add Transaction",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.inter_regular)),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = DetailsTextColor
                            ),

                            )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Receipt,
                            contentDescription = "Add Transaction",
                            tint = DetailsTextColor
                        )
                    },
                    onClick = {
                        ExpenseManagerRouter.navigateTo(destination = Screen.AddTransactionScreen)
                    },
                    expanded = listState.isScrollingUp()

                )
            }
        },
        containerColor = SurfaceBackgroundColor

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            ExpensesSearchBar(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                    .fillMaxWidth()
            ) { isActive ->
                isSearchBarActive = isActive
            }

            TotalBalanceCard(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                labelText = "TOTAL BALANCE",
                amountText = "1807"
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TotalIncomeCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(124.dp),
                    amountText = "3574.00"
                )
                TotalExpenseCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(124.dp),
                    amountText = "1767.00"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(top = 12.dp, start = 24.dp),
                    text = "Recent Transaction",
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = DetailsTextColor
                )

                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable {
                            //TODO: Navigate to All Transaction Screen
                        },
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = "View All Transactions",
                    tint = FABColor
                )
            }

            TransactionList(listState = listState)


        }
    }
}


@Composable
fun TransactionList(
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        content = {
            items(9) {
                TransactionListItem()
            }
        })
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}