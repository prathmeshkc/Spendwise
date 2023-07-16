package com.pcandroiddev.expensemanager.ui.states.ui

import com.pcandroiddev.expensemanager.data.local.TransactionType
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AddTransactionUIState(
    var title: String = "",
    var amount: Double = 0.0,
    var transactionType: String = TransactionType.EXPENSE.name,
    var category: String = "",
    val date: String = DateTimeFormatter
        .ofPattern("MMM dd, yyyy")
        .format(LocalDate.now()),
    var note: String = "",

    var titleError: Boolean = false,
    var amountError: Boolean = false,
    var categoryError: Boolean = false,
    var noteError: Boolean = false

) {
    constructor(transactionResponse: TransactionResponse) : this(
        title = transactionResponse.title,
        amount = transactionResponse.amount,
        transactionType = transactionResponse.transactionType,
        category = transactionResponse.category,
        date = transactionResponse.transactionDate,
        note = transactionResponse.note,
    )
}
