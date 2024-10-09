package com.project.tubocare.weight.data.remote

import com.project.tubocare.core.data.local.TimeDto
import com.project.tubocare.weight.domain.model.Weight
import java.util.Date

data class WeightDto (
    val weightId: String = "",
    val userId: String = "",
    val date: Date? = null,
    val time: TimeDto? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val note: String? = null,
) {
    fun toWeight(): Weight {
        return Weight(
            weightId = weightId,
            userId = userId,
            date = date,
            time = time?.toLocalTime(),
            weight = weight,
            height = height,
            note = note
        )
    }

    companion object {
        fun fromWeight(weight: Weight): WeightDto {
            return WeightDto(
                weightId = weight.weightId,
                userId = weight.userId,
                date = weight.date,
                time = weight.time?.let { TimeDto.fromLocalTime(it) },
                weight = weight.weight,
                height = weight.height,
                note = weight.note
            )
        }
    }
}