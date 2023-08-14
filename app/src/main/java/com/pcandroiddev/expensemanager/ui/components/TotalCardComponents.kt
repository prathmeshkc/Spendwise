package com.pcandroiddev.expensemanager.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.local.filter.TransactionFilters
import com.pcandroiddev.expensemanager.data.local.transaction.TransactionType
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemSelectedColor
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.GreenIncomeColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.RedExpenseColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.TotalBalanceColor
import com.pcandroiddev.expensemanager.utils.Helper

//TODO: Make a single Composable for Income/Expense Card


@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    symbol: String = "$",
    labelText: String,
    amountText: Double,
    currentTimeFrameText: String,
    onTransactionFilterClicked: (String) -> Unit,
    onPreviousTransactionsButtonClicked: () -> Unit,
    onNextTransactionsButtonClicked: () -> Unit,

    ) {
    Log.d("DashboardScreen", "TotalBalanceCard: $symbol")
    Card(
        modifier = modifier
            .height(130.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {


        var openDialog by remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*IconButton(onClick = { onPreviousTransactionsButtonClicked() }) {
                Icon(
                    imageVector = Icons.Outlined.ChevronLeft,
                    contentDescription = "View Previous Transactions",
                    tint = FABColor
                )
            }*/

            OutlinedButton(
                modifier = Modifier.padding(start = 12.dp),
                onClick = { openDialog = true },
                border = BorderStroke(
                    width = 1.dp,
                    color = FABColor
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SurfaceBackgroundColor
                )
            ) {
                Text(
                    //TODO: Change this text according to the currently selected filter
                    text = currentTimeFrameText,
                    style = TextStyle(
                        color = DetailsTextColor,
                        fontFamily = FontFamily(Font(R.font.inter_medium))
                    )
                )
            }

            /*IconButton(onClick = { onNextTransactionsButtonClicked() }) {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "View Next Transactions",
                    tint = FABColor
                )
            }*/
        }


        val transactionFiltersList = listOf(
            TransactionFilters.Daily.name,
            TransactionFilters.Weekly.name,
            TransactionFilters.Monthly.name,
            TransactionFilters.Yearly.name
        )
        var selectedTransactionFilter by remember {
            mutableStateOf(transactionFiltersList[2])
        }

        /*IconButton(onClick = { openDialog = true }) {
            Icon(
                imageVector = Icons.Outlined.FilterAlt,
                contentDescription = "Filter Transactions",
                tint = HeadingTextColor
            )
        }*/

        if (openDialog) {
            Dialog(onDismissRequest = { openDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ComponentsBackgroundColor
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)

                    ) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "Show Transactions",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                                color = DetailsTextColor
                            )
                        )
                        transactionFiltersList.forEach { transactionFilter ->
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = selectedTransactionFilter == transactionFilter,
                                        onClick = {
                                            selectedTransactionFilter = transactionFilter
                                            onTransactionFilterClicked(selectedTransactionFilter)
                                            openDialog = false
                                        }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedTransactionFilter == transactionFilter,
                                    onClick = {
                                        selectedTransactionFilter = transactionFilter
                                        onTransactionFilterClicked(selectedTransactionFilter)
                                        openDialog = false
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = BottomNavigationBarItemSelectedColor,

                                        )
                                )
                                Text(
                                    modifier = Modifier.padding(start = 16.dp),
                                    text = transactionFilter,
                                    style = TextStyle(
                                        color = DetailsTextColor,
                                        fontFamily = FontFamily(Font(R.font.inter_semi_bold))
                                    )
                                )
                            }
                        }

                        TextButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = { openDialog = false }) {
                            Text(
                                text = "CANCEL",
                                style = TextStyle(
                                    color = BottomNavigationBarItemSelectedColor,
                                )
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = labelText,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = HeadingTextColor
            )


            Text(
                text = when {
                    amountText < 0 -> {
                        "- ".plus(symbol.plus(Helper.formatAmountWithLocale(amount = amountText * -1)))
                    }

                    else -> {
                        symbol.plus(Helper.formatAmountWithLocale(amount = amountText))
                    }
                },
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                color = TotalBalanceColor
            )
        }


    }

}


