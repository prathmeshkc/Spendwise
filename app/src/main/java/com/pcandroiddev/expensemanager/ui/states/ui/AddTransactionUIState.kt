package com.pcandroiddev.expensemanager.ui.states.ui

import com.pcandroiddev.expensemanager.data.local.TransactionType
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AddTransactionUIState(
    var title: String = "",
    var amount: String = "",
    var transactionType: String = TransactionType.EXPENSE.name,
    var category: String = "",
    val date: String = DateTimeFormatter
        .ofPattern("MMM dd, yyyy")
        .format(LocalDate.now()),
    var note: String = "",
    private var _createdAt: Long = System.currentTimeMillis(),

    var titleError: Boolean = false,
    var amountError: Boolean = false,
    var categoryError: Boolean = false,
    var noteError: Boolean = false

) {
    val createdAt: String
        get() = DateFormat.getDateTimeInstance().format(_createdAt)
}
