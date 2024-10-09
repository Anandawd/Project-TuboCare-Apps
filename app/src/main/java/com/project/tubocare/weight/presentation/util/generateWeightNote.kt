package com.project.tubocare.weight.presentation.util

fun generateWeightNote(
    previousWeight: Float?,
    currentWeight: Float
): String {
    return when {
        previousWeight == null -> "Data berat badan pertama"
        currentWeight > previousWeight -> "Berat badan naik"
        currentWeight < previousWeight -> "Berat badan turun"
        else -> "Berat badan stabil"
    }
}
