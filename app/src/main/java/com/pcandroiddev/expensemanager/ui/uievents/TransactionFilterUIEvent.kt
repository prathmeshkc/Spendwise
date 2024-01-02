package com.pcandroiddev.expensemanager.ui.uievents

sealed class TransactionFilterUIEvent {
    data class TransactionFilterChanged(val selectedFilter: String) : TransactionFilterUIEvent()
    /*object PreviousTransactionsButtonClicked : TransactionFilterUIEvent()
    object NextTransactionsButtonClicked : TransactionFilterUIEvent()*/
}
