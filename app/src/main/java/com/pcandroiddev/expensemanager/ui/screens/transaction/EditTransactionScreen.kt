package com.pcandroiddev.expensemanager.ui.screens.transaction

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pcandroiddev.expensemanager.navigation.ExpenseManagerRouter
import com.pcandroiddev.expensemanager.navigation.Screen
import com.pcandroiddev.expensemanager.navigation.SystemBackButtonHandler
import com.pcandroiddev.expensemanager.ui.components.SegmentedControl
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor


//TODO: Pass in the ViewModel, which will provide the Transaction object.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SurfaceBackgroundColor
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Edit Transaction",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ComponentsBackgroundColor
                )
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 70.dp, vertical = 16.dp)
                    .fillMaxWidth(),
            ) {
                val transactionType = listOf("INCOME", "EXPENSE")
                SegmentedControl(
                    items = transactionType,
                    defaultSelectedItemIndex = 1,
                    useFixedWidth = true,
                    itemWidth = 120.dp
                ) {
                    Log.e("CustomToggle", "Selected item : ${transactionType[it]}")
                }


            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)

            ) {
                TransactionTitleTextFieldComponent(
                    onTextChanged = { title ->

                    }
                )


                TransactionAmountTextFieldComponent(
                    onTextChanged = { amount ->

                    }
                )


                TransactionCategoryMenuComponent(
                    onSelectionChanged = { category ->

                    }
                )


                TransactionDateComponent(
                    onDateChanged = { date ->

                    }
                )


                TransactionNoteComponent(
                    onTextChanged = { note ->

                    }
                )


                SaveTransactionButton(
                    onButtonClicked = {

                    }
                )

            }

        }


    }

    SystemBackButtonHandler {
        ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
    }

}

@Preview
@Composable
fun EditTransactionScreenPreview() {
    EditTransactionScreen()
}