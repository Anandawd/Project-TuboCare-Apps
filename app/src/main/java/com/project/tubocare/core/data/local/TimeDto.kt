package com.project.tubocare.core.data.local

import java.time.LocalTime

data class TimeDto(
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val nano: Int = 0,
) {
    fun toLocalTime(): LocalTime {
        return LocalTime.of(hour, minute, second, nano)
    }

    companion object {
        fun fromLocalTime(localTime: LocalTime): TimeDto {
            return TimeDto(
                hour = localTime.hour,
                minute = localTime.minute,
                second = localTime.second,
                nano = localTime.nano
            )
        }
    }
}
