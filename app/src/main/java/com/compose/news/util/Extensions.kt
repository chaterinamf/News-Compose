package com.compose.news.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String?.orDash() = this ?: "-"

fun String?.toCountryName(): String {
    return try {
        val locale = Locale("", this?.uppercase().orEmpty())
        locale.displayCountry
    } catch (e: Exception) {
        e.printStackTrace()
        "Unknown Country..."
    }
}

fun String?.convertDateFormat(): String {
    return try {
        val originalFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val parsedDate = originalFormatter.parse(this.orEmpty())
        val outputFormatter = SimpleDateFormat("d MMM yyyy", Locale.UK)

        outputFormatter.format(parsedDate as Date)
    } catch (e: Exception) {
        "Invalid date format"
    }
}