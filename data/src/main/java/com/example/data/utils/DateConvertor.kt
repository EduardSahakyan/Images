package com.example.data.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun convertDate(dateString: String): String {
    val outputFormat = "dd/MM/yyyy"
    val zonedDateTime = ZonedDateTime.parse(dateString)
    val formatter = DateTimeFormatter.ofPattern(outputFormat)
    return zonedDateTime.format(formatter)
}