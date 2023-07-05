package com.pcandroiddev.expensemanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.expensemanager.data.local.datastore.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {


    fun deleteToken() = viewModelScope.launch {
        tokenManager.deleteToken()
    }


}