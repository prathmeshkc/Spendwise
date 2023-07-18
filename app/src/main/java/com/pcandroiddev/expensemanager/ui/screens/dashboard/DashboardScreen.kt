package com.pcandroiddev.expensemanager.ui.screens.dashboard

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.local.TransactionType
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.ui.components.DashboardExpensesSearchBar
import com.pcandroiddev.expensemanager.ui.components.TotalBalanceCard
import com.pcandroiddev.expensemanager.ui.components.TotalExpenseCard
import com.pcandroiddev.expensemanager.ui.components.TotalIncomeCard
import com.pcandroiddev.expensemanager.ui.components.TransactionListItem
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.uievents.SearchTransactionUIEvent
import com.pcandroiddev.expensemanager.utils.Helper
import com.pcandroiddev.expensemanager.utils.ApiResult
import com.pcandroiddev.expensemanager.utils.isScrollingUp
import com.pcandroiddev.expensemanager.viewmodels.DashboardViewModel

private const val TAG = "DashboardScreen"

//TODO: Observe for transaction list changes to update the list
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onAddTransactionFABClicked: () -> Unit,
    onTransactionListItemClicked: (TransactionResponse) -> Unit,
    onSearchedTransactionListItemClicked: (TransactionResponse) -> Unit,
    onLogOutButtonClicked: () -> Unit,
    onBackPressedCallback: () -> Unit
) {

    val context = LocalContext.current

    val listState = rememberLazyListState()

    var isSearchBarActive by remember {
        mutableStateOf(false)
    }

    var totalBalanceText by remember {
        mutableStateOf(0.00)
    }

    var totalIncomeText by remember {
        mutableStateOf(0.00)
    }

    var totalExpenseText by remember {
        mutableStateOf(0.00)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val swipeToRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)


    var transactionList by remember {
        mutableStateOf(emptyList<TransactionResponse>())
    }

    val transactionResponseList by dashboardViewModel.transactionList.collectAsState()



    when (transactionResponseList) {
        is ApiResult.Loading -> {
            isLoading = true
        }

        is ApiResult.Error -> {
            isLoading = false
            Toast.makeText(context, transactionResponseList.message.toString(), Toast.LENGTH_LONG)
                .show()
        }

        is ApiResult.Success -> {
            isLoading = false
            transactionList = transactionResponseList.data!!
            val (totalIncomeList, totalExpenseList) = transactionList.partition { transactionResponse ->
                transactionResponse.transactionType == TransactionType.INCOME.name
            }
            totalIncomeText = totalIncomeList.sumOf { it.amount }
            totalExpenseText = totalExpenseList.sumOf { it.amount }
            totalBalanceText = totalIncomeText - totalExpenseText
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBackgroundColor),

        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            DashboardExpensesSearchBar(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                onLogoutOptionClicked = {
                    dashboardViewModel.logout()
                    onLogOutButtonClicked()
                },
                isActive = { isActive ->
                    isSearchBarActive = isActive
                    if (!isSearchBarActive) {
                        dashboardViewModel.resetSearchState()
                    }
                },
                onSearchTextChanged = {
                    dashboardViewModel.onEventChange(
                        event = SearchTransactionUIEvent.SearchTextChanged(
                            searchText = it
                        )
                    )
                },
                onSearchButtonClicked = {
                    dashboardViewModel.onEventChange(
                        event = SearchTransactionUIEvent.SearchTransactionButtonClicked
                    )
                }
            ) {
                SearchBarContentScreen(
                    dashboardViewModel = dashboardViewModel,
                    onSearchedTransactionListItemClicked = { transactionResponse ->
                        onSearchedTransactionListItemClicked(transactionResponse)
                    }
                )
            }

            TotalBalanceCard(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                labelText = "TOTAL BALANCE",
                amountText = Helper.stringifyTotalBalance(balance = totalBalanceText)
            )

            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TotalIncomeCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(124.dp),
                    amountText = totalIncomeText.toString()
                )
                TotalExpenseCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(124.dp),
                    amountText = totalExpenseText.toString()
                )
            }

            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Transactions",
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = DetailsTextColor
                )

                //TODO: Navigate to All Transaction Screen

                /* Icon(
                     modifier = Modifier
                         .clickable {
                             //TODO: Navigate to All Transaction Screen
                         },
                     imageVector = Icons.Outlined.ArrowForward,
                     contentDescription = "View All Transactions",
                     tint = FABColor
                 )*/
            }

            //TODO: Send list to this composable only when Success

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = FABColor
                )
            } else {

                Box(modifier = Modifier.fillMaxSize()) {

                    SwipeRefresh(
                        modifier = Modifier.fillMaxSize(),
                        state = swipeToRefreshState,
                        onRefresh = { dashboardViewModel.getAllTransaction() }) {
                        if (transactionList.isEmpty()) {

                            val composition by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.cards_animation
                                )
                            )
                            LottieAnimation(
                                modifier = Modifier.align(Alignment.Center),
                                composition = composition,
                                isPlaying = true,
                                iterations = LottieConstants.IterateForever
                            )

                        } else {
                            TransactionList(
                                listState = listState,
                                transactionList = transactionList,
                                onTransactionListItemClicked = {
                                    onTransactionListItemClicked(it)
                                })
                        }
                    }
                    /*if (transactionList.isEmpty()) {

                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                R.raw.cards_animation
                            )
                        )
                        LottieAnimation(
                            modifier = Modifier.align(Alignment.Center),
                            composition = composition,
                            isPlaying = true,
                            iterations = LottieConstants.IterateForever
                        )

                    } else {
                        TransactionList(
                            listState = listState,
                            transactionList = transactionList,
                            onTransactionListItemClicked = {
                                onTransactionListItemClicked(it)
                            })
                    }*/
                }
            }
        }

        if (!isSearchBarActive) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 40.dp, end = 30.dp)
                    .align(alignment = Alignment.BottomEnd),
                containerColor = FABColor,
                text = {
                    Text(
                        text = "Add Transaction",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.inter_regular)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = DetailsTextColor
                        )
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
                    onAddTransactionFABClicked()
                },
                expanded = listState.isScrollingUp()

            )
        }
    }



    BackHandler {
        onBackPressedCallback()
    }
}


@Composable
fun TransactionList(
    listState: LazyListState,
    transactionList: List<TransactionResponse>,
    onTransactionListItemClicked: (TransactionResponse) -> Unit
) {

    LazyColumn(
        state = listState,
        content = {
            items(transactionList) {
                TransactionListItem(
                    transactionResponse = it,
                    onTransactionListItemClicked = {
                        onTransactionListItemClicked(it)
                    }
                )
            }
        })
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(
        onAddTransactionFABClicked = {

        },
        onTransactionListItemClicked = {

        },
        onSearchedTransactionListItemClicked = {

        },
        onLogOutButtonClicked = {

        },
        onBackPressedCallback = {

        }
    )
}