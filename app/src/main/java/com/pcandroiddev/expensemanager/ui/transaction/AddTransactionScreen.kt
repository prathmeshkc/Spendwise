package com.pcandroiddev.expensemanager.ui.transaction

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.local.TransactionType
import com.pcandroiddev.expensemanager.navigation.ExpenseManagerRouter
import com.pcandroiddev.expensemanager.navigation.Screen
import com.pcandroiddev.expensemanager.navigation.SystemBackButtonHandler
import com.pcandroiddev.expensemanager.ui.custom.SegmentedControl
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.AddTransactionUIEvent
import com.pcandroiddev.expensemanager.viewmodels.AddTransactionViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//TODO: Add onValueChange for UIEvents

private const val TAG = "AddTransactionScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(addTransactionViewModel: AddTransactionViewModel = viewModel()) {
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
                        text = "Add Transaction",
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
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = ComponentsBackgroundColor
                )
            )

            TransactionTypeSegmentedButton(
                modifier = Modifier
                    .padding(horizontal = 70.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                onSelectionChange = { transactionType ->
                    addTransactionViewModel.onEventChange(
                        event = AddTransactionUIEvent.TransactionTypeChanged(
                            transactionType = transactionType
                        )
                    )
                }
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)

            ) {
                TransactionTitleTextFieldComponent(
                    errorStatus = addTransactionViewModel.addTransactionUIState.value.titleError,
                    onTextChanged = { title ->
                        addTransactionViewModel.onEventChange(
                            event = AddTransactionUIEvent.TitleChanged(
                                title
                            )
                        )
                    }
                )


                TransactionAmountTextFieldComponent(
                    errorStatus = addTransactionViewModel.addTransactionUIState.value.amountError,
                    onTextChanged = { amount ->
                        addTransactionViewModel.onEventChange(
                            event = AddTransactionUIEvent.AmountChanged(
                                amount
                            )
                        )
                    }
                )


                TransactionCategoryMenuComponent(
                    errorStatus = addTransactionViewModel.addTransactionUIState.value.categoryError,
                    onSelectionChanged = { category ->
                        addTransactionViewModel.onEventChange(
                            event = AddTransactionUIEvent.CategoryChanged(
                                category
                            )
                        )
                    }
                )


                TransactionDateComponent(
                    onDateChanged = { date ->
                        addTransactionViewModel.onEventChange(
                            event = AddTransactionUIEvent.DateChanged(
                                date
                            )
                        )
                    }
                )


                TransactionNoteComponent(
                    errorStatus = addTransactionViewModel.addTransactionUIState.value.noteError,
                    onTextChanged = { note ->
                        addTransactionViewModel.onEventChange(
                            event = AddTransactionUIEvent.NoteChanged(
                                note
                            )
                        )
                    }
                )


                SaveTransactionButton(
                    onButtonClicked = {
                        //TODO: Navigate Back to Dashboard. Handle in onButtonClicked Lambda
                        //TODO: Check if all the fields are filled
                        addTransactionViewModel.onEventChange(event = AddTransactionUIEvent.SaveTransactionButtonClicked)
                    }
                )

            }

        }


    }

    SystemBackButtonHandler {
        ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
    }
}

