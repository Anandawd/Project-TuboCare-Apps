package com.project.tubocare.core.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

// function for formatting local time to string (ex: 12:00 PM)
fun formatLocalTimeToString(time: LocalTime): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return time.format(timeFormatter)
}

// function for formatting date to string (ex: 12/02/2023)
fun formatDateToString(date: Date, locale: Locale = Locale.getDefault()): String {
    val dateFormatter = SimpleDateFormat("dd/MM/yyy", locale)
    return dateFormatter.format(date)
}

fun formatDateToString2(date: Date?): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return date?.let { format.format(it) } ?: ""
}



// function for formatting date to day month (ex: 12 Feb)
fun formatDateToDayMonth(date: Date, locale: Locale = Locale.getDefault()): String {
    val dateFormatter = SimpleDateFormat("dd MMM", locale)
    return dateFormatter.format(date)
}

// function for formatting date to day month year (ex: 12 Feb 2023)
fun formatDateToDayMonthYear(date: Date, locale: Locale = Locale.getDefault()): String {
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", locale)
    return dateFormatter.format(date)
}




// function for formatting local date time to string (ex: 12:00 PM)
fun formatLocalDateTimeToHourMinute(time: LocalDateTime): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return time.format(timeFormatter)
}

fun formatLocalTimeToHourMinute(time: LocalTime?): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return time?.format(timeFormatter) ?: ""
}

fun formatLocalDateTimeToString(date: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy")
    return date.format(dateFormatter)
}

// function for formatting date to day month (ex: 12 Feb)
fun formatLocalDateTimeToDayMonth(date: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")
    return date.format(dateFormatter)
}

fun formatDateToDayMonth(date: Date?): String {
    val localDateTime = date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")
    return localDateTime?.format(dateFormatter) ?: ""
}

fun formatLocalDateTimeToDayMonthYear(date: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return date.format(dateFormatter)
}

fun formatLocalDateTimeToDayMonthYearHourMinute(date: LocalDateTime): String {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    return date.format(dateFormatter)
}

fun formatLocalDateToDayMonthYear(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    return date.format(formatter)
}


fun formatDateToDayMonthYear(date: Date?): String {
    val localDateTime = date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return localDateTime?.format(dateFormatter) ?: ""
}

/*
fun getAppointmentLabel(dateTime: LocalDateTime): String {
    val today = LocalDateTime.now()

    return when {
        dateTime.toLocalDate().isEqual(today.toLocalDate()) -> "Hari ini pukul ${formatLocalDateTimeToHourMinute(dateTime)}"
        dateTime.toLocalDate().isEqual(today.plusDays(1).toLocalDate()) -> "Besok pukul ${formatLocalDateTimeToHourMinute(dateTime)}"
        else -> "${formatLocalDateTimeToDayMonth(dateTime)} pukul ${formatLocalDateTimeToHourMinute(dateTime)}"
    }
}
*/

fun getAppointmentLabel(date: Date, time: LocalTime): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val dateTime = LocalDateTime.of(localDate, time)
    val today = LocalDateTime.now()

    return when {
        dateTime.toLocalDate().isEqual(today.toLocalDate()) -> "Hari ini pukul ${formatLocalDateTimeToHourMinute(dateTime)}"
        dateTime.toLocalDate().isEqual(today.plusDays(1).toLocalDate()) -> "Besok pukul ${formatLocalDateTimeToHourMinute(dateTime)}"
        else -> "${formatLocalDateTimeToDayMonth(dateTime)} pukul ${formatLocalDateTimeToHourMinute(dateTime)}"
    }
}

fun parseStringToDate(dateString: String): Date? {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        format.parse(dateString)
    } catch (e: ParseException) {
        null
    }
}

fun formatInstantToHourMinute(time: Instant): String {
    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = ZonedDateTime.ofInstant(time, zoneId)
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return zonedDateTime.format(timeFormatter)
}

fun Date.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}