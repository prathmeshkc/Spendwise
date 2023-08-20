@file:OptIn(ExperimentalMaterial3Api::class)

package com.pcandroiddev.expensemanager.ui.screens.transaction

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.local.transaction.TransactionType
import com.pcandroiddev.expensemanager.ui.components.SegmentedControl
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemSelectedColor
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.DisabledButtonColor
import com.pcandroiddev.expensemanager.ui.theme.DisabledTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.LinkColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.AddTransactionUIEvent
import com.pcandroiddev.expensemanager.utils.Helper
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
fun AddTransactionScreen(
    addTransactionViewModel: AddTransactionViewModel = hiltViewModel(),
    onNavigateUpClicked: () -> Unit,
    onSaveTransactionClicked: () -> Unit
) {

    val context = LocalContext.current
    val createTransactionState =
        addTransactionViewModel.createTransactionState.collectAsState(initial = null)


    Scaffold(
        topBar = {
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
                    IconButton(onClick = {
//                        ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
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
                            text = "Add Transaction",
                            color = DetailsTextColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
//                        ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
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
                        defaultSelectedItemIndex = 1,
                        onSelectionChange = { transactionType ->
                            addTransactionViewModel.onEventChange(
                                event = AddTransactionUIEvent.TransactionTypeChanged(
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

                            if (amount.isNotBlank() || amount.isNotEmpty()) {
                                val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                                addTransactionViewModel.onEventChange(
                                    event = AddTransactionUIEvent.AmountChanged(
                                        parsedAmount
                                    )
                                )
                            } else {
                                addTransactionViewModel.onEventChange(
                                    event = AddTransactionUIEvent.AmountChanged(
                                        0.0
                                    )
                                )
                            }

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


                    /*TransactionDateComponent(
                        onDateChanged = { date ->
                            addTransactionViewModel.onEventChange(
                                event = AddTransactionUIEvent.DateChanged(
                                    date
                                )
                            )
                        }
                    )*/

                    TransactionDatePicker(
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


                    //TODO: Make it a loading button
                    SaveTransactionButton(
                        isEnable = addTransactionViewModel.allValidationPassed.value,
                        onButtonClicked = {
                            addTransactionViewModel.onEventChange(event = AddTransactionUIEvent.SaveTransactionButtonClicked)
                        }
                    )

                }
            }
        }
    }

    LaunchedEffect(key1 = createTransactionState.value?.isSuccess) {
        val success = createTransactionState.value?.isSuccess
        if (success != null && success == "Transaction Created!") {
            Log.d(TAG, "AddTransactionScreen/isSuccess: $success")
            onSaveTransactionClicked()
        }
    }

    LaunchedEffect(key1 = createTransactionState.value?.isError) {
        val error = createTransactionState.value?.isError
        if (!error.isNullOrBlank()) {
            Log.d(TAG, "AddTransactionScreen/isError: $error")
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    /*BackHandler {
//        ExpenseManagerRouter.navigateTo(destination = Screen.DashboardScreen)
        navController.popBackStack()
    }*/
}

@Composable
fun TransactionTypeSegmentedButton(
    defaultSelectedItemIndex: Int,
    onSelectionChange: (String) -> Unit
) {

    val transactionTypeItems = listOf("INCOME", "EXPENSE")
    SegmentedControl(
        items = transactionTypeItems,
        defaultSelectedItemIndex = defaultSelectedItemIndex,
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


@Composable
fun TransactionTitleTextFieldComponent(
    defaultTitle: String = "",
    errorStatus: Pair<Boolean, String> = Pair(false, ""),
    onTextChanged: (String) -> Unit
) {


    var transactionTitle by remember {
        mutableStateOf(defaultTitle)
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
            if (isFocused && !errorStatus.first) {
                Text(
                    text = errorStatus.second,
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isFocused && !errorStatus.first,
        trailingIcon = {
            if (isFocused && !errorStatus.first) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = errorStatus.second
                )
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceBackgroundColor,
            unfocusedContainerColor = SurfaceBackgroundColor,
            disabledContainerColor = SurfaceBackgroundColor,
            cursorColor = Color.White,
            focusedBorderColor = FABColor,
            unfocusedBorderColor = DetailsTextColor,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = DetailsTextColor,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),


        )
}


@Composable
fun TransactionAmountTextFieldComponent(
    defaultAmount: String = "",
    errorStatus: Pair<Boolean, String> = Pair(false, ""),
    onTextChanged: (String) -> Unit
) {

    var transactionAmount by remember {
        mutableStateOf(defaultAmount)
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    val localFocusManager = LocalFocusManager.current

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
            if (isFocused && !errorStatus.first) {
                Text(
                    text = errorStatus.second,
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isFocused && !errorStatus.first,
        trailingIcon = {
            if (isFocused && !errorStatus.first) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = errorStatus.second
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SurfaceBackgroundColor,
            unfocusedContainerColor = SurfaceBackgroundColor,
            focusedTextColor = DetailsTextColor,
            unfocusedTextColor = DetailsTextColor,
            focusedBorderColor = FABColor,
            unfocusedBorderColor = DetailsTextColor,
            cursorColor = Color.White,
            focusedLeadingIconColor = DetailsTextColor,
            unfocusedLeadingIconColor = DetailsTextColor,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = DetailsTextColor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
            isFocused = false
        },

        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCategoryMenuComponent(
    defaultSelectedCategory: String = "",
    errorStatus: Pair<Boolean, String> = Pair(false, ""),
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

    var selectedCategory by remember { mutableStateOf(defaultSelectedCategory) }

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
            isError = isFocused && !errorStatus.first,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceBackgroundColor,
                unfocusedContainerColor = SurfaceBackgroundColor,
                disabledContainerColor = SurfaceBackgroundColor,
                focusedBorderColor = FABColor,
                unfocusedBorderColor = DetailsTextColor,
                focusedTrailingIconColor = FABColor,
                unfocusedTrailingIconColor = DetailsTextColor,
                focusedLabelColor = FABColor,
                unfocusedLabelColor = DetailsTextColor,
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


@Composable
fun TransactionDateComponent(
    defaultSelectedDate: LocalDate = LocalDate.now(),
    onDateChanged: (String) -> Unit
) {

    var selectedDate by remember {
        mutableStateOf(defaultSelectedDate)
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
        colors = TextFieldDefaults.colors(
            disabledTextColor = DetailsTextColor,
            focusedContainerColor = SurfaceBackgroundColor,
            unfocusedContainerColor = SurfaceBackgroundColor,
            disabledContainerColor = SurfaceBackgroundColor,
            disabledIndicatorColor = HeadingTextColor,
            disabledLabelColor = DetailsTextColor,
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
fun TransactionDatePicker(
    defaultSelectedDate: LocalDate = LocalDate.now(),
    onDateChanged: (String) -> Unit
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

    var selectedDate by remember {
        mutableStateOf(defaultSelectedDate)
    }

    val formattedDate by remember {
        derivedStateOf {
            Helper.getFormattedStringDateFromLocalDate(selectedDate)
        }
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                openDialog = true
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
        colors = TextFieldDefaults.colors(
            disabledTextColor = DetailsTextColor,
            focusedContainerColor = SurfaceBackgroundColor,
            unfocusedContainerColor = SurfaceBackgroundColor,
            disabledContainerColor = SurfaceBackgroundColor,
            disabledIndicatorColor = HeadingTextColor,
            disabledLabelColor = DetailsTextColor,
        ),

        )

    if (openDialog) {
        val datePickerState = rememberDatePickerState()

        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        selectedDate =
                            Helper.getLocalDateFromLong(datePickerState.selectedDateMillis!!)
                        onDateChanged(formattedDate)
                        Log.d(TAG, "TransactionDatePicker milliseconds: ${datePickerState.selectedDateMillis}")
                        Log.d(TAG, "TransactionDatePicker formattedDate: $formattedDate")
                    },
                    enabled = confirmEnabled,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = FABColor,
                        disabledContentColor = DisabledButtonColor
                    )
                ) {
                    Text(
                        text = "Confirm",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                            fontSize = 16.sp
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        /*TODO: Pass the value to the onConfirmClicked*/
                        openDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = FABColor,
                        disabledContentColor = DisabledButtonColor
                    )
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                            fontSize = 16.sp
                        )
                    )
                }
            },
            colors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = ComponentsBackgroundColor,
            )
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(top = 12.dp),
                            text = "Select Transaction Date",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                            )
                        )
                    }
                },
                showModeToggle = false,
                colors = androidx.compose.material3.DatePickerDefaults.colors(
                    containerColor = ComponentsBackgroundColor,
                    titleContentColor = DetailsTextColor,
                    headlineContentColor = DetailsTextColor,
                    weekdayContentColor = DetailsTextColor,
                    subheadContentColor = DetailsTextColor,
                    yearContentColor = DetailsTextColor,
                    currentYearContentColor = FABColor,
                    selectedYearContentColor = DetailsTextColor,
                    selectedYearContainerColor = FABColor,
                    dayContentColor = DetailsTextColor,
                    selectedDayContentColor = DetailsTextColor,
                    selectedDayContainerColor = LinkColor,
                    todayContentColor = BottomNavigationBarItemSelectedColor,
                    todayDateBorderColor = BottomNavigationBarItemSelectedColor,
                )
            )
        }

    }

}


@Composable
fun TransactionNoteComponent(
    defaultNote: String = "",
    errorStatus: Pair<Boolean, String> = Pair(false, ""),
    onTextChanged: (String) -> Unit
) {

    val localFocusManager = LocalFocusManager.current

    var transactionNote by remember {
        mutableStateOf(defaultNote)
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
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
        isError = isFocused && !errorStatus.first,
        supportingText = {
            if (isFocused && !errorStatus.first) {
                Text(
                    text = errorStatus.second,
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SurfaceBackgroundColor,
            unfocusedContainerColor = SurfaceBackgroundColor,
            disabledContainerColor = SurfaceBackgroundColor,
            errorContainerColor = SurfaceBackgroundColor,
            cursorColor = Color.White,
            focusedIndicatorColor = FABColor,
            unfocusedIndicatorColor = DetailsTextColor,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = DetailsTextColor,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },

        )

}

@Composable
fun SaveTransactionButton(
    isEnable: Boolean = false,
    onButtonClicked: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = { onButtonClicked() },
        enabled = isEnable,
        colors = ButtonDefaults.buttonColors(
            containerColor = FABColor,
            disabledContainerColor = DisabledButtonColor,
            contentColor = DetailsTextColor,
            disabledContentColor = DisabledTextColor

        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 12.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                tint = Color.White,
                contentDescription = "Save Transaction"
            )
            Text(
                text = "SAVE TRANSACTION",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsTextColor
            )
        }
    }
}


@Preview
@Composable
fun AddTransactionScreenPreview() {


    /*TransactionTypeSegmentedButton(
        modifier = Modifier
            .padding(horizontal = 70.dp, vertical = 16.dp)
            .fillMaxWidth(),
        onSelectionChange = {
            Log.d(TAG, "AddTransactionScreenPreview: $it")
        }
    )*/

    /*Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TransactionTypeSegmentedButton(
            defaultSelectedItemIndex = 1,
            onSelectionChange = { transactionType ->

            }
        )
    }*/

    /*TransactionDatePicker() {

    }*/

}
