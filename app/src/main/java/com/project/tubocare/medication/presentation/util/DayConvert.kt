package com.project.tubocare.medication.presentation.util

import com.google.firebase.Timestamp
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun getDayOfWeekInIndonesian(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Senin"
        DayOfWeek.TUESDAY -> "Selasa"
        DayOfWeek.WEDNESDAY -> "Rabu"
        DayOfWeek.THURSDAY -> "Kamis"
        DayOfWeek.FRIDAY -> "Jumat"
        DayOfWeek.SATURDAY -> "Sabtu"
        DayOfWeek.SUNDAY -> "Minggu"
    }
}

fun getDayOfWeek(day: String): Int {
    return when(day){
        "Minggu" -> Calendar.SUNDAY
        "Senin" -> Calendar.MONDAY
        "Selasa" -> Calendar.TUESDAY
        "Rabu" -> Calendar.WEDNESDAY
        "Kamis" -> Calendar.THURSDAY
        "Jumat" -> Calendar.FRIDAY
        "Sabtu" -> Calendar.SATURDAY
        else -> Calendar.SUNDAY
    }
}

fun formatTimeTo12Hour(localTime: LocalTime): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return localTime.format(formatter)
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    val instant = Instant.ofEpochSecond(seconds, nanoseconds.toLong())
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}

fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = atZone(ZoneId.systemDefault()).toInstant()
    return Timestamp(instant.epochSecond, instant.nano)
}