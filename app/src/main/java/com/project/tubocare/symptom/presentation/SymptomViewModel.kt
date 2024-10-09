package com.project.tubocare.symptom.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.core.util.Resource
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.symptom.domain.repository.SymptomRepository
import com.project.tubocare.symptom.presentation.event.SymptomEvent
import com.project.tubocare.symptom.presentation.state.SymptomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SymptomViewModel @Inject constructor(
    private val symptomRepository: SymptomRepository,
    private val application: Application
): ViewModel(){
    private val hasUser = symptomRepository.hasUser()
    private val userId = symptomRepository.getUserId()
    private val symptomId = symptomRepository.getSymptomId()

    var symptomState by mutableStateOf(SymptomState())
        private set

    init {
        viewModelScope.launch {
            getSymptoms()
        }
    }

    fun onEvent(event: SymptomEvent) {
        when (event) {
            SymptomEvent.GetSymptoms -> {
                getSymptoms()
            }

            SymptomEvent.AddSymptom -> {
                addSymptom()
            }

            SymptomEvent.UpdateSymptom -> {
                updateSymptom()
            }

            SymptomEvent.ResetStatus -> {
                resetStatus()
            }

            SymptomEvent.ClearToast -> {
                clearToastMessage()
            }

            is SymptomEvent.GetSymptomById -> {
                getSymptomById(event.value)
            }

            is SymptomEvent.DeleteSymptom -> {
                deleteSymptom(event.value)
            }

            is SymptomEvent.OnNameChanged -> {
                onNameChanged(event.value)
            }

            is SymptomEvent.OnDateChanged -> {
                onDateChanged(event.value)
            }

            is SymptomEvent.OnTimeChanged -> {
                onTimeChanged(event.value)
            }

            is SymptomEvent.OnNoteChanged -> {
                onNoteChanged(event.value)
            }
        }
    }

    private fun getSymptoms() = viewModelScope.launch {
        if (hasUser) {
            symptomRepository.getSymptoms(userId).collect {
                when (it) {
                    is Resource.Success -> {
                        symptomState = symptomState.copy(symptomList = it)
                    }

                    is Resource.Error -> {
                        symptomState = symptomState.copy(errorGetSymptoms = it.message)
                    }

                    else -> { /* no-op */
                    }
                }
            }
        }
    }

    private fun getSymptomById(id: String) = viewModelScope.launch {
        symptomRepository.getSymptomById(id).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { symptom ->
                        symptomState = symptomState.copy(
                            symptomId = id,
                            userId = symptom.userId,
                            name = symptom.name,
                            date = symptom.date,
                            time = symptom.time,
                            note = symptom.note,
                        )
                    }
                }

                is Resource.Error -> {
                    symptomState = symptomState.copy(
                        errorGetDetail = result.message
                    )
                }

                else -> { /* no-op */
                }
            }
        }
    }

    private fun addSymptom() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            symptomState = symptomState.copy(errorAdd = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                symptomState = symptomState.copy(isLoading = true)

                val symptom = Symptom(
                    symptomId = symptomId,
                    userId = userId,
                    name = symptomState.name,
                    date = symptomState.date,
                    time = symptomState.time,
                    note = symptomState.note,
                )

                val result = symptomRepository.addSymptom(symptom)

                when (result) {
                    is Resource.Success -> {
                        symptomState = symptomState.copy(isSuccessAdd = true)
                    }

                    is Resource.Error -> {
                        symptomState = symptomState.copy(errorAdd = result.message)
                    }

                    else -> { /* no-op */
                    }
                }
            } else {
                throw IllegalArgumentException("Lengkapi formulir terlebih dahulu")
            }
        } catch (e: Exception) {
            symptomState = symptomState.copy(errorAdd = e.localizedMessage)
        } finally {
            symptomState = symptomState.copy(isLoading = false)
        }
    }

    private fun updateSymptom() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            symptomState = symptomState.copy(errorUpdate = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                symptomState = symptomState.copy(isLoading = true)

                val symptom = Symptom(
                    symptomId = symptomState.symptomId,
                    userId = symptomState.userId,
                    name = symptomState.name,
                    date = symptomState.date,
                    time = symptomState.time,
                    note = symptomState.note,
                )

                val result = symptomRepository.updateSymptom(symptom)

                when (result) {
                    is Resource.Success -> {
                        symptomState = symptomState.copy(isSuccessUpdate = true)
                    }

                    is Resource.Error -> {
                        symptomState = symptomState.copy(errorUpdate = result.message)
                    }

                    else -> { /* no-op */
                    }
                }
            } else {
                throw IllegalArgumentException("Lengkapi formulir terlebih dahulu")
            }
        } catch (e: Exception) {
            symptomState = symptomState.copy(errorUpdate = e.localizedMessage)
        } finally {
            symptomState = symptomState.copy(isLoading = false)
        }
    }

    private fun deleteSymptom(id: String) = viewModelScope.launch {
        if (!isInternetAvailable()) {
            symptomState = symptomState.copy(errorDelete = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            symptomState = symptomState.copy(isLoadingDelete = true)
            val result = symptomRepository.deleteSymptom(id)
            when (result) {
                is Resource.Success -> {
                    symptomState = symptomState.copy(isSuccessDelete = true)
                }

                is Resource.Error -> {
                    symptomState = symptomState.copy(errorDelete = result.message)
                }

                else -> { /* no-op */
                }
            }
        } catch (e: Exception) {
            symptomState = symptomState.copy(errorDelete = e.localizedMessage)
        } finally {
            symptomState = symptomState.copy(isLoadingDelete = false)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun resetStatus() {
        symptomState = symptomState.copy(
            isSuccessAdd = false,
            isSuccessUpdate = false,
            isSuccessDelete = false
        )
    }

    private fun clearToastMessage() {
        symptomState = symptomState.copy(
            errorGetSymptoms = null,
            errorGetDetail = null,
            errorAdd = null,
            errorUpdate = null,
            errorDelete = null
        )
    }

    private fun onNameChanged(name: String) {
        symptomState = symptomState.copy(name = name)
        if (name.isBlank()) {
            symptomState = symptomState.copy(errorName = "Field tidak boleh kosong")
        } else {
            symptomState = symptomState.copy(errorName = null)
        }
    }

    private fun onDateChanged(date: Date?) {
        symptomState = symptomState.copy(date = date)
        if (date == null) {
            symptomState = symptomState.copy(errorDate = "Field tidak boleh kosong")
        } else {
            symptomState = symptomState.copy(errorDate = null)
        }
    }

    private fun onTimeChanged(time: LocalTime?) {
        symptomState = symptomState.copy(time = time)
        if (time == null) {
            symptomState = symptomState.copy(errorTime = "Field tidak boleh kosong")
        } else {
            symptomState = symptomState.copy(errorTime = null)
        }
    }

    private fun onNoteChanged(note: String) {
        symptomState = symptomState.copy(note = note)
        if (note.isBlank()) {
            symptomState = symptomState.copy(errorNote = "Field tidak boleh kosong")
        } else {
            symptomState = symptomState.copy(errorNote = null)
        }
    }

    private fun validateForm(): Boolean {
        return symptomState.errorName == null &&
                symptomState.errorDate == null &&
                symptomState.errorTime == null &&
                symptomState.errorNote == null &&
                symptomState.name.isNotBlank() &&
                symptomState.date != null &&
                symptomState.time != null
    }
}