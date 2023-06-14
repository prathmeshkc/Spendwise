package com.pcandroiddev.expensemanager.ui.transaction

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.focus.FocusDirection
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
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.ui.custom.SegmentedControl
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//TODO: Handle top app bar navigationIcon
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen() {
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
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = HeadingTextColor,
                            contentDescription = "Back Button"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                TransactionTitleTextFieldComponent()
                TransactionAmountTextFieldComponent()

                TransactionTypeMenuComponent()
                TransactionDateComponent()
                TransactionNoteComponent()
                SaveTransactionButton()

            }

        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTitleTextFieldComponent() {

    val localFocusManager = LocalFocusManager.current

    var transactionTitle by remember {
        mutableStateOf("")
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    val isErrorInTitle by remember {
        derivedStateOf {
            if (!isFocused && transactionTitle.isEmpty()) {
                false
            } else {
                transactionTitle.isEmpty()
            }
        }
    }



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
            if (isErrorInTitle) {
                Text(
                    text = "Title must not be empty",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isErrorInTitle,
        trailingIcon = {
            if (isErrorInTitle) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Title Empty Error"
                )
            }
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = SurfaceBackgroundColor,

            focusedBorderColor = FABColor,
            cursorColor = Color.White,
            focusedLabelColor = FABColor,
            unfocusedLabelColor = HeadingTextColor
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions {
            localFocusManager.moveFocus(FocusDirection.Down)
        },

        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAmountTextFieldComponent() {
    val localFocusManager = LocalFocusManager.current

    var transactionAmount by remember {
        mutableStateOf("")
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    val isErrorInAmount by remember {
        derivedStateOf {
            if (!isFocused && transactionAmount.isEmpty()) {
                false
            } else {
                transactionAmount.isEmpty()
            }
        }
    }


    OutlinedTextField(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    isFocused = true
                }
            },
        value = transactionAmount,
        onValueChange = {
            transactionAmount = it
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
            if (isErrorInAmount) {
                Text(
                    text = "Title must not be empty",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            }
        },
        isError = isErrorInAmount,
        trailingIcon = {
            if (isErrorInAmount) {
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
        keyboardActions = KeyboardActions {
            localFocusManager.moveFocus(FocusDirection.Down)
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeMenuComponent() {
    val tags = listOf(
        "Entertainment", "Food", "Healthcare",
        "Housing", "Insurance", "Miscellaneous",
        "Personal Spending", "Savings & Debts",
        "Transportation", "Utilities"
    )

    var isExpanded by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf(tags[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = !isExpanded
        }
    ) {

        OutlinedTextField(
            value = selectedFilter,
            onValueChange = { },
            textStyle = TextStyle(
                color = DetailsTextColor,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 15.sp
            ),
            readOnly = true,
            label = {
                Text(
                    text = "Tag",
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            },
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
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(SurfaceBackgroundColor),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            tags.forEach { filter ->
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
                        selectedFilter = filter
                        isExpanded = false
                    })
            }
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDateComponent() {

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
            .padding(top = 40.dp)
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
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionNoteComponent() {

    val localFocusManager = LocalFocusManager.current

    var transactionNote by remember {
        mutableStateOf("")
    }

    TextField(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        value = transactionNote,
        onValueChange = {
            transactionNote = it
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
fun SaveTransactionButton() {
    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(top = 40.dp)
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
        onClick = { /*TODO*/ },
        containerColor = FABColor
    )

}


@Preview
@Composable
fun AddEditTransactionScreenPreview() {

    AddEditTransactionScreen()
    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBackgroundColor),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TransactionTitleTextFieldComponent()
        TransactionAmountTextFieldComponent()
        val transactionType = listOf("INCOME", "EXPENSE")
        SegmentedControl(
            items = transactionType,
            defaultSelectedItemIndex = 0,
            useFixedWidth = true,
            itemWidth = 120.dp
        ) {
            Log.e("CustomToggle", "Selected item : ${transactionType[it]}")
        }

        TransactionTypeMenuComponent()
        TransactionDateComponent()
        TransactionNoteComponent()
    }*/
}
