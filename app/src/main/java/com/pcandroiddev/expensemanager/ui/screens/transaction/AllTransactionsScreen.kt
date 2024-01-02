package com.pcandroiddev.expensemanager.ui.screens.transaction

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pcandroiddev.expensemanager.R
import com.pcandroiddev.expensemanager.data.local.filter.StatementOptions
import com.pcandroiddev.expensemanager.data.local.transaction.TransactionType
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.ui.components.TransactionListItem
import com.pcandroiddev.expensemanager.ui.components.TransactionListItemShimmerEffect
import com.pcandroiddev.expensemanager.ui.theme.BottomNavigationBarItemSelectedColor
import com.pcandroiddev.expensemanager.ui.theme.CategoryColors
import com.pcandroiddev.expensemanager.ui.theme.ComponentsBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.DetailsTextColor
import com.pcandroiddev.expensemanager.ui.theme.DisabledButtonColor
import com.pcandroiddev.expensemanager.ui.theme.FABColor
import com.pcandroiddev.expensemanager.ui.theme.GreenIncomeColor
import com.pcandroiddev.expensemanager.ui.theme.HeadingTextColor
import com.pcandroiddev.expensemanager.ui.theme.LinkColor
import com.pcandroiddev.expensemanager.ui.theme.PurpleGrey40
import com.pcandroiddev.expensemanager.ui.theme.RedExpenseColor
import com.pcandroiddev.expensemanager.ui.theme.SelectedChipContainerColor
import com.pcandroiddev.expensemanager.ui.theme.SurfaceBackgroundColor
import com.pcandroiddev.expensemanager.ui.theme.UnSelectedChipTextColor
import com.pcandroiddev.expensemanager.ui.uievents.AllTransactionTopBarUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import com.pcandroiddev.expensemanager.utils.Helper
import com.pcandroiddev.expensemanager.viewmodels.AllTransactionsViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


