package com.pcandroiddev.expensemanager.data.local

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Transaction(
    val title: String,
    val amount: String,
    val transactionType: String,
    val category: String,
    val transactionDate: String,
    val note: String,
    var createdAt: Long = System.currentTimeMillis()
) {
    val createdAtDateFormat: String get() = DateFormat.getDateTimeInstance().format(createdAt)
}
