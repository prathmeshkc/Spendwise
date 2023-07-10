package com.pcandroiddev.expensemanager.data.remote


data class TransactionResponse(
    val transactionId: String,
    val userId: String,
    val title: String,
    val amount: Double,
    val transactionType: String,
    val category: String,
    val transactionDate: String,
    val note: String
)
