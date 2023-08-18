package com.pcandroiddev.expensemanager.ui.screens.transaction

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pcandroiddev.expensemanager.data.local.transaction.TransactionType
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.EditTransactionUIEvent
import com.pcandroiddev.expensemanager.utils.Helper
import com.pcandroiddev.expensemanager.viewmodels.EditTransactionViewModel

private const val TAG = "EditTransactionScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    editTransactionViewModel: EditTransactionViewModel = hiltViewModel(),
    onNavigateUpClicked: () -> Unit,
    onSuccessfulUpdateCallback: () -> Unit
) {

    Log.d(
        TAG,
        "EditTransactionScreen: ${editTransactionViewModel.editTransactionUIState.value}"
    )

    val context = LocalContext.current
    val updateTransactionState =
        editTransactionViewModel.updateTransactionState.collectAsState(initial = null)


    Scaffold(
        topBar = {
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
                    IconButton(onClick = {
                        //TODO: Reset the respective UI State
                        onNavigateUpClicked()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = HeadingTextColor,
                            contentDescription = "Back Button"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceBackgroundColor
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = SurfaceBackgroundColor
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                /*TopAppBar(
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
                        IconButton(onClick = {
                            //TODO: Reset the respective UI State
                            onNavigateUpClicked()
                        }) {
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
                )*/

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TransactionTypeSegmentedButton(
                        defaultSelectedItemIndex = when (editTransactionViewModel.editTransactionUIState.value.transactionType) {
                            TransactionType.INCOME.name -> {
                                0
                            }

                            TransactionType.EXPENSE.name -> {
                                1
                            }

                            else -> {
                                1
                            }
                        },
                        onSelectionChange = { transactionType ->
                            editTransactionViewModel.onEventChange(
                                event = EditTransactionUIEvent.TransactionTypeChanged(
                                    transactionType = transactionType
                                )
                            )
                        }
                    )


                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)

                ) {
                    TransactionTitleTextFieldComponent(
                        defaultTitle = editTransactionViewModel.editTransactionUIState.value.title,
                        errorStatus = editTransactionViewModel.editTransactionUIState.value.titleError,
                        onTextChanged = { title ->
                            editTransactionViewModel.onEventChange(
                                event = EditTransactionUIEvent.TitleChanged(
                                    title
                                )
                            )
                        }
                    )


                    TransactionAmountTextFieldComponent(
                        defaultAmount = editTransactionViewModel.editTransactionUIState.value.amount.toString(),
                        errorStatus = editTransactionViewModel.editTransactionUIState.value.amountError,
                        onTextChanged = { amount ->
                            if (amount.isNotBlank() || amount.isNotEmpty()) {
                                val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                                editTransactionViewModel.onEventChange(
                                    event = EditTransactionUIEvent.AmountChanged(
                                        parsedAmount
                                    )
                                )
                            } else {
                                editTransactionViewModel.onEventChange(
                                    event = EditTransactionUIEvent.AmountChanged(
                                        0.0
                                    )
                                )
                            }
                        }
                    )


                    TransactionCategoryMenuComponent(
                        defaultSelectedCategory = editTransactionViewModel.editTransactionUIState.value.category,
                        errorStatus = editTransactionViewModel.editTransactionUIState.value.categoryError,
                        onSelectionChanged = { category ->
                            editTransactionViewModel.onEventChange(
                                event = EditTransactionUIEvent.CategoryChanged(
                                    category
                                )
                            )
                        }
                    )

//              TODO: Remember to change the defaultDate to LocalDate format
                    /*TransactionDateComponent(
                        defaultSelectedDate = Helper.stringToLocalDate(editTransactionViewModel.editTransactionUIState.value.date),
                        onDateChanged = { date ->
                            editTransactionViewModel.onEventChange(
                                event = EditTransactionUIEvent.DateChanged(
                                    date
                                )
                            )
                        }
                    )*/

                    TransactionDatePicker(
                        defaultSelectedDate = Helper.stringToLocalDate(
                            editTransactionViewModel.editTransactionUIState.value.date
                        ),
                        onDateChanged = { date ->
                            editTransactionViewModel.onEventChange(
                                event = EditTransactionUIEvent.DateChanged(
                                    date
                                )
                            )
                        })


                    TransactionNoteComponent(
                        defaultNote = editTransactionViewModel.editTransactionUIState.value.note,
                        errorStatus = editTransactionViewModel.editTransactionUIState.value.noteError,
                        onTextChanged = { note ->
                            editTransactionViewModel.onEventChange(
                                event = EditTransactionUIEvent.NoteChanged(
                                    note
                                )
                            )
                        }
                    )


                    //TODO: Make it a loading button
                    SaveTransactionButton(
                        isEnable = editTransactionViewModel.allValidationPassed.value,
                        onButtonClicked = {
                            editTransactionViewModel.onEventChange(
                                event = EditTransactionUIEvent.EditTransactionButtonClicked
                            )
                        }

                    )

                }

            }
        }

    }

    LaunchedEffect(key1 = updateTransactionState.value?.isSuccess) {
        val success = updateTransactionState.value?.isSuccess
        if (success != null && success == "Transaction Updated!") {
            Log.d(TAG, "EditTransactionScreen/isSuccess: $success")
            onSuccessfulUpdateCallback()
        }
    }

    LaunchedEffect(key1 = updateTransactionState.value?.isError) {
        val error = updateTransactionState.value?.isError
        if (!error.isNullOrBlank()) {
            Log.d(TAG, "EditTransactionScreen/isError: $error")
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }


    /* BackHandler {
 //        ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
         navController.popBackStack(Screen.DashboardScreen.route, inclusive = false)
     }
 */
}

@Preview
@Composable
fun EditTransactionScreenPreview() {
    EditTransactionScreen(
        onNavigateUpClicked = {

        },
        onSuccessfulUpdateCallback = {

        }
    )
}
