package com.pcandroiddev.expensemanager.ui.uievents

sealed class DateRangeUIEvent {
    data class DateRangeChanged(val startDate: Long, val endDate: Long) : DateRangeUIEvent()
}
