package com.project.tubocare.healthcare.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.appointment.domain.repository.AppointmentRepository
import com.project.tubocare.appointment.presentation.state.AppointmentState
import com.project.tubocare.core.util.Resource
import com.project.tubocare.healthcare.domain.repository.HealthcareRepository
import com.project.tubocare.healthcare.presentation.event.HealthcareEvent
import com.project.tubocare.healthcare.presentation.state.HealthcareState
import com.project.tubocare.medication.domain.repository.MedicationRepository
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.profile.domain.repository.ProfileRepository
import com.project.tubocare.profile.presentation.state.ProfileState
import com.project.tubocare.symptom.domain.repository.SymptomRepository
import com.project.tubocare.symptom.presentation.state.SymptomState
import com.project.tubocare.weight.domain.repository.WeightRepository
import com.project.tubocare.weight.presentation.state.WeightState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthcareViewModel @Inject constructor(
    private val healthcareRepository: HealthcareRepository,
    private val profileRepository: ProfileRepository,
    private val medicationRepository: MedicationRepository,
    private val appointmentRepository: AppointmentRepository,
    private val symptomRepository: SymptomRepository,
    private val weightRepository: WeightRepository
) : ViewModel() {

    val healthcareId = healthcareRepository.getHealthcareId()

    private val _healthcareState = MutableStateFlow(HealthcareState())
    val healthcareState: StateFlow<HealthcareState> = _healthcareState

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    var medicationState by mutableStateOf(MedicationState())
        private set

    var appointmentState by mutableStateOf(AppointmentState())
        private set

    var symptomState by mutableStateOf(SymptomState())
        private set

    var weightState by mutableStateOf(WeightState())
        private set

    init {
        getHealthcare()
    }

    fun onEvent(event: HealthcareEvent) {
        when (event) {
            HealthcareEvent.getHealthcare -> {
                getHealthcare()
            }

            is HealthcareEvent.GetPatientsByPuskemas -> {
                getPatientsByPuskesmas(event.value)
            }

            HealthcareEvent.ClearToast -> {
                clearToastMessage()
            }

            is HealthcareEvent.GetPatientById -> {
                getPatientById(event.value)
            }

            is HealthcareEvent.GetMedications -> {
                getMedications(event.value)
            }

            is HealthcareEvent.GetMedicationById -> {
                getMedicationById(event.value)
            }

            is HealthcareEvent.GetAppointments -> {
                getAppointments(event.value)
            }

            is HealthcareEvent.GetSymptoms -> {
                getSymptoms(event.value)
            }


            is HealthcareEvent.GetWeights -> {
                getWeights(event.value)
            }

        }
    }

    private fun getPatientsByPuskesmas(puskesmas: String) = viewModelScope.launch {
        try {
            healthcareRepository.getPatients(role = "Pasien", unit = puskesmas).collect {
                when (it) {
                    is Resource.Success -> {
                        _healthcareState.update { state ->
                            state.copy(patients = it.data ?: emptyList())
                        }
                    }

                    is Resource.Error -> {
                        _healthcareState.update { state ->
                            state.copy(errorGetPatients = it.message)
                        }
                    }

                    else -> { /* no-op */
                    }
                }
            }
        } catch (e: Exception) {
            _healthcareState.update { state ->
                state.copy(errorGetPatients = e.localizedMessage)
            }
        }
    }

    private fun getHealthcare() = viewModelScope.launch(Dispatchers.IO) {
        if (healthcareId.isNotEmpty()) {
            profileRepository.getUser(healthcareId)
                .catch { e ->
                    _healthcareState.update { state ->
                        state.copy(errorGetHealthcare = e.localizedMessage)
                    }
                }
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { user ->
                                _healthcareState.update { state ->
                                    state.copy(
                                        name = user.name,
                                        puskesmas = user.puskesmas,
                                    )
                                }
                            }
                        }

                        is Resource.Error -> {
                            _healthcareState.update { state ->
                                state.copy(errorGetHealthcare = result.message)
                            }
                        }

                        else -> { /* no-op */
                        }
                    }
                }
        }
    }

    private fun getPatientById(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        profileRepository.getUser(userId).collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { user ->
                        _profileState.update { state ->
                            state.copy(
                                userId = user.userId,
                                email = user.email,
                                name = user.name,
                                bdate = user.bdate,
                                gender = user.gender,
                                address = user.address,
                                phone = user.phone,
                                puskesmas = user.puskesmas,
                                role = user.role,
                                imageUrl = user.imageUrl
                            )
                        }
                        /* profileState = profileState.copy(
                             userId = user.userId,
                             email = user.email,
                             name = user.name,
                             bdate = user.bdate,
                             gender = user.gender,
                             address = user.address,
                             phone = user.phone,
                             puskesmas = user.puskesmas,
                             role = user.role,
                             imageUrl = user.imageUrl
                         )*/
                    }
                }

                is Resource.Error -> {
                    _healthcareState.update { state ->
                        state.copy(errorGetDetailPatient = it.message)
                    }
                }

                else -> { /* no-op */
                }
            }
        }
    }

    private fun getMedications(userId: String) = viewModelScope.launch {
        medicationRepository.getMedications(userId).collect {
            when (it) {
                is Resource.Success -> {
                    medicationState = medicationState.copy(
                        medicationList = it,
                        selectedMedication = it.data?.firstOrNull()
                    )
                }

                is Resource.Error -> {
                    Log.e("MedicationViewModel", "Error: ${it.message}")
                }

                else -> { /* no-op */
                }
            }
        }
    }

    private fun getMedicationById(medicationId: String) = viewModelScope.launch {
        medicationRepository.getMedicationById(medicationId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { medication ->
                        medicationState = medicationState.copy(
                            userId = medication.userId,
                            medicationId = medicationId,
                            name = medication.name,
                            weeklySchedule = medication.weeklySchedule,
                            frequency = medication.frequency,
                            instruction = medication.instruction,
                            remain = medication.remain,
                            dosage = medication.dosage,
                            note = medication.note,
                            image = medication.image,
                            selectedMedication = medication
                        )
                    }
                }

                is Resource.Error -> {
                    medicationState = medicationState.copy(
                        isSuccessGetDetail = false,
                        errorGetDetail = result.message
                    )
                }

                else -> { /* no-op */
                }
            }
        }
    }


    private fun getAppointments(userId: String) = viewModelScope.launch {
        appointmentRepository.getAppointments(userId).collect {
            when (it) {
                is Resource.Success -> {
                    appointmentState = appointmentState.copy(appointmentList = it)
                }

                is Resource.Error -> {
                    appointmentState = appointmentState.copy(errorGetAppointments = it.message)
                }

                else -> { /* no-op */
                }
            }
        }

    }

    private fun getWeights(userId: String) = viewModelScope.launch {
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

    private fun getSymptoms(userId: String) = viewModelScope.launch {
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

    private fun clearToastMessage() {
        _healthcareState.update { state ->
            state.copy(
                errorGetHealthcare = null,
                errorGetPatients = null,
                errorGetDetailPatient = null
            )
        }
    }
}