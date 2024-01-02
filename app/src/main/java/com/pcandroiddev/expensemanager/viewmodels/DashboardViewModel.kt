package com.pcandroiddev.expensemanager.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.data.local.filter.SearchFilters
import com.pcandroiddev.expensemanager.data.local.datastore.UserPreferencesManager
import com.pcandroiddev.expensemanager.data.local.filter.TransactionFilters
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import com.pcandroiddev.expensemanager.repository.auth.AuthRepository
import com.pcandroiddev.expensemanager.repository.transaction.TransactionRepository
import com.pcandroiddev.expensemanager.ui.rules.Validator
import com.pcandroiddev.expensemanager.ui.states.ui.SearchUIState
import com.pcandroiddev.expensemanager.ui.uievents.SearchTransactionUIEvent
import com.pcandroiddev.expensemanager.ui.uievents.TransactionFilterUIEvent
import com.pcandroiddev.expensemanager.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val dateTimeFormatter: DateTimeFormatter
) : ViewModel() {


    private val _transactionList =
        MutableStateFlow<ApiResult<List<TransactionResponse>>>(ApiResult.Success(emptyList()))
    val transactionList: MutableStateFlow<ApiResult<List<TransactionResponse>>> get() = _transactionList

    private val _searchedTransactionList =
        MutableStateFlow<ApiResult<List<TransactionResponse>>>(ApiResult.Success(emptyList()))
    val searchedTransactionList: StateFlow<ApiResult<List<TransactionResponse>>> get() = _searchedTransactionList

    var searchTransactionUIState = mutableStateOf(SearchUIState())

    private val allSearchValidationPassed = mutableStateOf(false)

    private val _currentlySelectedFilter = mutableStateOf(TransactionFilters.Monthly.name)
    val currentlySelectedFilter get() = _currentlySelectedFilter

    private val todayDate = LocalDate.now()
    private val locale = Locale.getDefault()
    private val weekFields = WeekFields.of(locale)


//    private val _currentTimeFrameText = mutableStateOf(getCurrentMonth())

    //TODO: Show this in the outlined button text
//    val currentTimeFrameText get() = _currentTimeFrameText

//    private val arrowsClickCount = mutableStateOf(0L)

    private var searchJob: Job? = null

    private fun getTodayDate(): String {
        return dateTimeFormatter.format(todayDate)
    }

    private fun getCurrentWeekDates(): String {
        val weekFields = WeekFields.of(locale)

        val firstDayOfWeek = todayDate.with(weekFields.dayOfWeek(), 1)
        val lastDayOfWeek = firstDayOfWeek.plusDays(6)

        val formattedStartDate = dateTimeFormatter.format(firstDayOfWeek)
        val formattedEndDate = dateTimeFormatter.format(lastDayOfWeek)
        return "$formattedStartDate - $formattedEndDate"
    }

    private fun getCurrentMonth(): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM", locale)
        return todayDate.format(formatter)
    }

    private fun getCurrentYear(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy", locale)
        return todayDate.format(formatter)
    }

    init {
        getAllTransaction(null)
    }

    fun getAllTransaction(selectedFilter: String?) {
        viewModelScope.launch {


            if (selectedFilter != null) {
                when (selectedFilter) {
                    TransactionFilters.Daily.name -> {
                        val formattedTodayDate = dateTimeFormatter.format(todayDate)

                        transactionRepository.getAllTransactionBetweenDates(
                            startDate = formattedTodayDate,
                            endDate = formattedTodayDate
                        ).collect { result: ApiResult<List<TransactionResponse>> ->
                            _transactionList.value = result
                        }
                    }

                    TransactionFilters.Weekly.name -> {
//                        val locale = Locale.getDefault()
                        val weekFields = WeekFields.of(locale)

                        val firstDayOfWeek = todayDate.with(weekFields.dayOfWeek(), 1)
                        val lastDayOfWeek = firstDayOfWeek.plusDays(6)

                        val formattedStartDate = dateTimeFormatter.format(firstDayOfWeek)
                        val formattedEndDate = dateTimeFormatter.format(lastDayOfWeek)
                        transactionRepository.getAllTransactionBetweenDates(
                            startDate = formattedStartDate,
                            endDate = formattedEndDate
                        ).collect { result: ApiResult<List<TransactionResponse>> ->
                            _transactionList.value = result
                        }
                    }

                    TransactionFilters.Monthly.name -> {

                        val firstDayOfMonth = todayDate.withDayOfMonth(1)
                        val lastDayOfMonth = todayDate.withDayOfMonth(todayDate.lengthOfMonth())

                        val formattedStartDate = dateTimeFormatter.format(firstDayOfMonth)
                        val formattedEndDate = dateTimeFormatter.format(lastDayOfMonth)

                        transactionRepository.getAllTransactionBetweenDates(
                            startDate = formattedStartDate,
                            endDate = formattedEndDate
                        ).collect { result: ApiResult<List<TransactionResponse>> ->
                            _transactionList.value = result
                        }
                    }

                    TransactionFilters.Yearly.name -> {

                        val firstDayOfYear = todayDate.withDayOfYear(1)
                        val lastDayOfYear = todayDate.withDayOfYear(todayDate.lengthOfYear())

                        val formattedStartDate = dateTimeFormatter.format(firstDayOfYear)
                        val formattedEndDate = dateTimeFormatter.format(lastDayOfYear)

                        transactionRepository.getAllTransactionBetweenDates(
                            startDate = formattedStartDate,
                            endDate = formattedEndDate
                        ).collect { result: ApiResult<List<TransactionResponse>> ->
                            _transactionList.value = result
                        }
                    }

                    else -> {
                        transactionRepository.getAllTransaction()
                            .collect { result: ApiResult<List<TransactionResponse>> ->
                                _transactionList.value = result
                            }
                    }
                }
            } else {
                val firstDayOfMonth = todayDate.withDayOfMonth(1)
                val lastDayOfMonth = todayDate.withDayOfMonth(todayDate.lengthOfMonth())

                val formattedStartDate = dateTimeFormatter.format(firstDayOfMonth)
                val formattedEndDate = dateTimeFormatter.format(lastDayOfMonth)

                Log.d(TAG, "getAllTransaction: formattedStartDate: $formattedStartDate")
                Log.d(TAG, "getAllTransaction: formattedEndDate: $formattedEndDate")

                transactionRepository.getAllTransactionBetweenDates(
                    startDate = formattedStartDate,
                    endDate = formattedEndDate
                ).collect { result: ApiResult<List<TransactionResponse>> ->
                    _transactionList.value = result
                }
            }

        }
    }

    fun onSearchTransactionEventChange(event: SearchTransactionUIEvent) {
        when (event) {
            is SearchTransactionUIEvent.SearchFilterChanged -> {
                searchTransactionUIState.value =
                    searchTransactionUIState.value.copy(filterType = event.searchFilter)
                printState()
            }

            is SearchTransactionUIEvent.SearchTextChanged -> {
                searchTransactionUIState.value =
                    searchTransactionUIState.value.copy(searchText = event.searchText)

                /*if (allSearchValidationPassed.value) {
                    searchTransactions()
                }*/
                printState()
            }

            SearchTransactionUIEvent.SearchTransactionButtonClicked -> {
                /* if (allSearchValidationPassed.value) {
                     searchTransactions()
                 }
                 printState()*/

            }

            SearchTransactionUIEvent.ResetSearchTransactionUIState -> {
                searchTransactionUIState.value = SearchUIState()
                return
            }

            else -> {}
        }
        validateDataWithRules()

        if (allSearchValidationPassed.value) {
            // Debounce the search text changes by delaying execution for 500 milliseconds
            if (event is SearchTransactionUIEvent.SearchTextChanged) {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(250)
                    searchTransactions()
                }
            } else {
                searchTransactions()
            }
        }
        printState()
    }

    fun onTransactionFilterEventChange(event: TransactionFilterUIEvent) {
        when (event) {
            is TransactionFilterUIEvent.TransactionFilterChanged -> {
                /* runBlocking {
                     userPreferencesManager.saveSelectedFilter(filter = event.selectedFilter)
                 }*/
                _currentlySelectedFilter.value = event.selectedFilter
                /*when (_currentlySelectedFilter.value) {
                    TransactionFilters.Daily.name -> {
                        _currentTimeFrameText.value = getTodayDate()
                    }

                    TransactionFilters.Weekly.name -> {
                        _currentTimeFrameText.value = getCurrentWeekDates()
                    }

                    TransactionFilters.Monthly.name -> {
                        _currentTimeFrameText.value = getCurrentMonth()
                    }

                    TransactionFilters.Yearly.name -> {
                        _currentTimeFrameText.value = getCurrentYear()
                    }
                }*/
                getAllTransaction(selectedFilter = event.selectedFilter)
            }


            /*TransactionFilterUIEvent.PreviousTransactionsButtonClicked -> {
                arrowsClickCount.value = arrowsClickCount.value - 1

                when (_currentlySelectedFilter.value) {
                    TransactionFilters.Daily.name -> {
                        val startDate =
                            dateTimeFormatter.format(todayDate.minusDays(abs(arrowsClickCount.value)))
                        val endDate = startDate
                        _currentTimeFrameText.value = startDate
                        getAllTransactionBetweenDates(startDate = startDate, endDate = endDate)

                    }

                    TransactionFilters.Weekly.name -> {
                        val dateTimeFormatterForWeek = DateTimeFormatter.ofPattern("MMM dd")
                        val firstDayOfWeek =
                            todayDate.minusWeeks(abs(arrowsClickCount.value))
                                .with(weekFields.dayOfWeek(), 1)
                        val lastDayOfWeek = firstDayOfWeek.plusDays(6)

                        val startDate = dateTimeFormatterForWeek.format(firstDayOfWeek)
                        val endDate = dateTimeFormatterForWeek.format(lastDayOfWeek)
                        _currentTimeFrameText.value = "$startDate - $endDate"

                        getAllTransactionBetweenDates(
                            startDate = dateTimeFormatter.format(firstDayOfWeek),
                            endDate = dateTimeFormatter.format(lastDayOfWeek)
                        )

                    }

                    TransactionFilters.Monthly.name -> {
                        val dateTimeFormatterForMonth =
                            DateTimeFormatter.ofPattern("MMMM", locale)

                        val firstDayOfMonth =
                            todayDate.minusMonths(abs(arrowsClickCount.value)).withDayOfMonth(1)
                        val lastDayOfMonth =
                            firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())

                        val currentMonth =
                            todayDate.minusMonths(abs(arrowsClickCount.value))
                                .format(dateTimeFormatterForMonth)

                        val startDate = dateTimeFormatter.format(firstDayOfMonth)
                        val endDate = dateTimeFormatter.format(lastDayOfMonth)
                        _currentTimeFrameText.value = currentMonth
                        getAllTransactionBetweenDates(startDate = startDate, endDate = endDate)

                    }

                    TransactionFilters.Yearly.name -> {
                        val dateTimeFormatterForYear = DateTimeFormatter.ofPattern("yyyy", locale)
                        val firstDayOfYear =
                            todayDate.minusYears(abs(2L)).withDayOfYear(1)
                        val lastDayOfYear =
                            firstDayOfYear.withDayOfYear(firstDayOfYear.lengthOfYear())

                        val currentYear = todayDate.minusYears(abs(2L))
                            .format(dateTimeFormatterForYear)

                        val startDate = dateTimeFormatter.format(firstDayOfYear)
                        val endDate = dateTimeFormatter.format(lastDayOfYear)


                        _currentTimeFrameText.value = currentYear
                        getAllTransactionBetweenDates(
                            startDate = startDate,
                            endDate = endDate
                        )

                    }
                }
            }

            TransactionFilterUIEvent.NextTransactionsButtonClicked -> {
                arrowsClickCount.value = arrowsClickCount.value + 1

                when (_currentlySelectedFilter.value) {
                    TransactionFilters.Daily.name -> {
                        val startDate =
                            dateTimeFormatter.format(todayDate.plusDays(abs(arrowsClickCount.value)))
                        _currentTimeFrameText.value = startDate
                        getAllTransactionBetweenDates(startDate = startDate, endDate = startDate)

                    }

                    TransactionFilters.Weekly.name -> {
                        val dateTimeFormatterForWeek = DateTimeFormatter.ofPattern("MMM dd")
                        val firstDayOfWeek =
                            todayDate.plusWeeks(abs(arrowsClickCount.value))
                                .with(weekFields.dayOfWeek(), 1)
                        val lastDayOfWeek = firstDayOfWeek.plusDays(6)

                        val startDate = dateTimeFormatterForWeek.format(firstDayOfWeek)
                        val endDate = dateTimeFormatterForWeek.format(lastDayOfWeek)
                        _currentTimeFrameText.value = "$startDate - $endDate"
                        getAllTransactionBetweenDates(
                            startDate = dateTimeFormatter.format(firstDayOfWeek),
                            endDate = dateTimeFormatter.format(lastDayOfWeek)
                        )

                    }

                    TransactionFilters.Monthly.name -> {
                        val dateTimeFormatterForMonth =
                            DateTimeFormatter.ofPattern("MMMM", locale)

                        val firstDayOfMonth =
                            todayDate.plusMonths(abs(arrowsClickCount.value)).withDayOfMonth(1)
                        val lastDayOfMonth =
                            firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())

                        val currentMonth =
                            todayDate.plusMonths(abs(arrowsClickCount.value))
                                .format(dateTimeFormatterForMonth)

                        val startDate = dateTimeFormatter.format(firstDayOfMonth)
                        val endDate = dateTimeFormatter.format(lastDayOfMonth)
                        _currentTimeFrameText.value = currentMonth

                        getAllTransactionBetweenDates(
                            startDate = startDate,
                            endDate = endDate
                        )

                    }

                    TransactionFilters.Yearly.name -> {
                        val dateTimeFormatterForYear = DateTimeFormatter.ofPattern("yyyy", locale)
                        val firstDayOfYear =
                            todayDate.plusYears(abs(arrowsClickCount.value)).withDayOfYear(1)
                        val lastDayOfYear =
                            firstDayOfYear.withDayOfYear(firstDayOfYear.lengthOfYear())

                        val currentYear = todayDate.plusYears(abs(arrowsClickCount.value))
                            .format(dateTimeFormatterForYear)

                        val startDate = dateTimeFormatter.format(firstDayOfYear)
                        val endDate = dateTimeFormatter.format(lastDayOfYear)

                        _currentTimeFrameText.value = currentYear
                        getAllTransactionBetweenDates(
                            startDate = startDate,
                            endDate = endDate
                        )

                    }
                }
            }*/
            else -> {

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


    private fun validateDataWithRules() {
        val searchTextError =
            Validator.validateSearchText(searchText = searchTransactionUIState.value.searchText)

        allSearchValidationPassed.value = searchTextError.status == true
    }


    private fun searchTransactions() {
        Log.d(TAG, "Inside_saveTransaction")
        validateDataWithRules()
        printState()

        searchJob?.cancel()

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            when (searchTransactionUIState.value.filterType) {
                SearchFilters.All.name -> {
                    transactionRepository.searchTransactionsByText(searchText = searchTransactionUIState.value.searchText)
                        .collect { result: ApiResult<List<TransactionResponse>> ->
                            _searchedTransactionList.value = result
                        }
                }

                SearchFilters.Income.name -> {
                    transactionRepository.searchTransactionByTypeAndText(
                        searchText = searchTransactionUIState.value.searchText,
                        transactionType = searchTransactionUIState.value.filterType
                    ).collect { result: ApiResult<List<TransactionResponse>> ->
                        _searchedTransactionList.value = result
                    }
                }

                SearchFilters.Expense.name -> {
                    transactionRepository.searchTransactionByTypeAndText(
                        searchText = searchTransactionUIState.value.searchText,
                        transactionType = searchTransactionUIState.value.filterType
                    ).collect { result: ApiResult<List<TransactionResponse>> ->
                        _searchedTransactionList.value = result
                    }
                }


            }
        }
    }

    fun resetSearchState() {
        _searchedTransactionList.value = ApiResult.Success(emptyList())
        searchJob?.cancel()
    }

    /*
        fun deleteToken() = viewModelScope.launch {
            tokenManager.deleteToken()
        }
    */

    fun logout() = viewModelScope.launch {
        userPreferencesManager.deleteToken()
//        userPreferencesManager.deleteSelectedFilter()
        authRepository.logout()
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, searchTransactionUIState.value.toString())
    }

    companion object {
        private const val TAG = "DashboardViewModel"
    }


}