private const val TAG = "AllTransactionsScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsScreen(
    allTransactionsViewModel: AllTransactionsViewModel = hiltViewModel(),
    symbol: String,
    snackbarHostState: SnackbarHostState,
    onTransactionListItemClicked: (TransactionResponse) -> Unit,
    onStatementGenerated: (File) -> Unit,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val statementGenerationErrorState =
        allTransactionsViewModel.statementGenerationErrorState.collectAsState()

    val statementGenerationSuccessState =
        allTransactionsViewModel.statementGenerationSuccessState.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var transactionList by remember {
        mutableStateOf(emptyList<TransactionResponse>())
    }

    val transactionResponseList by allTransactionsViewModel.transactionList.collectAsState()

    var incomeText by remember {
        mutableStateOf(0.00)
    }

    var expenseText by remember {
        mutableStateOf(0.00)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val swipeToRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    var isCalendarOpen by remember {
        mutableStateOf(false)
    }

    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    var currentlyShowingDates by remember {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        val currentMonth = LocalDate.now().format(formatter)
        mutableStateOf(currentMonth)
    }

//    val categoryPercentages by allTransactionsViewModel.categoryPercentages
    var categoryPercentages by remember {
        mutableStateOf(emptyMap<String, Double>())
    }

    when (transactionResponseList) {
        is ApiResult.Loading -> {
            isLoading = true
        }

        is ApiResult.Error -> {
            isLoading = false
//            TODO: Show snackbar instead of Toast message also replace toast with snackbar at other places
            Toast.makeText(context, transactionResponseList.message.toString(), Toast.LENGTH_LONG)
                .show()
        }

        is ApiResult.Success -> {
            isLoading = false
            transactionList = transactionResponseList.data!!
            val (totalIncomeList, totalExpenseList) = transactionList.partition { transactionResponse ->
                transactionResponse.transactionType == TransactionType.INCOME.name
            }
            incomeText = totalIncomeList.sumOf { it.amount }
            expenseText = totalExpenseList.sumOf { it.amount }

            val expenseTransactions = transactionList.filter { it.transactionType == "EXPENSE" }

            // Step 2: Map the Categories to their Count
            val categoryCounts = expenseTransactions.groupingBy { it.category }.eachCount()

            // Step 3: Calculate the Percentage of Each Category
            val totalExpenseTransactions = expenseTransactions.size
            categoryPercentages =
                categoryCounts.mapValues { (_, count) -> (count.toDouble() / totalExpenseTransactions) * 100 }


//            allTransactionsViewModel.getCategoryPercentages(transactionList = transactionList)
        }

        else -> {
            Log.d(TAG, "AllTransactionsScreen: Else branch")
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            if (!isCalendarOpen) {
                AllTransactionsTopAppBar(
                    onCalendarButtonClicked = {
                        isCalendarOpen = true
                    },
                    onStatementOptionSelected = { selectionStatementOption ->
                        //TODO
                        Log.d(TAG, "AllTransactionsScreen: $selectionStatementOption")
                        if (allTransactionsViewModel.isOfMonthYearFormat(currentlyShowingDates)) {

                            val dateRange = allTransactionsViewModel.getCurrentMonthDates()
                            allTransactionsViewModel.onTopBarEventChange(
                                event = AllTransactionTopBarUIEvent.DownloadStatement(
                                    transactionList = transactionList,
                                    startDate = dateRange.first,
                                    endDate = dateRange.second,
                                    income = incomeText.toString(),
                                    expense = expenseText.toString(),
                                    fileType = selectionStatementOption
                                )
                            )
                        } else {

                            val dateRangeList = currentlyShowingDates.split(" - ")
                            val dateRange: Pair<String, String> =
                                dateRangeList[0] to dateRangeList[1]
                            allTransactionsViewModel.onTopBarEventChange(
                                event = AllTransactionTopBarUIEvent.DownloadStatement(
                                    transactionList = transactionList,
                                    startDate = dateRange.first,
                                    endDate = dateRange.second,
                                    income = incomeText.toString(),
                                    expense = expenseText.toString(),
                                    fileType = selectionStatementOption
                                )
                            )

                        }

                    })
            }
        }, containerColor = SurfaceBackgroundColor

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(SurfaceBackgroundColor)
        ) {

            if (isCalendarOpen) {
                ExpenseDateRangePicker(
                    onConfirmButtonClicked = { startDate: Long, endDate: Long ->
                        Log.d("AllTransactionsScreen", "startDate-$startDate endDate-$endDate")
                        isCalendarOpen = false

                        /*snackScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "$startDate to $endDate",
                                withDismissAction = true
                            )
                        }*/

                        allTransactionsViewModel.onTopBarEventChange(
                            event = AllTransactionTopBarUIEvent.DateRangeChanged(
                                startDate = startDate,
                                endDate = endDate
                            )
                        )
                        currentlyShowingDates =
                            "${allTransactionsViewModel.getLocalDateFromLong(startDate)} - ${
                                allTransactionsViewModel.getLocalDateFromLong(endDate)
                            }"
                    },
                    onCloseCalendarButtonClicked = {
                        isCalendarOpen = false
                    })
            }


            if (transactionList.isNotEmpty()) {

                IncomeExpenseText(
                    symbol = symbol,
                    incomeText = incomeText,
                    expenseText = expenseText
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentlyShowingDates,
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = DetailsTextColor
                    )

                    IconButton(onClick = { isBottomSheetOpen = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Insights,
                            tint = FABColor,
                            contentDescription = "Show Pie Chart"
                        )
                    }
                }

                if (isBottomSheetOpen) {

                    ModalBottomSheet(
                        containerColor = ComponentsBackgroundColor,
                        tonalElevation = 8.dp,
                        onDismissRequest = { isBottomSheetOpen = false },
                        dragHandle = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                BottomSheetDefaults.DragHandle(color = HeadingTextColor)

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Insights",
                                            style = TextStyle(
                                                color = DetailsTextColor,
                                                fontFamily = FontFamily(Font(R.font.inter_bold)),
                                                fontSize = 22.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Outlined.AutoAwesome,
                                            tint = UnSelectedChipTextColor,
                                            contentDescription = null
                                        )
                                    }

                                    IconButton(onClick = {
                                        scope.launch { bottomSheetState.hide() }
                                            .invokeOnCompletion {
                                                if (!bottomSheetState.isVisible) isBottomSheetOpen =
                                                    false
                                            }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            tint = FABColor,
                                            contentDescription = "Close Charts Bottom Sheet"
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Divider(color = SelectedChipContainerColor)
                            }
                        }
                    ) {

                        val listOfSlice = mutableListOf<PieChartData.Slice>()
                        categoryPercentages.forEach { (category, percentage) ->
                            val formattedPercentage = String.format("%.2f%%", percentage)
                            println("$category: $formattedPercentage")
                            listOfSlice.add(
                                PieChartData.Slice(
                                    label = category,
                                    value = Helper.formatDoubleWithTwoDecimals(percentage)
                                        .toFloat(),
                                    color = CategoryColors[category] ?: Color.White
                                )
                            )
                        }

                        Log.d(TAG, "DonutChart/listOfSlice: $listOfSlice")

                        val pieChartData = PieChartData(
                            slices = listOfSlice, plotType = PlotType.Pie
                        )

                        val pieChartConfig = PieChartConfig(
                            showSliceLabels = true,
                            sliceLabelTextSize = 14.sp,
                            sliceLabelTextColor = DetailsTextColor,
                            sliceLabelTypeface = Typeface.defaultFromStyle(Typeface.ITALIC),
                            labelVisible = true,
                            strokeWidth = 100f,
                            labelColor = DetailsTextColor,
                            activeSliceAlpha = .5f,
                            inActiveSliceAlpha = 1f,
                            isEllipsizeEnabled = true,
                            sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE,
                            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
                            backgroundColor = SurfaceBackgroundColor,
                            chartPadding = 30,
                            isAnimationEnable = true,
                            animationDuration = 500,
                            isClickOnSliceEnabled = false,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Legends(
                            legendsConfig = getLegendsConfigFromPieChartData(
                                pieChartData = pieChartData,
                                gridSize = 2
                            )
                        )

                        PieChart(
                            modifier = Modifier
                                .background(color = ComponentsBackgroundColor),
                            pieChartData = pieChartData,
                            pieChartConfig = pieChartConfig,
                            onSliceClick = {
                                Log.d(TAG, "PieChart/OnClick: $it")
                            }
                        )

                        /*DonutChart(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            categoryData = categoryPercentages
                        )*/
                    }
                }

                if (isLoading) {
                    LazyColumn(content = {
                        items(count = 7) {
                            TransactionListItemShimmerEffect()
                        }
                    })

                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SwipeRefresh(modifier = Modifier
                            .fillMaxSize(),
                            state = swipeToRefreshState,
                            onRefresh = {
//                            TODO: Refresh on the currently selected start and end dates
                                val dateRange = allTransactionsViewModel.getCurrentMonthDates()
                                allTransactionsViewModel.getAllTransactionBetweenDates(
                                    startDate = dateRange.first,
                                    endDate = dateRange.second
                                )

                                val formatter =
                                    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
                                val currentMonth = LocalDate.now().format(formatter)

                                currentlyShowingDates = currentMonth

                            }) {
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
                                LazyColumn(content = {
                                    items(transactionList) {
                                        TransactionListItem(symbol = symbol,
                                            transactionResponse = it,
                                            onTransactionListItemClicked = {
                                                onTransactionListItemClicked(it)
                                            })
                                    }
                                })
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(

                        text = "Start adding transactions or select different dates to see the chart...",
                        style = TextStyle(
                            color = PurpleGrey40,
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.inter_light)),
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

        }




        LaunchedEffect(key1 = statementGenerationErrorState.value) {
            statementGenerationErrorState.value?.let { message ->
//                showToast(context, message)
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Long
                    )
                }
                allTransactionsViewModel.clearStatementGenerationState()
            }
        }

        LaunchedEffect(key1 = statementGenerationSuccessState.value) {
            statementGenerationSuccessState.value?.let { file ->
                onStatementGenerated(file)
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Statement generated in Downloads folder",
                        duration = SnackbarDuration.Long
                    )
                }
