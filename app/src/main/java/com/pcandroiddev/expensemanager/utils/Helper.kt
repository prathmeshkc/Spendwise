package com.pcandroiddev.expensemanager.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

object Helper {

    fun stringToLocalDate(dateString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(dateString, formatter)

        // Convert LocalDate to ZonedDateTime at midnight
        val zonedDateTime = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, ZoneOffset.UTC)

        // Get the epoch milliseconds
        return zonedDateTime.toInstant().toEpochMilli()
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


    fun getLocalDateFromLong(
        dateInLong: Long,
    ): String {

        val instant = Instant.ofEpochMilli(dateInLong)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.of("UTC"))
        return formatter.format(instant)
    }


}

