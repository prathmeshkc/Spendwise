package com.pcandroiddev.expensemanager.data.local

import java.text.DateFormat

data class Transaction(
    val title: String,
    val amount: String,
    val transactionType: String,
    val category: String,
    val date: String,
    val note: String,
    var createdAt: Long = System.currentTimeMillis()
) {
    val createdAtDateFormat: String get() = DateFormat.getDateTimeInstance().format(createdAt)
}
