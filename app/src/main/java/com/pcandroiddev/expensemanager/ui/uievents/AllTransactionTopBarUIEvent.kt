package com.pcandroiddev.expensemanager.ui.uievents

import com.pcandroiddev.expensemanager.data.remote.TransactionResponse

sealed class AllTransactionTopBarUIEvent {
    data class DateRangeChanged(val startDate: Long, val endDate: Long) :
        AllTransactionTopBarUIEvent()

    data class DownloadStatement(
        val transactionList: List<TransactionResponse>,
        val startDate: String,
        val endDate: String,
        val income: String,
        val expense: String,
        val fileType: String
    ) :
        AllTransactionTopBarUIEvent()
}
