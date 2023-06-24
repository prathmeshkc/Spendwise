@file:OptIn(ExperimentalMaterial3Api::class)

package com.pcandroiddev.expensemanager.ui.screens.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.navigation.ExpenseManagerRouter
import com.pcandroiddev.expensemanager.navigation.Screen
import com.pcandroiddev.expensemanager.navigation.SystemBackButtonHandler
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor


@Composable
fun TransactionDetailsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            DetailsTopAppBar()
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DetailComponent(heading = "Title", value = "Phone bill")
                DetailComponent(heading = "Amount", value = "15.94")
                DetailComponent(heading = "Transaction Type", value = "Expense")
                DetailComponent(heading = "Category", value = "Utilities")
                DetailComponent(heading = "When", value = "05/01/2023")
                DetailComponent(heading = "Note", value = "T-Mobile Prepaid Recharge")
                DetailComponent(heading = "Created At", value = "May 19, 2023, 5:10 PM")
                EditFAB(
                    modifier = Modifier
                        .padding(bottom = 31.dp, end = 16.dp, top = 50.dp)
                        .align(Alignment.End)
                )

            }
        }

    }

    SystemBackButtonHandler {
        ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
    }
}

@Composable
fun DetailsTopAppBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "Details",
                color = DetailsTextColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = HeadingTextColor,
                    contentDescription = "Back Button"
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = DetailsTextColor,
                    contentDescription = ""
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    tint = DetailsTextColor,
                    contentDescription = ""
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ComponentsBackgroundColor
        )
    )
}

@Composable
fun DetailComponent(heading: String, value: String) {
    Column(modifier = Modifier) {
        Text(
            text = heading,
            fontSize = 12.sp,
            fontWeight = FontWeight(400),
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            color = HeadingTextColor
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = DetailsTextColor
        )
    }

}

@Composable
fun EditFAB(modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text("EDIT", color = Color.White) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Edit,
                tint = Color.White,
                contentDescription = "Edit Transaction"
            )
        },
        shape = RoundedCornerShape(50),
        onClick = { ExpenseManagerRouter.navigateTo(destination = Screen.EditTransactionScreen) },
        containerColor = FABColor
    )
}


@Preview
@Composable
fun TransactionDetailsPreview() {
    TransactionDetailsScreen()
}
