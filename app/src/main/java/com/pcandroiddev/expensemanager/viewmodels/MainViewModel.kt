package com.pcandroiddev.expensemanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.utils.NetworkManager
import com.pcandroiddev.expensemanager.utils.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(networkManager: NetworkManager) : ViewModel() {

    val networkState: StateFlow<NetworkState> = networkManager.getNetworkState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000L),
        initialValue = NetworkState.Unavailable
    )

}