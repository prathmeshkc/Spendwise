package com.pcandroiddev.expensemanager.data.local.transaction

//TODO: Remove this file if not needed
data class Transaction(
    val title: String,
    val amount: String,
    val transactionType: String,
    val category: String,
    val transactionDate: String,
    val note: String,
)
