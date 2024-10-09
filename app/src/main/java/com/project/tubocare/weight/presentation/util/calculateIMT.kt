package com.project.tubocare.weight.presentation.util

fun calculateIMT(weight: Float, height: Float): Float {
    val heightInMeters = height / 100
    return if (heightInMeters > 0) weight / (heightInMeters * heightInMeters) else 0f
}