//                showToast(context, "Statement generated in Downloads folder")
            }
        }
    }


    BackHandler {
        onBackPressed()
    }
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsTopAppBar(
    onCalendarButtonClicked: () -> Unit, onStatementOptionSelected: (String) -> Unit
) {

    var openDialog by remember {
        mutableStateOf(false)
    }

    TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
        Text(
            text = "Transactions",
            style = TextStyle(
                color = DetailsTextColor, fontWeight = FontWeight.SemiBold, fontSize = 20.sp
            ),
        )
    }, actions = {

        IconButton(onClick = { onCalendarButtonClicked() }) {
            Icon(
                imageVector = Icons.Outlined.Today,
                contentDescription = "Date Picker to select dates",
                tint = DetailsTextColor
            )
        }

        IconButton(
            onClick = {
                openDialog = true
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.FileDownload,
                tint = DetailsTextColor,
                contentDescription = "Download CSV or PDF file for currently filtered transactions"
            )
        }
    },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceBackgroundColor
        )
    )

    val statementOptionsList = listOf(
        StatementOptions.PDF.name,
//        StatementOptions.EXCEL.name
    )

    var selectedStatementOption by remember {
        mutableStateOf(statementOptionsList[0])
    }

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
                        text = "Download Statement For Selected Dates",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                            textAlign = TextAlign.Center,
                            color = DetailsTextColor
                        )
                    )

                    statementOptionsList.forEach { statementOption ->
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(36.dp)
                                .selectable(
                                    selected = selectedStatementOption == statementOption,
                                    onClick = {
                                        selectedStatementOption = statementOption
                                        onStatementOptionSelected(selectedStatementOption)
                                        openDialog = false
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedStatementOption == statementOption,
                                onClick = {
                                    selectedStatementOption = statementOption
                                    onStatementOptionSelected(selectedStatementOption)
                                    openDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = BottomNavigationBarItemSelectedColor,

                                    )
                            )

                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = statementOption,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDateRangePicker(
    onConfirmButtonClicked: (startDate: Long, endDate: Long) -> Unit,
    onCloseCalendarButtonClicked: () -> Unit
) {

    val dateRangePickerState = rememberDateRangePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ComponentsBackgroundColor),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(onClick = {
                onCloseCalendarButtonClicked()
            }) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close Date Range Picker",
                    tint = HeadingTextColor
                )
            }

            TextButton(
                onClick = {
                    onConfirmButtonClicked(
                        dateRangePickerState.selectedStartDateMillis!!,
                        dateRangePickerState.selectedEndDateMillis!!
                    )
                },
                enabled = dateRangePickerState.selectedEndDateMillis != null,
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
        }

        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .weight(1f),
            title = {
                Text(
                    modifier = Modifier
                        .padding(start = 64.dp, end = 12.dp),
                    text = "Select Start and End Dates",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.inter_semi_bold)),
                    )
                )
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                titleContentColor = DetailsTextColor,
                headlineContentColor = DetailsTextColor,
                weekdayContentColor = DetailsTextColor,
                subheadContentColor = DetailsTextColor,
                yearContentColor = DetailsTextColor,
                currentYearContentColor = FABColor,
                selectedYearContentColor = FABColor,
                selectedYearContainerColor = DetailsTextColor,
                dayContentColor = DetailsTextColor,
                selectedDayContentColor = DetailsTextColor,
                selectedDayContainerColor = LinkColor,
                todayContentColor = BottomNavigationBarItemSelectedColor,
                todayDateBorderColor = BottomNavigationBarItemSelectedColor,
                dayInSelectionRangeContentColor = SelectedChipContainerColor,
                dayInSelectionRangeContainerColor = BottomNavigationBarItemSelectedColor,
            )
        )

    }
}

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    categoryData: Map<String, Double>
) {

    val listOfSlice = mutableListOf<PieChartData.Slice>()
    categoryData.forEach { (category, percentage) ->
        val formattedPercentage = String.format("%.2f%%", percentage)
        println("$category: $formattedPercentage")
        listOfSlice.add(
            PieChartData.Slice(
                label = category,
                value = Helper.formatDoubleWithTwoDecimals(percentage).toFloat(),
                color = CategoryColors[category] ?: Color.White
            )
        )
    }

    Log.d(TAG, "DonutChart/listOfSlice: $listOfSlice")
    /*val donutChartData = PieChartData(
        slices = listOfSlice, plotType = PlotType.Donut
    )*/

    val pieChartData = PieChartData(
        slices = listOfSlice, plotType = PlotType.Pie
    )

    /*val donutChartConfig = PieChartConfig(
        labelVisible = true,
        strokeWidth = 100f,
        labelColor = DetailsTextColor,
        activeSliceAlpha = .9f,
        isEllipsizeEnabled = true,
        labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
        backgroundColor = SurfaceBackgroundColor,
        chartPadding = 30,
        isAnimationEnable = true,
        animationDuration = 700,
        isClickOnSliceEnabled = true,
    )*/


    val pieChartConfig = PieChartConfig(
        showSliceLabels = true,
        sliceLabelTextSize = 14.sp,
        sliceLabelTextColor = DetailsTextColor,
        sliceLabelTypeface = Typeface.defaultFromStyle(Typeface.ITALIC),
        labelVisible = true,
        strokeWidth = 100f,
        labelColor = DetailsTextColor,
        activeSliceAlpha = .5f,
        inActiveSliceAlpha = 1f,
        isEllipsizeEnabled = true,
        sliceLabelEllipsizeAt = TextUtils.TruncateAt.MIDDLE,
        labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
        backgroundColor = SurfaceBackgroundColor,
        chartPadding = 30,
        isAnimationEnable = true,
        animationDuration = 500,
        isClickOnSliceEnabled = false,
    )

    PieChart(
        modifier = modifier
            .height(370.dp)
            .fillMaxSize(),
        pieChartData = pieChartData,
        pieChartConfig = pieChartConfig,
        onSliceClick = {
            Log.d(TAG, "PieChart/OnClick: $it")
        }

    )

    /*Column(
        modifier = modifier
            .height(370.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        *//*Legends(
            legendsConfig = getLegendsConfigFromPieChartData(
                pieChartData = donutChartData,
                gridSize = 2
            )
        )*//*

        *//*DonutPieChart(
            modifier = modifier,
            pieChartData = donutChartData,
            pieChartConfig = donutChartConfig,
            onSliceClick = {
                Log.d(TAG, "DonutChart/OnClick: $it")
            }

        )*//*

        PieChart(
            modifier = modifier,
            pieChartData = pieChartData,
            pieChartConfig = pieChartConfig,
            onSliceClick = {
                Log.d(TAG, "PieChart/OnClick: $it")
            }

        )
    }*/

}