@Composable
fun DashboardFilterButton(

    onTransactionFilterClicked: (String) -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    val transactionFiltersList = listOf("Daily", "Weekly", "Monthly", "Yearly")
    var selectedTransactionFilter by remember {
        mutableStateOf(transactionFiltersList[2])
    }

    /*IconButton(onClick = { openDialog = true }) {
        Icon(
            imageVector = Icons.Outlined.FilterAlt,
            contentDescription = "Filter Transactions",
            tint = HeadingTextColor
        )
    }*/

    if (openDialog) {
        Dialog(onDismissRequest = { openDialog = false }) {
            Surface(
                color = ComponentsBackgroundColor
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "Show Transactions",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                            color = DetailsTextColor
                        )
                    )
                    transactionFiltersList.forEach { transactionFilter ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = selectedTransactionFilter == transactionFilter,
                                    onClick = {
                                        selectedTransactionFilter = transactionFilter
                                        onTransactionFilterClicked(selectedTransactionFilter)
                                        openDialog = false
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTransactionFilter == transactionFilter,
                                onClick = {
                                    selectedTransactionFilter = transactionFilter
                                    onTransactionFilterClicked(selectedTransactionFilter)
                                    openDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = BottomNavigationBarItemSelectedColor,

                                    )
                            )
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = transactionFilter,
                                style = TextStyle(
                                    color = DetailsTextColor,
                                    fontFamily = FontFamily(Font(R.font.inter_semi_bold))
                                )
                            )
                        }
                    }

                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = { openDialog = false }) {
                        Text(
                            text = "CANCEL",
                            style = TextStyle(
                                color = BottomNavigationBarItemSelectedColor,
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TotalIncomeCard(
    modifier: Modifier = Modifier,
    amountText: String,
    symbol: String = "$"
) {
    Log.d("DashboardScreen", "TotalIncomeCard: $symbol")

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        modifier = Modifier
                            .padding(start = 21.dp),
                        text = "INCOME",
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = HeadingTextColor
                    )

                    Image(
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp),
                        painter = painterResource(id = R.drawable.transaction_type_income),
                        contentDescription = "Total Income"
                    )


                }

                Text(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth(),
                    text = symbol.plus(amountText),
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = GreenIncomeColor
                )
            }
        }
    }
}

@Composable
fun TotalExpenseCard(
    modifier: Modifier = Modifier,
    amountText: String,
    symbol: String = "$"
) {
    Log.d("DashboardScreen", "TotalExpenseCard: $symbol")
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp, end = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        modifier = Modifier
                            .padding(start = 21.dp),
                        text = "EXPENSE",
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = HeadingTextColor
                    )

                    Image(
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp),
                        painter = painterResource(id = R.drawable.transaction_type_expense),
                        contentDescription = "Total Expense"
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth(),
                    text = symbol.plus(amountText),
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = RedExpenseColor
                )
            }
        }
    }
}


@Composable
fun TotalIncomeExpenseCard(
    modifier: Modifier = Modifier,
    type: TransactionType,
    amountText: String,
    symbol: String = "$",
) {

    Log.d("DashboardScreen", "TotalExpenseCard: $symbol")
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComponentsBackgroundColor),
        shape = RoundedCornerShape(2.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            Row(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    modifier = Modifier
                        .padding(start = 21.dp),
                    text = if (type == TransactionType.EXPENSE) "TOTAL EXPENSE" else "TOTAL INCOME",
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = HeadingTextColor
                )

                Image(
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp),
                    painter = painterResource(
                        id = when (type) {
                            TransactionType.EXPENSE -> {
                                R.drawable.transaction_type_expense
                            }

                            TransactionType.INCOME -> {
                                R.drawable.transaction_type_income
                            }
                        }
                    ),
                    contentDescription = "Total Expense"
                )
            }


            /*Text(
                modifier = Modifier
                    .padding(start = 21.dp)
                    .fillMaxWidth(),
                text = if (type == TransactionType.EXPENSE) "TOTAL EXPENSE" else "TOTAL INCOME",
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = HeadingTextColor
            )*/

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth(),
                text = when (type) {
                    TransactionType.EXPENSE -> {
                        "- $symbol".plus(amountText)
                    }

                    TransactionType.INCOME -> {
                        "+ $symbol".plus(amountText)
                    }
                },
                fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp,
                color = when (type) {
                    TransactionType.EXPENSE -> {
                        RedExpenseColor
                    }

                    TransactionType.INCOME -> {
                        GreenIncomeColor
                    }
                }
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun TotalCardsPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        TotalBalanceCard(
            labelText = "BALANCE",
            amountText = 1807.123456,
            currentTimeFrameText = TransactionFilters.Monthly.name,
            onTransactionFilterClicked = {

            },
            onPreviousTransactionsButtonClicked = {

            },
            onNextTransactionsButtonClicked = {

            }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TotalIncomeCard(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                amountText = "3574.00"
            )
            TotalExpenseCard(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                amountText = "1767.00"
            )
        }
        Text(text = "Single Card Component")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TotalIncomeExpenseCard(
                type = TransactionType.INCOME,
                modifier = Modifier
                    .weight(1f)
                    .height(104.dp),
                amountText = "3574.00",
            )


            TotalIncomeExpenseCard(
                type = TransactionType.EXPENSE,
                modifier = Modifier
                    .weight(1f)
                    .height(104.dp),
                amountText = "1767.00",
            )

        }


    }


}
