package com.pcandroiddev.expensemanager.ui.uievents

sealed class AddTransactionUIEvent {
    data class TitleChanged(val title: String) : AddTransactionUIEvent()
    data class AmountChanged(val amount: Double) : AddTransactionUIEvent()
    data class TransactionTypeChanged(val transactionType: String) : AddTransactionUIEvent()
    data class CategoryChanged(val category: String) : AddTransactionUIEvent()
    data class DateChanged(val date: String) : AddTransactionUIEvent()
    data class NoteChanged(val note: String) : AddTransactionUIEvent()

    object SaveTransactionButtonClicked : AddTransactionUIEvent()
}
