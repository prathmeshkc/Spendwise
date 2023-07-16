package com.pcandroiddev.expensemanager.ui.screens.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.local.SearchFilters
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.ui.components.TransactionListItem
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.SelectedChipContainerColor
import com.pcandroiddev.expensemanager.ui.theme.SelectedChipTextColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.UnSelectedChipContainerColor
import com.pcandroiddev.expensemanager.ui.theme.UnSelectedChipTextColor
import com.pcandroiddev.expensemanager.ui.uievents.SearchTransactionUIEvent
import com.pcandroiddev.expensemanager.utils.NetworkResult
import com.pcandroiddev.expensemanager.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarContentScreen(
    dashboardViewModel: DashboardViewModel,
    onSearchedTransactionListItemClicked: (TransactionResponse) -> Unit
) {

    val context = LocalContext.current

    val searchedTransactionResponseList by
    dashboardViewModel.searchedTransactionList.collectAsState()

    var isLoading by remember {
        mutableStateOf(false)
    }

    var searchedTransactionList by remember {
        mutableStateOf(emptyList<TransactionResponse>())
    }

    when (searchedTransactionResponseList) {
        is NetworkResult.Loading -> {
            isLoading = true
        }

        is NetworkResult.Error -> {
            isLoading = false
            Toast.makeText(
                context,
                searchedTransactionResponseList.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }

        is NetworkResult.Success -> {
            isLoading = false
            searchedTransactionList = searchedTransactionResponseList.data!!
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBackgroundColor)
    ) {

        var selectedFilter by remember {
            mutableStateOf(SearchFilters.All.name)
        }

        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedFilter == SearchFilters.All.name,
                onClick = {
                    selectedFilter = SearchFilters.All.name
                    dashboardViewModel.onEventChange(
                        event = SearchTransactionUIEvent.SearchFilterChanged(
                            searchFilter = selectedFilter
                        )
                    )
                },
                label = {
                    Text(
                        text = "All",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 15.sp,
                        color = if (selectedFilter == SearchFilters.All.name) SelectedChipTextColor else UnSelectedChipTextColor
                    )
                },
                leadingIcon = {
                    if (selectedFilter == SearchFilters.All.name) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            tint = Color.White,
                            contentDescription = "Selected Filter is All"
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = UnSelectedChipContainerColor,
                    selectedContainerColor = SelectedChipContainerColor
                )
            )

            FilterChip(
                selected = selectedFilter == SearchFilters.Income.name,
                onClick = {
                    selectedFilter = SearchFilters.Income.name
                    dashboardViewModel.onEventChange(
                        event = SearchTransactionUIEvent.SearchFilterChanged(
                            searchFilter = selectedFilter
                        )
                    )
                },
                label = {
                    Text(
                        text = "Income",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 15.sp,
                        color = if (selectedFilter == SearchFilters.Income.name) SelectedChipTextColor else UnSelectedChipTextColor
                    )
                },
                leadingIcon = {
                    if (selectedFilter == SearchFilters.Income.name) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            tint = Color.White,
                            contentDescription = "Selected Filter is Income"
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = UnSelectedChipContainerColor,
                    selectedContainerColor = SelectedChipContainerColor
                )
            )

            FilterChip(
                selected = selectedFilter == SearchFilters.Expense.name,
                onClick = {
                    selectedFilter = SearchFilters.Expense.name
                    dashboardViewModel.onEventChange(
                        event = SearchTransactionUIEvent.SearchFilterChanged(
                            searchFilter = selectedFilter
                        )
                    )
                },
                label = {
                    Text(
                        text = "Expense",
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 15.sp,
                        color = if (selectedFilter == SearchFilters.Expense.name) SelectedChipTextColor else UnSelectedChipTextColor
                    )
                },
                leadingIcon = {
                    if (selectedFilter == SearchFilters.Expense.name) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            tint = Color.White,
                            contentDescription = "Selected Filter is Expense"
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = UnSelectedChipContainerColor,
                    selectedContainerColor = SelectedChipContainerColor
                )
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {

            if (searchedTransactionList.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "No search results",
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = DetailsTextColor
                )

            } else {
                SearchedTransactionList(
                    transactionList = searchedTransactionList,
                    onTransactionListItemClicked = { transactionResponse ->
                        onSearchedTransactionListItemClicked(transactionResponse)
                    }
                )
            }


        }


    }

    DisposableEffect(key1 = dashboardViewModel.searchTransactionUIState.value.searchText) {
        onDispose {
            if (dashboardViewModel.searchTransactionUIState.value.searchText.isEmpty()) {
                dashboardViewModel.resetSearchState()
            }
        }
    }
}


@Composable
fun SearchedTransactionList(
    transactionList: List<TransactionResponse>,
    onTransactionListItemClicked: (TransactionResponse) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(transactionList) {
            TransactionListItem(
                transactionResponse = it,
                onTransactionListItemClicked = {
                    onTransactionListItemClicked(it)
                }
            )
        }
    }


}