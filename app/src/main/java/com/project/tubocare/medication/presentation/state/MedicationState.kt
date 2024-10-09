package com.project.tubocare.medication.presentation.state

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.project.tubocare.core.util.Resource
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.util.ResourceTypeAdapter
import java.time.LocalTime

data class MedicationState(
    val medicationList: Resource<List<Medication>> = Resource.Loading(),

    val userName: String = "",

    val userId: String = "",
    val medicationId: String = "",
    val name: String = "",
    val weeklySchedule: Map<String, List<ChecklistEntry>> = emptyMap(),
    var frequency: String = "",
    val instruction: String = "",
    val remain: Int? = null,
    val dosage: Int? = null,
    val note: String = "",
    val image: String = "",

    val tempTimes: List<LocalTime?> = emptyList(),

    val isLoading: Boolean = false,
    val isLoadingDelete: Boolean = false,
    val isSuccessGetDetail: Boolean = false,
    val isSuccessUploadImage: Boolean = false,
    val isSuccessUpdate: Boolean = false,
    val isSuccessDelete: Boolean = false,
    val isSuccessAdd: Boolean = false,

    val errorName: String? = null,
    val errorSchedule: String? = null,
    val errorTimes: String? = null,
    val errorFrequency: String? = null,
    val errorInstruction: String? = null,
    val errorRemain: String? = null,
    val errorDosage: String? = null,
    val errorNote: String? = null,
    val errorImage: String? = null,

    val errorGetMedications: String? = null,
    val errorGetDetail: String? = null,
    val errorUploadImage: String? = null,
    val errorAdd: String? = null,
    val errorUpdate: String? = null,
    val errorDelete: String? = null,

    val selectedMedication: Medication? = null
)