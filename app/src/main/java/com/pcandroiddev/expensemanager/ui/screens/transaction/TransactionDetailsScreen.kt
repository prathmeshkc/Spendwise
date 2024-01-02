@file:OptIn(ExperimentalMaterial3Api::class)

package com.pcandroiddev.expensemanager.ui.screens.transaction

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemSelectedColor
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.TransactionDetailsUIEvent
import com.pcandroiddev.expensemanager.viewmodels.DetailsViewModel


private const val TAG = "TransactionDetailsScreen"

@Composable
fun TransactionDetailsScreen(
    jsonTransactionResponse: String,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    onNavigateUpClicked: () -> Unit,
    onEditFABClicked: () -> Unit,
    onShareButtonClicked: (TransactionResponse) -> Unit,
    onDeleteTransactionButtonClicked: () -> Unit
) {

    val context = LocalContext.current

    val transactionResponse =
        Gson().fromJson(jsonTransactionResponse, TransactionResponse::class.java)

    val deleteTransactionState =
        detailsViewModel.deleteTransactionState.collectAsState(initial = null)


    Scaffold(
        topBar = {
            DetailsTopAppBar(
                onBackButtonClicked = {
                    onNavigateUpClicked()
                },
                onDeleteButtonClicked = {
                    detailsViewModel.onEventChange(
                        event = TransactionDetailsUIEvent.DeleteTransactionButtonClicked(
                            transactionId = transactionResponse.transactionId
                        )
                    )
                },
                onShareButtonClicked = {
                    onShareButtonClicked(transactionResponse)
                }
            )
        },
        floatingActionButton = {
            EditFAB(
                modifier = Modifier
                    .padding(bottom = 92.dp, end = 16.dp, top = 50.dp),
                onEditFABClicked = {
                    onEditFABClicked()
                }
            )
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = SurfaceBackgroundColor
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DetailComponent(heading = "Title", value = transactionResponse.title)
                DetailComponent(
                    heading = "Amount",
                    value = transactionResponse.amount.toString()
                )
                DetailComponent(
                    heading = "Transaction Type",
                    value = transactionResponse.transactionType
                )
                DetailComponent(heading = "Category", value = transactionResponse.category)
                DetailComponent(heading = "When", value = transactionResponse.transactionDate)
                if (transactionResponse.note.isNotEmpty()) {
                    DetailComponent(heading = "Note", value = transactionResponse.note)
                }
            }

        }
    }

    LaunchedEffect(key1 = deleteTransactionState.value?.isSuccess) {
        val success = deleteTransactionState.value?.isSuccess
        if (success != null && success == "Transaction Deleted!") {
            Log.d(TAG, "TransactionDetailsScreen/isSuccess: $success")
            onDeleteTransactionButtonClicked()
        }
    }

    LaunchedEffect(key1 = deleteTransactionState.value?.isError) {
        val error = deleteTransactionState.value?.isError
        if (!error.isNullOrBlank()) {
            Log.d(TAG, "TransactionDetailsScreen/isError: $error")
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }


    BackHandler {
        onNavigateUpClicked()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopAppBar(
    onBackButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onShareButtonClicked: () -> Unit
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

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
            IconButton(onClick = {
                onBackButtonClicked()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = DetailsTextColor,
                    contentDescription = "Back Button"
                )
            }
        },
        actions = {

            IconButton(onClick = {
                onShareButtonClicked()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    tint = DetailsTextColor,
                    contentDescription = "Share Transaction Button"
                )
            }

            IconButton(onClick = {
                openDialog = true
            }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = DetailsTextColor,
                    contentDescription = "Delete Transaction Button"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceBackgroundColor
        )
    )

    if (openDialog) {
        ShowDeleteTransactionDialog(
            title = "Delete Transaction?",
            content = "Are you sure you want to delete this transaction?",
            onDismissClicked = { openDialog = false },
            onConfirmClicked = {
                openDialog = false
                onDeleteButtonClicked()
            })
    }
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
fun EditFAB(
    modifier: Modifier = Modifier,
    onEditFABClicked: () -> Unit
) {
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
        onClick = {
//            ExpenseManagerRouter.navigateTo(destination = Screen.EditTransactionScreen)
            onEditFABClicked()
        },
        containerColor = FABColor
    )
}

@Composable
fun ShowDeleteTransactionDialog(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onDismissClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismissClicked()
        },
        confirmButton = {
            TextButton(onClick = { onConfirmClicked() }) {
                Text(
                    text = "Delete",
                    style = TextStyle(
                        color = BottomNavigationBarItemSelectedColor
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissClicked() }) {
                Text(
                    text = "Cancel", style = TextStyle(
                        color = BottomNavigationBarItemSelectedColor
                    )
                )
            }
        },
        containerColor = ComponentsBackgroundColor,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete icon on alert dialog",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    color = HeadingTextColor
                )
            )
        },
        text = {
            Text(
                text = content,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    color = HeadingTextColor
                )
            )
        }
    )
}


@Preview
@Composable
fun TransactionDetailsPreview() {
    DetailsTopAppBar(onBackButtonClicked = {}, onDeleteButtonClicked = {}) {

    }
    /*TransactionDetailsScreen(
        jsonTransactionResponse = TransactionResponse(
            "",
            "",
            "T-Mobile Phone Bill",
            15.94,
            "EXPENSE",
            "Utilities",
            "Jul 01, 2023",
            "Monthly Phone Recharge"
        ).toString(),
        onNavigateUpClicked = {

        },
        onEditFABClicked = {

        },
        onShareButtonClicked = {

        },
        onDeleteTransactionButtonClicked = {

        }
    )*/
}
