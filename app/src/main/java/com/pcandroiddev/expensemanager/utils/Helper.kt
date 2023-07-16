package com.pcandroiddev.expensemanager.utils

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object Helper {
    fun stringToLocalDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        return LocalDate.parse(dateString, formatter)
    }

    fun stringifyTotalBalance(balance: Double): String {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(balance)
    }


}

