package com.pcandroiddev.expensemanager.ui.states.ui

import com.pcandroiddev.expensemanager.data.local.transaction.TransactionType
import com.pcandroiddev.expensemanager.data.remote.TransactionResponse
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

    var titleError: Pair<Boolean, String> = Pair(false, "Name must not be empty"),
    var amountError: Pair<Boolean, String> = Pair(false, "Please enter some amount!"),
    var categoryError: Pair<Boolean, String> = Pair(false, "Select a category!"),
    var noteError: Pair<Boolean, String> = Pair(false, "Only whitespaces not allowed!")

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
