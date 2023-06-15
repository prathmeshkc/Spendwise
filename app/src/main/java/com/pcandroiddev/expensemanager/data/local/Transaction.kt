package com.pcandroiddev.expensemanager.data.local

import java.text.DateFormat

data class Transaction(
    val id: Int,
    val title: String,
    val amount: Double,
    val transactionType: TransactionType,
    val category: String,
    val date: String,
    val note: String,
    var createdAt: Long = System.currentTimeMillis()
) {
    val createdAtDateFormat: String get() = DateFormat.getDateTimeInstance().format(createdAt)
}
