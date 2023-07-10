package com.pcandroiddev.expensemanager.data.local

data class Transaction(
    val title: String,
    val amount: String,
    val transactionType: String,
    val category: String,
    val transactionDate: String,
    val note: String,
)
