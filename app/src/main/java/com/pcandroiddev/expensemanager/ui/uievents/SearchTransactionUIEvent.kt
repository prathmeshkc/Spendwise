package com.pcandroiddev.expensemanager.ui.uievents

sealed class SearchTransactionUIEvent {
    data class SearchTextChanged(val searchText: String) : SearchTransactionUIEvent()
    data class SearchFilterChanged(val searchFilter: String) : SearchTransactionUIEvent()
    object SearchTransactionButtonClicked : SearchTransactionUIEvent()
    object ResetSearchTransactionUIState : SearchTransactionUIEvent()
}
