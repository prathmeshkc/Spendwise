package com.pcandroiddev.expensemanager.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

object Helper {

    fun stringToLocalDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        return LocalDate.parse(dateString, formatter)
    }

    /*fun stringifyTotalBalance(balance: Double): String {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(balance)
    }

    fun formatAmount(amount: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        return numberFormat.format(amount)
    }*/

     fun formatDoubleWithTwoDecimals(value: Double): String {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(value)
    }

    fun formatAmountWithLocale(amount: Double, locale: Locale = Locale.getDefault()): String {
        val roundedAmount = formatDoubleWithTwoDecimals(amount)
        val numberFormat = NumberFormat.getNumberInstance(locale)
        return numberFormat.format(roundedAmount.toDouble())
    }



}