/*@Composable
fun IncomeExpenseText(
    modifier: Modifier = Modifier,
    symbol: String = "$",
    incomeText: Double,
    expenseText: Double
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier
                .weight(1f)
                .background(Color(0, 180, 102)),
            text = symbol.plus(Helper.formatAmountWithLocale(incomeText)),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold))
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = modifier
                .weight(1f)
                .background(Color(191, 63, 36)),
            text = symbol.plus(Helper.formatAmountWithLocale(expenseText)),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold))

            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}*/

@Composable
fun IncomeExpenseText(
    modifier: Modifier = Modifier,
    symbol: String = "$",
    incomeText: Double,
    expenseText: Double
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val incomeAnnotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = GreenIncomeColor,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold))
                )
            ) {
                append(symbol.plus(Helper.formatAmountWithLocale(incomeText)))
            }

            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            ) {
                pushStringAnnotation(tag = "Income", annotation = "Income")
                append(" Income")
            }
        }

        val expenseAnnotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = RedExpenseColor,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.inter_semi_bold))
                )
            ) {
                append(symbol.plus(Helper.formatAmountWithLocale(expenseText)))
            }

            withStyle(
                style = SpanStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_regular))
                )
            ) {
                pushStringAnnotation(tag = "Expense", annotation = "Expense")
                append(" Expense")
            }
        }

        Log.d(TAG, "IncomeExpenseText/incomeAnnotatedString: $incomeAnnotatedString")
        Log.d(TAG, "IncomeExpenseText/expenseAnnotatedString: $incomeAnnotatedString")

        Text(
            text = incomeAnnotatedString,
            overflow = TextOverflow.Ellipsis
        )

        Divider(
            modifier = Modifier
                .height(20.dp)
                .width(1.dp),
            color = DetailsTextColor
        )

        Text(
            text = expenseAnnotatedString,
            overflow = TextOverflow.Ellipsis
        )


        /*Text(
            modifier = modifier
                .weight(1f)
                .background(Color(0, 180, 102)),
            text = symbol.plus(Helper.formatAmountWithLocale(incomeText)),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold))
            ),


        )*/

        /*Text(
            modifier = modifier
                .weight(1f)
                .background(Color(191, 63, 36)),
            text = symbol.plus(Helper.formatAmountWithLocale(expenseText)),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_semi_bold))

            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )*/
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AllTransactionsScreenPreview() {
    /*IncomeExpenseText(
        incomeText = 1749.0,
        expenseText = 1420.0
    )*/

    IncomeExpenseText(incomeText = 100043.59, expenseText = 60549.35)
}

/**
 * Returns the legends config for given pie chart data
 * @param pieChartData:  Pie chart details.
 * @param gridSize: Legends grid size.
 */
private fun getLegendsConfigFromPieChartData(
    pieChartData: PieChartData,
    gridSize: Int
): LegendsConfig {

    val legendsList = mutableListOf<LegendLabel>()
    pieChartData.slices.forEach { slice ->
        legendsList.add(LegendLabel(slice.color, slice.label))
    }

    Log.d(TAG, "getLegendsConfigFromPieChartData/legendsList: $legendsList")
    Log.d(TAG, "getLegendsConfigFromPieChartData/pieChartData.slices: ${pieChartData.slices}")

    return LegendsConfig(
        legendLabelList = legendsList,
        gridColumnCount = gridSize,
        legendsArrangement = Arrangement.Start,
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            color = DetailsTextColor
        )
    )
}
