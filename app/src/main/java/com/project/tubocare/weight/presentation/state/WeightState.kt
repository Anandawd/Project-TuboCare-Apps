package com.project.tubocare.weight.presentation.state

import com.project.tubocare.core.util.Resource
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.weight.domain.model.Weight
import java.time.LocalTime
import java.util.Date

data class WeightState(
    val weightList: Resource<List<Weight>> = Resource.Loading(),

    val weightId: String = "",
    val userId: String = "",
    val date: Date? = null,
    val time: LocalTime? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val note: String? = null,

    val previousWeight: Int? = null,

    val isLoading: Boolean = false,
    val isLoadingDelete: Boolean = false,
    val isSuccessAdd: Boolean = false,
    val isSuccessGetWeights: Boolean = false,
    val isSuccessGetDetail: Boolean = false,
    val isSuccessUpdate: Boolean = false,
    val isSuccessDelete: Boolean = false,

    val errorDate: String? = null,
    val errorTime: String? = null,
    val errorWeight: String? = null,
    val errorHeight: String? = null,
    val errorNote: String? = null,

    val errorGetWeights: String? = null,
    val errorGetDetail: String? = null,
    val errorAdd: String? = null,
    val errorUpdate: String? = null,
    val errorDelete: String? = null,
)