@Composable
fun TransactionTypeSegmentedButton(
    modifier: Modifier = Modifier,
    onSelectionChange: (String) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        val transactionTypeItems = listOf("INCOME", "EXPENSE")
        SegmentedControl(
            items = transactionTypeItems,
            defaultSelectedItemIndex = 1,
            useFixedWidth = true,
            itemWidth = 120.dp
        ) {
            val transactionType: String =
                if (it == 0) TransactionType.INCOME.name
                else TransactionType.EXPENSE.name
            Log.e(TAG, "Selected item : $transactionType")
            onSelectionChange(transactionType)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTitleTextFieldComponent(
    errorStatus: Boolean = false,
    onTextChanged: (String) -> Unit
) {


    var transactionTitle by remember {
        mutableStateOf("")
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    /* val isErrorInTitle by remember {
         derivedStateOf {
             if (!isFocused && transactionTitle.isEmpty()) {
                 false
             } else {
                 transactionTitle.isEmpty()
             }
         }
     }*/



    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    isFocused = true
                }
            },
        value = transactionTitle,
        onValueChange = {
            transactionTitle = it
            onTextChanged(it)
        },
        textStyle = TextStyle(
            color = DetailsTextColor,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 15.sp
        ),
        label = {
            Text(
                text = "Title",
                fontFamily = FontFamily(Font(R.font.inter_regular))
            )
        },
        placeholder = {
            Text(
                text = "Enter Transaction Title",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = HeadingTextColor
            )
        },
        supportingText = {
            if (isFocused && !errorStatus) {
                Text(
                    text = "Title must not be empty",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isFocused && !errorStatus,
        trailingIcon = {
            if (isFocused && !errorStatus) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Title Empty Error"
                )
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = SurfaceBackgroundColor,
            focusedBorderColor = FABColor,
            cursorColor = Color.White,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = HeadingTextColor
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),


        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAmountTextFieldComponent(
    errorStatus: Boolean = false,
    onTextChanged: (String) -> Unit
) {

    var transactionAmount by remember {
        mutableStateOf("")
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    /* val isErrorInAmount by remember {
         derivedStateOf {
             if (!isFocused && transactionAmount.isEmpty()) {
                 false
             } else {
                 transactionAmount.isEmpty()
             }
         }
     }
 */

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    isFocused = true
                }
            },
        value = transactionAmount,
        onValueChange = {
            transactionAmount = it
            onTextChanged(it)
        },
        textStyle = TextStyle(
            color = DetailsTextColor,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 15.sp
        ),
        label = {
            Text(
                text = "Amount",
                fontFamily = FontFamily(Font(R.font.inter_regular)),

                )
        },
        placeholder = {
            Text(
                text = "Enter Transaction Amount",
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                color = HeadingTextColor
            )
        },
        supportingText = {
            if (isFocused && !errorStatus) {
                Text(
                    text = "Amount must not be empty",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isFocused && !errorStatus,
        trailingIcon = {
            if (isFocused && !errorStatus) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Amount Empty Error"
                )
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = "Dollar Sign"
            )
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = SurfaceBackgroundColor,
            textColor = DetailsTextColor,
            focusedBorderColor = FABColor,
            cursorColor = Color.White,
            focusedLeadingIconColor = DetailsTextColor,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = HeadingTextColor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),

        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCategoryMenuComponent(
    errorStatus: Boolean = false,
    onSelectionChanged: (String) -> Unit
) {
    val transactionCategory = listOf(
        "Entertainment", "Food", "Healthcare",
        "Housing", "Insurance", "Miscellaneous",
        "Personal Spending", "Savings & Debts",
        "Transportation", "Utilities"
    )

    var isFocused by remember {
        mutableStateOf(false)
    }

    var isExpanded by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = !isExpanded
        }
    ) {

        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { },
            textStyle = TextStyle(
                color = DetailsTextColor,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 15.sp
            ),
            readOnly = true,
            label = {
                Text(
                    text = "Category",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            },
            isError = isFocused && !errorStatus,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = SurfaceBackgroundColor,
                focusedBorderColor = FABColor,
                focusedTrailingIconColor = FABColor,
                unfocusedTrailingIconColor = HeadingTextColor,
                focusedLabelColor = FABColor,
                unfocusedLabelColor = HeadingTextColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        isFocused = true
                    }
                }
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(SurfaceBackgroundColor),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            transactionCategory.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = filter,
                            color = DetailsTextColor,
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start
                        )
                    },
                    onClick = {
                        selectedCategory = filter
                        isExpanded = false
                        onSelectionChanged(filter)
                        Log.d(
                            TAG,
                            "TransactionCategoryMenuComponent selectedCategory: $selectedCategory"
                        )
                    })
            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDateComponent(
    onDateChanged: (String) -> Unit
) {

    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd, yyyy")
                .format(selectedDate)
        }
    }

    val dateDialogState = rememberMaterialDialogState()

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                dateDialogState.show()
            },
        enabled = false,
        value = formattedDate,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(
                text = "Date",
                fontFamily = FontFamily(Font(R.font.inter_regular))
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                tint = HeadingTextColor,
                contentDescription = "Date Picker"
            )
        },
        textStyle = TextStyle(
            color = DetailsTextColor,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 15.sp
        ),
        colors = TextFieldDefaults.textFieldColors(
            disabledIndicatorColor = HeadingTextColor,
            disabledTextColor = DetailsTextColor,
            disabledLabelColor = DetailsTextColor,
            containerColor = SurfaceBackgroundColor,

            ),

        )

    MaterialDialog(
        dialogState = dateDialogState,
        backgroundColor = ComponentsBackgroundColor,
        elevation = 8.dp,
        buttons = {
            positiveButton(
                text = "OK",
                textStyle = TextStyle(
                    color = FABColor,
                    fontWeight = FontWeight.SemiBold
                )
            )
            negativeButton(
                text = "CANCEL",
                textStyle = TextStyle(
                    color = FABColor,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Transaction Date",
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = Color(R.color.date_picker_header_bg_color),
                headerTextColor = DetailsTextColor,
                dateActiveBackgroundColor = FABColor,
                dateInactiveTextColor = DetailsTextColor,
                calendarHeaderTextColor = DetailsTextColor
            )
        ) {
            selectedDate = it
            onDateChanged(formattedDate)
            Log.d(TAG, "TransactionDateComponent formattedDate: $formattedDate")
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionNoteComponent(
    errorStatus: Boolean = false,
    onTextChanged: (String) -> Unit
) {

    val localFocusManager = LocalFocusManager.current

    var transactionNote by remember {
        mutableStateOf("")
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if(focusState.isFocused) {
                    isFocused = true
                }
            },
        value = transactionNote,
        onValueChange = {
            transactionNote = it
            onTextChanged(it)
        },
        textStyle = TextStyle(
            color = DetailsTextColor,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 15.sp
        ),
        singleLine = true,
        maxLines = 1,
        label = {
            Text(
                text = "Transaction Note",
                fontFamily = FontFamily(Font(R.font.inter_regular))
            )
        },
        isError = isFocused && !errorStatus,
        supportingText = {
            if (isFocused && !errorStatus) {
                Text(
                    text = "Only whitespaces not allowed!",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = SurfaceBackgroundColor,
            disabledIndicatorColor = HeadingTextColor,
            focusedIndicatorColor = FABColor,
            cursorColor = Color.White,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = HeadingTextColor

        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },

        )

}

@Composable
fun SaveTransactionButton(
    onButtonClicked: () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(),
        text = {
            Text(
                text = "SAVE TRANSACTION",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsTextColor
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Check,
                tint = Color.White,
                contentDescription = "Save Transaction"
            )
        },
        onClick = {
            onButtonClicked.invoke()
//            ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
            Log.d(TAG, "SaveTransactionButton: Save Transaction Button Clicked")
        },
        containerColor = FABColor
    )

}


@Preview
@Composable
fun AddTransactionScreenPreview() {

//    AddTransactionScreen()

    TransactionTypeSegmentedButton(
        modifier = Modifier
            .padding(horizontal = 70.dp, vertical = 16.dp)
            .fillMaxWidth(),
        onSelectionChange = {
            Log.d(TAG, "AddTransactionScreenPreview: $it")
        }
    )

}
