package com.pcandroiddev.expensemanager.utils.networkstate

sealed interface NetworkState {
    object Available : NetworkState
    object Lost : NetworkState
    object Losing : NetworkState
    object Unavailable : NetworkState
}