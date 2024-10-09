package com.project.tubocare.profile.presentation.util

import android.icu.util.Calendar
import java.util.Date

fun calculateAge(bdate: Date): Int {
    val birthCalendar = Calendar.getInstance()
    birthCalendar.time = bdate
    val today = Calendar.getInstance()

    var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

    if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)){
        age--
    }

    return age
}