package com.project.tubocare.weight.presentation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.core.util.Resource
import com.project.tubocare.weight.domain.model.Weight
import com.project.tubocare.weight.domain.repository.WeightRepository
import com.project.tubocare.weight.presentation.event.WeightEvent
import com.project.tubocare.weight.presentation.state.WeightState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val weightRepository: WeightRepository,
    private val application: Application
): ViewModel() {
    private val hasUser = weightRepository.hasUser()
    private val userId = weightRepository.getUserId()
    private val weightId = weightRepository.getWeightId()

    var weightState by mutableStateOf(WeightState())
        private set

    init {
        getWeights()
    }

    fun onEvent(event: WeightEvent) {
        when (event) {
            WeightEvent.GetWeights -> {
                getWeights()
            }

            WeightEvent.AddWeight -> {
                addWeight()
            }

            WeightEvent.UpdateWeight -> {
                updateWeight()
            }

            WeightEvent.ResetStatus -> {
                resetStatus()
            }

            WeightEvent.ClearToast -> {
                clearToastMessage()
            }

            is WeightEvent.GetWeightById -> {
                getWeightById(event.value)
            }

            is WeightEvent.DeleteWeight -> {
                deleteWeight(event.value)
            }

            is WeightEvent.OnDateChanged -> {
                onDateChanged(event.value)
            }

            is WeightEvent.OnTimeChanged -> {
                onTimeChanged(event.value)
            }

            is WeightEvent.OnWeightChanged -> {
                onWeightChanged(event.value)
            }

            is WeightEvent.OnHeightChanged -> {
                onHeightChanged(event.value)
            }
        }
    }

    private fun getWeights() = viewModelScope.launch {
        if (hasUser) {
            weightRepository.getWeights(userId).collect {
                when (it) {
                    is Resource.Success -> {
                        val latestWeight = it.data?.maxByOrNull { weight -> weight.date!! }
                        weightState = weightState.copy(
                            weightList = it,
                            previousWeight = latestWeight?.weight
                        )
                    }

                    is Resource.Error -> {
                        weightState = weightState.copy(errorGetWeights = it.message)
                    }

                    else -> { /* no-op */
                    }
                }
            }
        }
    }

    private fun getWeightById(id: String) = viewModelScope.launch {
        weightRepository.getWeightById(id).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { weight ->
                        weightState = weightState.copy(
                            weightId = id,
                            userId = weight.userId,
                            date = weight.date,
                            time = weight.time,
                            weight = weight.weight,
                            height = weight.height,
                            note = weight.note,
                        )
                    }
                }

                is Resource.Error -> {
                    weightState = weightState.copy(
                        errorGetDetail = result.message
                    )
                }

                else -> { /* no-op */
                }
            }
        }
    }

    private fun addWeight() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            weightState = weightState.copy(errorAdd = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                weightState = weightState.copy(isLoading = true)

                val weight = Weight(
                    weightId = weightId,
                    userId = userId,
                    date = weightState.date,
                    time = weightState.time,
                    weight = weightState.weight,
                    height = weightState.height,
                    note = generateNote(),
                )

                val result = weightRepository.addWeight(weight)

                when (result) {
                    is Resource.Success -> {
                        weightState = weightState.copy(isSuccessAdd = true)
                    }

                    is Resource.Error -> {
                        weightState = weightState.copy(errorAdd = result.message)
                    }

                    else -> { /* no-op */
                    }
                }
            } else {
                throw IllegalArgumentException("Lengkapi formulir terlebih dahulu")
            }
        } catch (e: Exception) {
            weightState = weightState.copy(errorAdd = e.localizedMessage)
        } finally {
            weightState = weightState.copy(isLoading = false)
        }
    }

    private fun updateWeight() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            weightState = weightState.copy(errorUpdate = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                weightState = weightState.copy(isLoading = true)

                val weight = Weight(
                    weightId = weightState.weightId,
                    userId = weightState.userId,
                    date = weightState.date,
                    time = weightState.time,
                    weight = weightState.weight,
                    height = weightState.height,
                    note = generateNote(),
                )

                val result = weightRepository.updateWeight(weight)

                when (result) {
                    is Resource.Success -> {
                        weightState = weightState.copy(isSuccessUpdate = true)
                    }

                    is Resource.Error -> {
                        weightState = weightState.copy(errorUpdate = result.message)
                    }

                    else -> { /* no-op */
                    }
                }
            } else {
                throw IllegalArgumentException("Lengkapi formulir terlebih dahulu")
            }
        } catch (e: Exception) {
            weightState = weightState.copy(errorUpdate = e.localizedMessage)
        } finally {
            weightState = weightState.copy(isLoading = false)
        }
    }

    private fun deleteWeight(id: String) = viewModelScope.launch {
        if (!isInternetAvailable()) {
            weightState = weightState.copy(errorDelete = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            weightState = weightState.copy(isLoadingDelete = true)
            val result = weightRepository.deleteWeight(id)
            when (result) {
                is Resource.Success -> {
                    weightState = weightState.copy(isSuccessDelete = true)
                }

                is Resource.Error -> {
                    weightState = weightState.copy(errorDelete = result.message)
                }

                else -> { /* no-op */ }
            }
        } catch (e: Exception) {
            weightState = weightState.copy(errorDelete = e.localizedMessage)
        } finally {
            weightState = weightState.copy(isLoadingDelete = false)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun resetStatus() {
        weightState = weightState.copy(
            isSuccessAdd = false,
            isSuccessUpdate = false,
            isSuccessDelete = false
        )
    }

    private fun clearToastMessage() {
        weightState = weightState.copy(
            errorGetWeights = null,
            errorGetDetail = null,
            errorAdd = null,
            errorUpdate = null,
            errorDelete = null
        )
    }

    private fun onDateChanged(date: Date?) {
        weightState = weightState.copy(date = date)
        if (date == null) {
            weightState = weightState.copy(errorDate = "Field tidak boleh kosong")
        } else {
            weightState = weightState.copy(errorDate = null)
        }
    }

    private fun onTimeChanged(time: LocalTime?) {
        weightState = weightState.copy(time = time)
        if (time == null) {
            weightState = weightState.copy(errorTime = "Field tidak boleh kosong")
        } else {
            weightState = weightState.copy(errorTime = null)
        }
    }

    private fun onWeightChanged(weight: String) {
        val weightValue = weight.toIntOrNull()
        weightState = if (weightValue != null) {
            weightState.copy(weight = weightValue, errorWeight = null)
        } else {
            weightState.copy(weight = null, errorWeight = "Field tidak boleh kosong")
        }
    }

    private fun onHeightChanged(height: String) {
        val heightValue = height.toIntOrNull()
        weightState = if (heightValue != null) {
            weightState.copy(height = heightValue, errorHeight = null)
        } else {
            weightState.copy(height = null, errorHeight = "Field tidak boleh kosong")
        }
    }

    private fun generateNote(): String {
        val previousWeight = weightState.previousWeight ?: return "Berat badan pertama"
        val currentWeight = weightState.weight ?: return "Berat badan belum tercatat"

        return when {
            currentWeight > previousWeight -> "Berat badan naik"
            currentWeight < previousWeight -> "Berat badan turun"
            else -> "Berat badan tetap"
        }
    }

    private fun validateForm(): Boolean {
        return weightState.errorDate == null &&
                weightState.errorTime == null &&
                weightState.errorWeight == null &&
                weightState.errorHeight == null &&
                weightState.errorNote == null &&
                weightState.date != null &&
                weightState.time != null &&
                weightState.weight != null &&
                weightState.height != null
    }
}