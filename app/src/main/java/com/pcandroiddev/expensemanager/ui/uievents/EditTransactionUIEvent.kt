package com.pcandroiddev.expensemanager.ui.uievents

sealed class EditTransactionUIEvent {
    data class TitleChanged(val title: String) : EditTransactionUIEvent()
    data class AmountChanged(val amount: Double) : EditTransactionUIEvent()
    data class TransactionTypeChanged(val transactionType: String) : EditTransactionUIEvent()
    data class CategoryChanged(val category: String) : EditTransactionUIEvent()
    data class DateChanged(val date: String) : EditTransactionUIEvent()
    data class NoteChanged(val note: String) : EditTransactionUIEvent()
    object EditTransactionButtonClicked : EditTransactionUIEvent()
}
