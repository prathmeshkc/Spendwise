package com.pcandroiddev.expensemanager.ui.uievents

sealed class TransactionDetailsUIEvent {
    data class DeleteTransactionButtonClicked(val transactionId: String) :
        TransactionDetailsUIEvent()
}
