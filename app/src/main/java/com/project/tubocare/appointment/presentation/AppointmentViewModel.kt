package com.project.tubocare.appointment.presentation

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.alarm.AppointmentReminderReceiver
import com.project.tubocare.alarm.MedicationReminderReceiver
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.appointment.domain.repository.AppointmentRepository
import com.project.tubocare.appointment.presentation.event.AppointmentEvent
import com.project.tubocare.appointment.presentation.state.AppointmentState
import com.project.tubocare.appointment.presentation.state.toAppointment
import com.project.tubocare.core.util.Resource
import com.project.tubocare.medication.presentation.util.getDayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val application: Application
) : ViewModel() {
    private val hasUser = appointmentRepository.hasUser()
    private val userId = appointmentRepository.getUserId()
    private val appointmentId = appointmentRepository.getAppointmentId()

    var appointmentState by mutableStateOf(AppointmentState())
        private set

    init {
        viewModelScope.launch {
            getAppointments()
        }
    }

    fun onEvent(event: AppointmentEvent) {
        when (event) {
            AppointmentEvent.GetAppointments -> {
                getAppointments()
            }

            is AppointmentEvent.AddAppointment -> {
                addAppointment()
            }

            is AppointmentEvent.UpdateAppointment -> {
                updateAppointment()
            }

            AppointmentEvent.ResetStatus -> {
                resetStatus()
            }

            AppointmentEvent.ClearToast -> {
                clearToastMessage()
            }

            is AppointmentEvent.GetAppointmentById -> {
                getAppointmentById(event.value)
            }

            is AppointmentEvent.DeleteAppointment -> {
                deleteAppointment(event.value)
            }

            is AppointmentEvent.OnNameChanged -> {
                onNameChanged(event.value)
            }

            is AppointmentEvent.OnDateChanged -> {
                onDateChanged(event.value)
            }

            is AppointmentEvent.OnTimeChanged -> {
                onTimeChanged(event.value)
            }

            is AppointmentEvent.OnLocationChanged -> {
                onLocationChanged(event.value)
            }

            is AppointmentEvent.OnNoteChanged -> {
                onNoteChanged(event.value)
            }

            is AppointmentEvent.OnCheckChanged -> {
                onCheckChanged(event.id, event.value)
            }
        }
    }

    private fun getAppointments() = viewModelScope.launch {
        if (hasUser) {
            appointmentRepository.getAppointments(userId).collect {
                when (it) {
                    is Resource.Success -> {
                        appointmentState = appointmentState.copy(appointmentList = it, isSuccessGetAppointments = true)
                    }

                    is Resource.Error -> {
                        appointmentState = appointmentState.copy(errorGetAppointments = it.message)
                    }

                    else -> { /* no-op */
                    }
                }
            }
        }
    }

    private fun getAppointmentById(id: String) = viewModelScope.launch {
        appointmentRepository.getAppointmentById(id).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { setAppointmentState(id, it) }
                }

                is Resource.Error -> {
                    appointmentState = appointmentState.copy(errorGetDetail = result.message)
                }

                else -> { /* no-op */ }
            }
        }
    }

    private fun addAppointment() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            appointmentState = appointmentState.copy(errorAdd = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                appointmentState = appointmentState.copy(isLoading = true)

                val appointment = Appointment(
                    appointmentId = appointmentId,
                    userId = userId,
                    name = appointmentState.name,
                    date = appointmentState.date,
                    time = appointmentState.time,
                    location = appointmentState.location,
                    note = appointmentState.note,
                    done = appointmentState.isChecked
                )

                val result = appointmentRepository.addAppointment(appointment)

                when (result) {
                    is Resource.Success -> {
                        appointmentState = appointmentState.copy(isSuccessAdd = true)
                        scheduleAppointmentReminders(appointment)
                    }

                    is Resource.Error -> {
                        appointmentState = appointmentState.copy(errorAdd = result.message)
                    }

                    else -> { /* no-op */ }
                }
            } else {
                throw IllegalArgumentException("Lengkapi formulir terlebih dahulu")
            }
        } catch (e: Exception) {
            appointmentState = appointmentState.copy(errorAdd = e.localizedMessage)
        } finally {
            appointmentState = appointmentState.copy(isLoading = false)
        }
    }

    private fun updateAppointment() = viewModelScope.launch {
        if (!isInternetAvailable()) {
            appointmentState = appointmentState.copy(errorUpdate = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            if (validateForm()) {
                appointmentState = appointmentState.copy(isLoading = true)

                val appointment = appointmentState.toAppointment()

                val result = appointmentRepository.updateAppointment(appointment)

                when (result) {
                    is Resource.Success -> {
                        appointmentState = appointmentState.copy(isSuccessUpdate = true)
                        scheduleAppointmentReminders(appointment)
                    }

                    is Resource.Error -> {
                        appointmentState = appointmentState.copy(errorUpdate = result.message)
                    }

                    else -> { /* no-op */ }
                }
            } else {
                throw IllegalArgumentException("Lengkapi form terlebih dahulu")
            }
        } catch (e: Exception) {
            appointmentState = appointmentState.copy(errorUpdate = e.localizedMessage)
        } finally {
            appointmentState = appointmentState.copy(isLoading = false)
        }
    }

    private fun deleteAppointment(id: String) = viewModelScope.launch {
        if (!isInternetAvailable()) {
            appointmentState = appointmentState.copy(errorDelete = "Tidak ada koneksi internet. Mohon periksa sambungan internet Anda.")
            return@launch
        }
        try {
            appointmentState = appointmentState.copy(isLoadingDelete = true)
            val result = appointmentRepository.deleteAppointment(id)
            when (result) {
                is Resource.Success -> {
                    appointmentState = appointmentState.copy(isSuccessDelete = true)
                    cancelAppointmentReminder(id, application)
                }

                is Resource.Error -> {
                    appointmentState = appointmentState.copy(errorDelete = result.message)
                }

                else -> { /* no-op */ }
            }
        } catch (e: Exception) {
            appointmentState = appointmentState.copy(errorDelete = e.localizedMessage)
        } finally {
            appointmentState = appointmentState.copy(isLoadingDelete = false)
        }
    }

    private fun scheduleAppointmentReminders(appointment: Appointment) {
        Log.d("AppointmentVM", "scheduleAppointmentReminders loading...")

        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Set Reminder at 6 PM the day before appointment
        val calenderEveningBefore = Calendar.getInstance().apply {
            time = appointment.date!!
            add(Calendar.DAY_OF_YEAR, -1)
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 20)
            set(Calendar.SECOND, 0)
        }

        val intentEveningBefore = Intent(application, AppointmentReminderReceiver::class.java).apply {
            putExtra("userId", appointment.userId)
            putExtra("appointmentName", appointment.name)
            putExtra("appointmentId", appointment.appointmentId)
            putExtra("appointmentTime", appointment.time.toString())
            putExtra("appointmentLocation", appointment.location)
            putExtra("reminderType", "H-1")
        }

        val pendingIntentEveningBefore = PendingIntent.getBroadcast(
            application,
            appointment.appointmentId.hashCode() + 1,
            intentEveningBefore,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calenderEveningBefore.timeInMillis,
            pendingIntentEveningBefore
        )

        // Reminder at 6 AM on the day of the appointment
        val calendarMorningOf = Calendar.getInstance().apply {
            time = appointment.date!!
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val intentMorningOf = Intent(application, AppointmentReminderReceiver::class.java).apply {
            putExtra("userId", appointment.userId)
            putExtra("appointmentName", appointment.name)
            putExtra("appointmentId", appointment.appointmentId)
            putExtra("appointmentTime", appointment.time.toString())
            putExtra("appointmentLocation", appointment.location)
            putExtra("reminderType", "H-H")
        }

        val pendingIntentMorningOf = PendingIntent.getBroadcast(
            application,
            (appointment.appointmentId.hashCode() + 2),
            intentMorningOf,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendarMorningOf.timeInMillis,
            pendingIntentMorningOf
        )
        Log.d("Appointment", "Alarm set for ${appointment.name} at ${appointment.time} on ${appointment.date}")
    }

    private fun cancelAppointmentReminder(appointmentId: String, context: Context){
        Log.d("AppointmentVM", "cancelAppointmentReminder loading...")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel Reminder at 6 PM the day before appointment
        val intentEveningBefore = Intent(context, AppointmentReminderReceiver::class.java)
        val pendingIntentEveningBefore = PendingIntent.getBroadcast(
            context,
            appointmentId.hashCode() + 1,
            intentEveningBefore,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntentEveningBefore)

        // Cancel Reminder at 6 AM on the day of the appointment
        val intentMorningOf = Intent(context, AppointmentReminderReceiver::class.java)
        val pendingIntentMorningOf = PendingIntent.getBroadcast(
            context,
            appointmentId.hashCode() + 2,
            intentMorningOf,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntentMorningOf)
        Log.d("AppointmentVM", "cancelAppointmentReminder id $appointmentId success")
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    private fun setAppointmentState(id: String, appointment: Appointment){
        appointmentState = appointmentState.copy(
            appointmentId = id,
            userId = appointment.userId,
            name = appointment.name,
            date = appointment.date,
            time = appointment.time,
            location = appointment.location,
            note = appointment.note,
            isChecked = appointment.done,
        )
    }

    private fun resetStatus() {
        appointmentState = appointmentState.copy(
            isSuccessGetAppointments = false,
            isSuccessGetDetail = false,
            isSuccessAdd = false,
            isSuccessUpdate = false,
            isSuccessDelete = false
        )
    }

    private fun clearToastMessage() {
        appointmentState = appointmentState.copy(
            errorGetAppointments = null,
            errorGetDetail = null,
            errorAdd = null,
            errorUpdate = null,
            errorDelete = null
        )
    }

    private fun onNameChanged(name: String) {
        appointmentState = appointmentState.copy(name = name)
        if (name.isBlank()) {
            appointmentState = appointmentState.copy(errorName = "Field tidak boleh kosong")
        } else {
            appointmentState = appointmentState.copy(errorName = null)
        }
    }

    private fun onDateChanged(date: Date?) {
        appointmentState = appointmentState.copy(date = date)
        if (date == null) {
            appointmentState = appointmentState.copy(errorDate = "Field tidak boleh kosong")
        } else {
            appointmentState = appointmentState.copy(errorDate = null)
        }
    }

    private fun onTimeChanged(time: LocalTime?) {
        appointmentState = appointmentState.copy(time = time)
        if (time == null) {
            appointmentState = appointmentState.copy(errorTime = "Field tidak boleh kosong")
        } else {
            appointmentState = appointmentState.copy(errorTime = null)
        }
    }

    private fun onLocationChanged(location: String) {
        appointmentState = appointmentState.copy(location = location)
        if (location.isBlank()) {
            appointmentState = appointmentState.copy(errorLocation = "Field tidak boleh kosong")
        } else {
            appointmentState = appointmentState.copy(errorLocation = null)
        }
    }

    private fun onNoteChanged(note: String) {
        appointmentState = appointmentState.copy(note = note)
    }

    private fun onCheckChanged(id: String, check: Boolean) {
        viewModelScope.launch {
            appointmentRepository.updateAppointmentCheckStatus(id, check)
        }
    }

    private fun validateForm(): Boolean {
        return appointmentState.errorName == null &&
                appointmentState.errorDate == null &&
                appointmentState.errorTime == null &&
                appointmentState.errorLocation == null &&
                appointmentState.name.isNotBlank() &&
                appointmentState.location.isNotBlank() &&
                appointmentState.date != null &&
                appointmentState.time != null
    }
}