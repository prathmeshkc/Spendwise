package com.pcandroiddev.expensemanager.viewmodels

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.data.local.filter.StatementOptions
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.ui.uievents.AllTransactionTopBarUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import com.pcandroiddev.expensemanager.utils.pdf.PDFService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class AllTransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val dateTimeFormatter: DateTimeFormatter
) : ViewModel() {


    private val _transactionList =
        MutableStateFlow<ApiResult<List<TransactionResponse>>>(ApiResult.Success(emptyList()))

    val transactionList: StateFlow<ApiResult<List<TransactionResponse>>> get() = _transactionList

    /*private val _categoryPercentages = mutableStateOf(emptyMap<String, Double>())
    val categoryPercentages: State<Map<String, Double>> get() = _categoryPercentages*/

    private var statementGenerationJob: Job? = null

    private val _statementGenerationErrorState = MutableStateFlow<String?>(null)
    private val _statementGenerationSuccessState = MutableStateFlow<File?>(null)
    val statementGenerationErrorState: StateFlow<String?> get() = _statementGenerationErrorState.asStateFlow()
    val statementGenerationSuccessState: StateFlow<File?> get() = _statementGenerationSuccessState.asStateFlow()


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

    fun onTopBarEventChange(event: AllTransactionTopBarUIEvent) {
        when (event) {
            is AllTransactionTopBarUIEvent.DateRangeChanged -> {

                val startDate = getLocalDateFromLong(
                    dateInLong = event.startDate
                )

                val endDate = getLocalDateFromLong(
                    dateInLong = event.endDate
                )

                Log.d(TAG, "onDateRangeEventChange: $startDate - $endDate")

                getAllTransactionBetweenDates(startDate, endDate)
            }

            is AllTransactionTopBarUIEvent.DownloadStatement -> {

                generateStatement(
                    transactionList = event.transactionList,
                    startDate = event.startDate,
                    endDate = event.endDate,
                    income = event.income,
                    expense = event.expense,
                    fileType = event.fileType
                )
            }

            else -> {
                Log.d(TAG, "onTopBarEventChange: Else branch")
            }
        }
    }

    fun getAllTransactionBetweenDates(startDate: String, endDate: String) {
        viewModelScope.launch {
            transactionRepository.getAllTransactionBetweenDates(
                startDate = startDate,
                endDate = endDate
            ).collect { result: ApiResult<List<TransactionResponse>> ->
                _transactionList.value = result
            }
        }
    }


    /*TODO*/
    private fun generateStatement(
        transactionList: List<TransactionResponse>,
        startDate: String,
        endDate: String,
        income: String,
        expense: String,
        fileType: String
    ) {
        Log.d(TAG, "downloadStatement: $startDate $endDate $income $expense $fileType")
        statementGenerationJob?.cancel()

        when (fileType) {
            StatementOptions.PDF.name -> {
                generatePDF(transactionList, startDate, endDate, income, expense)
            }

            StatementOptions.EXCEL.name -> {
                generateExcel(transactionList, startDate, endDate, income, expense)
            }
        }
    }


    private fun generatePDF(
        transactionList: List<TransactionResponse>,
        startDate: String,
        endDate: String,
        income: String,
        expense: String
    ) {

        val pdfService = PDFService()
        pdfService.generatePDF(
            transactionList = transactionList,
            startDate = startDate,
            endDate = endDate,
            incomeAmt = income,
            expenseAmt = expense,
            onError = { exception ->
                _statementGenerationErrorState.value = exception.message
            },
            onSuccess = { file ->
                _statementGenerationSuccessState.value = file
            }
        )
    }

    fun clearStatementGenerationState() {
        _statementGenerationErrorState.value = null
    }

    private fun generateExcel(
        transactionList: List<TransactionResponse>,
        startDate: String,
        endDate: String,
        income: String,
        expense: String
    ) {

    }

    /*fun getCategoryPercentages(transactionList: List<TransactionResponse>) {
        val expenseTransactions = transactionList.filter { it.transactionType == "EXPENSE" }

        // Step 2: Map the Categories to their Count
        val categoryCounts = expenseTransactions.groupingBy { it.category }.eachCount()

        // Step 3: Calculate the Percentage of Each Category
        val totalExpenseTransactions = expenseTransactions.size
        val categoryPercentages =
            categoryCounts.mapValues { (_, count) -> (count.toDouble() / totalExpenseTransactions) * 100 }

        _categoryPercentages.value = categoryPercentages

    }*/

    fun getLocalDateFromLong(
        dateInLong: Long,
    ): String {

        val instant = Instant.ofEpochMilli(dateInLong)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.of("UTC"))
        return formatter.format(instant)

        /*val instant = Instant.ofEpochMilli(dateInLong)
        val formatter = dateTimeFormatter.withZone(ZoneId.systemDefault())
        return formatter.format(instant)*/

        /*val instant = Instant.ofEpochMilli(dateInLong)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        return dateTimeFormatter.format(zonedDateTime)*/
    }


    fun isOfMonthYearFormat(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)

        return try {
            YearMonth.parse(dateString, formatter)
            true // Parsing succeeded, string is in the correct format
        } catch (e: DateTimeParseException) {
            false // Parsing failed, string is not in the correct format
        }
    }

    fun getCurrentMonthDates(): Pair<String, String> {
        val todayDate = LocalDate.now()
        val firstDayOfMonth = todayDate.withDayOfMonth(1)
        val lastDayOfMonth = todayDate.withDayOfMonth(todayDate.lengthOfMonth())

        val formattedStartDate = dateTimeFormatter.format(firstDayOfMonth)
        val formattedEndDate = dateTimeFormatter.format(lastDayOfMonth)

        return Pair(formattedStartDate, formattedEndDate)
    }


    companion object {
        private const val TAG = "AllTransactionsViewModel"
    }

}
