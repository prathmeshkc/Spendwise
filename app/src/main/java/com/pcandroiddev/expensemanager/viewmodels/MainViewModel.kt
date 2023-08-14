package com.pcandroiddev.expensemanager.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.utils.networkstate.NetworkManager
import com.pcandroiddev.expensemanager.utils.networkstate.NetworkState
import com.pcandroiddev.expensemanager.utils.orientationstate.OrientationManager
import com.pcandroiddev.expensemanager.utils.orientationstate.OrientationState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkManager: NetworkManager,
    orientationManager: OrientationManager,
    @ApplicationContext context: Context
) : ViewModel() {

    val networkState: StateFlow<NetworkState> = networkManager.getNetworkState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000L),
        initialValue = NetworkState.Available
    )

    /*val orientationState: StateFlow<OrientationState> =
        orientationManager.getOrientationState(context).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000L),
            initialValue = OrientationState.Portrait
        )*/

}