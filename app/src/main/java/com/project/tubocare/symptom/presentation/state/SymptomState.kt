package com.project.tubocare.symptom.presentation.state

import com.project.tubocare.core.util.Resource
import com.project.tubocare.symptom.domain.model.Symptom
import java.time.LocalTime
import java.util.Date

data class SymptomState(
    val symptomList: Resource<List<Symptom>> = Resource.Loading(),

    val symptomId: String = "",
    val userId: String = "",
    val name: String = "",
    val date: Date? = null,
    val time: LocalTime? = null,
    val note: String = "",

    val isLoading: Boolean = false,
    val isLoadingDelete: Boolean = false,
    val isSuccessAdd: Boolean = false,
    val isSuccessGetSymptoms: Boolean = false,
    val isSuccessGetDetail: Boolean = false,
    val isSuccessUpdate: Boolean = false,
    val isSuccessDelete: Boolean = false,

    val errorName: String? = null,
    val errorDate: String? = null,
    val errorTime: String? = null,
    val errorNote: String? = null,

    val errorGetSymptoms: String? = null,
    val errorGetDetail: String? = null,
    val errorAdd: String? = null,
    val errorUpdate: String? = null,
    val errorDelete: String? = null,
)
