package com.project.tubocare.medication.presentation

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.alarm.MedicationReminderReceiver
import com.project.tubocare.core.util.Resource
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.domain.repository.MedicationRepository
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.medication.presentation.util.getDayOfWeek
import com.project.tubocare.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository,
    private val profileRepository: ProfileRepository,
    private val application: Application
) : ViewModel() {

    private val hasUser = medicationRepository.hasUser()

    private val userId = medicationRepository.getUserId()

    private val medicationId = medicationRepository.getMedicationId()

    var medicationState by mutableStateOf(MedicationState())
        private set

    init {
        viewModelScope.launch {
            getMedications()
        }
    }

    fun onEvent(event: MedicationEvent) {
        when (event) {

            MedicationEvent.GetMedications -> {
                getMedications()
            }

            MedicationEvent.GetUserName -> {
                getUserName()
            }

            MedicationEvent.AddMedication -> {
                addMedication()
            }

            MedicationEvent.ResetStatus -> {
                resetStatus()
            }

            MedicationEvent.ClearToast -> {
                clearToastMessage()
            }

            MedicationEvent.UpdateMedication -> {
                updateMedication()
            }

            is MedicationEvent.DeleteMedication -> {
                deleteMedication(event.value)
            }

            is MedicationEvent.GetMedicationById -> {
                getMedicationById(event.value)
            }

            is MedicationEvent.OnNameChanged -> {
                onNameChanged(event.value)
            }

            is MedicationEvent.OnScheduleChanged -> {
                onScheduleChanged(event.value)
            }

            is MedicationEvent.OnFrequencyChanged -> {
                onFrequencyChanged(event.value)
            }

            is MedicationEvent.OnRemainChanged -> {
                onRemainChanged(event.value)
            }

            is MedicationEvent.OnInstructionChanged -> {
                onInstructionChanged(event.value)
            }

            is MedicationEvent.OnDosageChanged -> {
                onDosageChanged(event.value)
            }

            is MedicationEvent.OnNoteChanged -> {
                onNoteChanged(event.value)
            }

            is MedicationEvent.OnImageChanged -> {
                onImageChanged(event.value)
            }

            is MedicationEvent.SelectMedication -> {
                onSelectedMedication(event.data)
            }

            is MedicationEvent.OnTempTimesChanged -> {
                onTempTimesChanged(event.value)
            }

            is MedicationEvent.UpdateChecklistEntry -> {
                updateChecklistEntry(event.medicationId, event.day, event.updatedChecklist)
            }

            is MedicationEvent.ScheduleAlarms -> {
                scheduleMedicationReminders(event.value)
            }

            is MedicationEvent.UploadImage -> {
                uploadImage(event.value)
            }
        }
    }

    private fun getMedications() = viewModelScope.launch {
        if (hasUser) {
            Log.d("MedicationViewModel", "loading data...")
            medicationRepository.getMedications(userId).collect {
                when (it) {
                    is Resource.Success -> {
                        medicationState = medicationState.copy(
                            medicationList = it,
                            selectedMedication = it.data?.firstOrNull()
                        )
                    }

                    is Resource.Error -> {
                        medicationState = medicationState.copy(
                            errorGetMedications = it.message
                        )
                    }

                    else -> { /* no-op */
                    }
                }
            }
        }
    }

    private fun addMedication() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            medicationState = medicationState.copy(errorAdd = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                medicationState = medicationState.copy(isLoading = true)

                val medication = Medication(
                    userId = userId,
                    medicationId = medicationId,
                    name = medicationState.name,
                    weeklySchedule = medicationState.weeklySchedule,
                    frequency = medicationState.frequency,
                    instruction = medicationState.instruction,
                    remain = medicationState.remain,
                    dosage = medicationState.dosage,
                    note = medicationState.note,
                    image = medicationState.image,
                )

                val result = medicationRepository.addMedication(medication)

                when (result) {
                    is Resource.Success -> {
                        scheduleMedicationReminders(medication)
                        medicationState = medicationState.copy(isSuccessAdd = true)
                    }

                    is Resource.Error -> {
                        medicationState = medicationState.copy(errorAdd = result.message)
                    }

                    else -> { /* no-op */
                    }
                }
            } else {
                throw IllegalArgumentException("Lengkapi formulir terlebih dahulu")
            }
        } catch (e: Exception) {
            medicationState = medicationState.copy(errorAdd = e.localizedMessage)
        } finally {
            medicationState = medicationState.copy(isLoading = false)
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

    private fun updateMedication() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            medicationState = medicationState.copy(errorUpdate = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                medicationState = medicationState.copy(isLoading = true)

                val medication = Medication(
                    userId = medicationState.userId,
                    medicationId = medicationState.medicationId,
                    name = medicationState.name,
                    weeklySchedule = medicationState.weeklySchedule,
                    frequency = medicationState.frequency,
                    instruction = medicationState.instruction,
                    remain = medicationState.remain,
                    dosage = medicationState.dosage,
                    note = medicationState.note,
                    image = medicationState.image
                )

                val result = medicationRepository.updateMedication(medication)

                when (result) {
                    is Resource.Success -> {
                        scheduleMedicationReminders(medication)
                        medicationState = medicationState.copy(isSuccessUpdate = true)
                    }

                    is Resource.Error -> {
                        medicationState = medicationState.copy(errorUpdate = result.message)
                    }

                    else -> { /* no-op */
                    }
                }
            } else {
                throw IllegalArgumentException("Lengkapi form terlebih dahulu")
            }
        } catch (e: Exception) {
            medicationState = medicationState.copy(errorUpdate = e.localizedMessage)
        } finally {
            medicationState = medicationState.copy(isLoading = false)
        }
    }

    private fun deleteMedication(medication: Medication?) = viewModelScope.launch {
        if (!isInternetAvailable()) {
            medicationState = medicationState.copy(errorDelete = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            medicationState = medicationState.copy(isLoadingDelete = true)
            val result = medication?.medicationId?.let { medicationRepository.deleteMedication(it) }
            when (result) {
                is Resource.Success -> {
                    cancelMedicationReminders(medication)
                    medicationState = medicationState.copy(isSuccessDelete = true)
                }

                is Resource.Error -> {
                    medicationState = medicationState.copy(errorDelete = result.message)
                }

                else -> { /* no-op */
                }
            }
        } catch (e: Exception) {
            medicationState = medicationState.copy(errorDelete = e.localizedMessage)
        } finally {
            medicationState = medicationState.copy(isLoadingDelete = false)
        }
    }

    private fun uploadImage(imageUrl: String) = viewModelScope.launch {
        if (!isInternetAvailable()) {
            medicationState = medicationState.copy(errorUploadImage = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            medicationState = medicationState.copy(isLoading = true)
            val result = medicationRepository.uploadImage(Uri.parse(imageUrl))
            when (result) {
                is Resource.Success -> {
                    medicationState = medicationState.copy(
                        image = result.data!!,
                        isSuccessUploadImage = true
                    )
                    updateMedication()
                }

                is Resource.Error -> {
                    medicationState = medicationState.copy(errorUploadImage = result.message)
                }

                else -> {/* no-op */
                }
            }
        } catch (e: Exception) {
            medicationState = medicationState.copy(errorUploadImage = e.localizedMessage)
        } finally {
            medicationState = medicationState.copy(isLoading = false)
        }
    }

    private fun updateChecklistEntry(
        medicationId: String,
        day: String,
        updatedChecklist: List<ChecklistEntry>
    ) = viewModelScope.launch {
        try {
            val selectedMedication =
                medicationState.medicationList.data?.find { it.medicationId == medicationId }

            if (selectedMedication != null) {
                val currentSchedule = selectedMedication.weeklySchedule.toMutableMap()
                currentSchedule[day] = updatedChecklist

                val updatedMedication = selectedMedication.copy(weeklySchedule = currentSchedule)

                val result =
                    medicationRepository.updateChecklistEntry(medicationId, day, updatedChecklist)
                when (result) {
                    is Resource.Success -> {
                        val updatedMedicationList =
                            medicationState.medicationList.data?.map { medication ->
                                if (medication.medicationId == medicationId) {
                                    updatedMedication
                                } else {
                                    medication
                                }
                            } ?: listOf()

                        medicationState = medicationState.copy(
                            selectedMedication = updatedMedication,
                            medicationList = Resource.Success(updatedMedicationList)
                        )
                    }

                    is Resource.Error -> {
                        Log.e(
                            "updateCheckListEntry()",
                            "Error updating checklist: ${result.message}"
                        )
                    }

                    else -> { /* no-op */
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("updateCheckListEntry()", "Exception: $e")
        }
    }

    private fun getUserName() = viewModelScope.launch {
        if (hasUser) {
            profileRepository.getUser(userId).collect {
                when (it) {
                    is Resource.Success -> {
                        it.data?.let { user ->
                            medicationState = medicationState.copy(userName = user.name)
                        }
                    }

                    is Resource.Error -> {
                        throw IllegalArgumentException(it.message)
                    }

                    is Resource.Loading -> {
                        medicationState = medicationState.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun resetChecklistEntries() = viewModelScope.launch {
        try {
            val updateMedications = medicationState.medicationList.data?.map { medication ->
                val updateWeeklySchedule = medication.weeklySchedule.mapValues { entry ->
                    entry.value.map { checklist ->
                        if (checklist.timestamp != null && checklist.timestamp.isBefore(
                                LocalDateTime.now().minusDays(7)
                            )
                        ) {
                            checklist.copy(checked = false, timestamp = null)
                        } else {
                            checklist
                        }
                    }
                }
                medication.copy(weeklySchedule = updateWeeklySchedule)
            } ?: listOf()
            updateMedications.forEach { medication ->
                medicationRepository.updateMedication(medication)
            }
        } catch (e: Exception) {
            Log.e("resetChecklistEntries()", "Exception: $e")
        }
    }

    private fun scheduleMedicationReminders(medication: Medication) {
        Log.d("MedicationVM", "scheduleMedicationReminders loading...")
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d("MedicationVM", "Processing medication: ${medication.name}")
        medication.weeklySchedule.forEach { (day, entries) ->
            entries.forEachIndexed { index, entry ->
                entry.time?.let { time ->
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_WEEK, getDayOfWeek(day))
                        set(Calendar.HOUR_OF_DAY, time.hour)
                        set(Calendar.MINUTE, time.minute)
                        set(Calendar.SECOND, 0)
                    }

                    if (calendar.timeInMillis < System.currentTimeMillis()) {
                        calendar.add(Calendar.WEEK_OF_YEAR, 1)
                    }

                    val calendarBefore = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_WEEK, getDayOfWeek(day))
                        set(Calendar.HOUR_OF_DAY, time.hour)
                        set(Calendar.MINUTE, time.minute)
                        add(Calendar.MINUTE, -5)
                        set(Calendar.SECOND, 0)
                    }

                    if (calendarBefore.timeInMillis < System.currentTimeMillis()) {
                        calendarBefore.add(Calendar.WEEK_OF_YEAR, 1)
                    }

                    val intent = Intent(application, MedicationReminderReceiver::class.java).apply {
                        putExtra("userId", medication.userId)
                        putExtra("medicationId", medication.medicationId)
                        putExtra("medicationName", medication.name)
                        putExtra("index", index)
                        putExtra("day", day)
                        putExtra("time", time.toString())
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        application,
                        (medication.medicationId.hashCode() + day.hashCode() + index),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val pendingIntentBefore = PendingIntent.getBroadcast(
                        application,
                        (medication.medicationId.hashCode() + day.hashCode() + index + 10),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    try {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )

                        // Schedule the 5 minutes before reminder
                        val alarmClockBefore = AlarmManager.AlarmClockInfo(
                            calendarBefore.timeInMillis,
                            pendingIntentBefore
                        )
                        alarmManager.setAlarmClock(
                            alarmClockBefore,
                            pendingIntentBefore
                        )

                        Log.d(
                            "MedicationVM",
                            "Alarm set for ${medication.name} at $time on $day"
                        )
                    } catch (e: Exception) {
                        Log.e(
                            "MedicationVM",
                            "Failed to set alarm for ${medication.name} at $time on $day",
                            e
                        )
                    }
                } ?: Log.e(
                    "MedicationVM",
                    "Time is null for medication ${medication.name} on $day"
                )
            }
        }
    }

    private fun cancelMedicationReminders(medication: Medication) {
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        medication.weeklySchedule.forEach { (day, entries) ->
            entries.forEachIndexed { index, entry ->
                entry.time?.let { time ->
                    val intent = Intent(application, MedicationReminderReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        application,
                        (medication.medicationId.hashCode() + day.hashCode() + index),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    val pendingIntentBefore = PendingIntent.getBroadcast(
                        application,
                        (medication.medicationId.hashCode() + day.hashCode() + index + 10),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.cancel(pendingIntent)
                    alarmManager.cancel(pendingIntentBefore)

                    Log.d("MedicationVM", "Canceled alarm for ${medication.name} at $time on $day")
                }
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun resetStatus() {
        medicationState = medicationState.copy(
            isSuccessGetDetail = false,
            isSuccessUploadImage = false,
            isSuccessUpdate = false,
            isSuccessDelete = false,
            isSuccessAdd = false,
        )
    }

    private fun clearToastMessage() {
        medicationState = medicationState.copy(
            errorGetDetail = null,
            errorUploadImage = null,
            errorUpdate = null,
            errorDelete = null,
            errorAdd = null,
        )
    }

    private fun onNameChanged(name: String) {
        medicationState = medicationState.copy(name = name)
        if (name.isBlank()) {
            medicationState = medicationState.copy(errorName = "Field tidak boleh kosong")
        } else {
            medicationState = medicationState.copy(errorName = null)
        }
    }

    private fun onScheduleChanged(schedule: Map<String, List<ChecklistEntry>>) {
        medicationState = medicationState.copy(weeklySchedule = schedule)
    }

    private fun onTempTimesChanged(times: List<LocalTime?>) {
        medicationState = medicationState.copy(tempTimes = times)
        if (times.isEmpty()) {
            medicationState = medicationState.copy(errorTimes = "Field tidak boleh kosong")
        } else {
            medicationState = medicationState.copy(errorTimes = null)
        }
    }

    private fun onFrequencyChanged(frequency: String) {
        medicationState = medicationState.copy(frequency = frequency)
        if (frequency.isBlank()) {
            medicationState = medicationState.copy(errorFrequency = "Field tidak boleh kosong")
        } else {
            medicationState = medicationState.copy(errorFrequency = null)
        }
    }

    private fun onInstructionChanged(instruction: String) {
        medicationState = medicationState.copy(instruction = instruction)
        if (instruction.isBlank()) {
            medicationState = medicationState.copy(errorInstruction = "Field tidak boleh kosong")
        } else {
            medicationState = medicationState.copy(errorInstruction = null)
        }
    }

    private fun onRemainChanged(remain: Int) {
        medicationState = medicationState.copy(remain = remain)
        if (remain <= 0) {
            medicationState = medicationState.copy(errorRemain = "Field tidak boleh kosong")
        } else {
            medicationState = medicationState.copy(errorRemain = null)
        }
    }

    private fun onDosageChanged(dosage: Int) {
        medicationState = medicationState.copy(dosage = dosage)
        if (dosage <= 0) {
            medicationState = medicationState.copy(errorDosage = "Field tidak boleh kosong")
        } else {
            medicationState = medicationState.copy(errorDosage = null)
        }
    }

    private fun onNoteChanged(note: String) {
        medicationState = medicationState.copy(note = note)
        if (note.isBlank()) {
            medicationState = medicationState.copy(errorNote = "Field tidak boleh kosong")
        } else {
            medicationState = medicationState.copy(errorNote = null)
        }
    }

    private fun onImageChanged(imageUrl: String) {
        medicationState = medicationState.copy(image = imageUrl)
    }

    private fun onSelectedMedication(medication: Medication) {
        medicationState = medicationState.copy(selectedMedication = medication)
    }

    private fun validateForm(): Boolean {
        return medicationState.errorName == null &&
                medicationState.errorFrequency == null &&
                medicationState.errorInstruction == null &&
                medicationState.errorNote == null &&
                medicationState.errorDosage == null &&
                medicationState.errorRemain == null &&
                medicationState.errorSchedule == null &&
                medicationState.name.isNotBlank() &&
                medicationState.frequency.isNotBlank() &&
                medicationState.instruction.isNotBlank() &&
                medicationState.dosage != null &&
                medicationState.remain != null

    }
}
