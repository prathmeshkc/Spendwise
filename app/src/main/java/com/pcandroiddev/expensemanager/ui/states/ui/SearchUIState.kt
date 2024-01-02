package com.pcandroiddev.expensemanager.ui.states.ui

import com.pcandroiddev.expensemanager.data.local.filter.SearchFilters

data class SearchUIState(
    var filterType: String = SearchFilters.All.name,
    var searchText: String = "",

    var searchTextError: Boolean = false
)
