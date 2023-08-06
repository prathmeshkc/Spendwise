package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.ui.uievents.DateRangeUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class AllTransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val dateTimeFormatter: DateTimeFormatter
) : ViewModel() {


    private val _transactionList =
        MutableStateFlow<ApiResult<List<TransactionResponse>>>(ApiResult.Success(emptyList()))

    val transactionList: StateFlow<ApiResult<List<TransactionResponse>>> get() = _transactionList

    private val _categoryPercentages = mutableStateOf(emptyMap<String, Double>())
    val categoryPercentages: State<Map<String, Double>> get() = _categoryPercentages


    init {
        val todayDate = LocalDate.now()
        val firstDayOfMonth = todayDate.withDayOfMonth(1)
        val lastDayOfMonth = todayDate.withDayOfMonth(todayDate.lengthOfMonth())

        val formattedStartDate = dateTimeFormatter.format(firstDayOfMonth)
        val formattedEndDate = dateTimeFormatter.format(lastDayOfMonth)
        getAllTransactionBetweenDates(
            startDate = formattedStartDate,
            endDate = formattedEndDate
        )
    }

    fun onDateRangeEventChange(event: DateRangeUIEvent) {
        when (event) {
            is DateRangeUIEvent.DateRangeChanged -> {


                val startDate = getLocalDateFromLong(
                    dateInLong = event.startDate
                )
                val endDate = getLocalDateFromLong(
                    dateInLong = event.endDate
                )

                Log.d(TAG, "onDateRangeEventChange: $startDate - $endDate")

                getAllTransactionBetweenDates(startDate, endDate)
            }
        }
    }

    private fun getAllTransactionBetweenDates(startDate: String, endDate: String) {
        viewModelScope.launch {
            transactionRepository.getAllTransactionBetweenDates(
                startDate = startDate,
                endDate = endDate
            ).collect { result: ApiResult<List<TransactionResponse>> ->
                _transactionList.value = result
            }
        }
    }

    fun getCategoryPercentages(transactionList: List<TransactionResponse>) {
        val expenseTransactions = transactionList.filter { it.transactionType == "EXPENSE" }

        // Step 2: Map the Categories to their Count
        val categoryCounts = expenseTransactions.groupingBy { it.category }.eachCount()

        // Step 3: Calculate the Percentage of Each Category
        val totalExpenseTransactions = expenseTransactions.size
        val categoryPercentages =
            categoryCounts.mapValues { (_, count) -> (count.toDouble() / totalExpenseTransactions) * 100 }

        _categoryPercentages.value = categoryPercentages

    }

    fun getLocalDateFromLong(
        dateInLong: Long,
    ): String {

        val instant = Instant.ofEpochMilli(dateInLong)
        val formatter = dateTimeFormatter.withZone(ZoneId.of("UTC"))
        return formatter.format(instant)
    }

    companion object {
        private const val TAG = "AllTransactionsViewModel"
    }

}
