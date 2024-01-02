package com.pcandroiddev.expensemanager.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object Helper {

    fun stringToLocalDate(dateString: String): LocalDate {
        /*val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(dateString, formatter)

        // Convert LocalDate to ZonedDateTime at midnight
        val zonedDateTime = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, ZoneOffset.UTC)

        // Get the epoch milliseconds
        return zonedDateTime.toInstant().toEpochMilli()*/

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


    fun getLocalDateFromLong(
        dateInLong: Long,
    ): LocalDate {

        /*val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val instant = Instant.ofEpochMilli(dateInLong)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        return dateTimeFormatter.format(zonedDateTime)*/

        val instant = Instant.ofEpochMilli(dateInLong)
        return instant.atZone(ZoneId.of("UTC")).toLocalDate()
    }

    fun getFormattedStringDateFromLocalDate(localDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy").withZone(ZoneId.of("UTC"))
        return formatter.format(localDate)
    }


}

