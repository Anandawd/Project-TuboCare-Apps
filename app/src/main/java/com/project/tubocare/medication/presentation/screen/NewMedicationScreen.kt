package com.project.tubocare.medication.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.core.presentation.components.CustomAutocompleteTextField
import com.project.tubocare.core.presentation.components.CustomDropdown
import com.project.tubocare.core.presentation.components.CustomOutlineTimePicker
import com.project.tubocare.core.presentation.components.CustomOutlinedNumberField
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.presentation.components.getTimePickerLabel
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.presentation.component.CustomCheckBoxDays
import com.project.tubocare.medication.presentation.component.CustomDropdownDosage
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.medication.presentation.util.medicationNames
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMedicationScreen(
    state: MedicationState,
    onEvent: (MedicationEvent) -> Unit,
    navigateToHome: () -> Unit,
) {
    val isErrorName = state.errorName != null
    val isErrorInstruction = state.errorInstruction != null
    val isErrorFrequency = state.errorFrequency != null
    val isErrorRemain = state.errorRemain != null
    val isErrorNote = state.errorNote != null
    val isErrorTime = state.errorTimes != null
    val isErrorDosage = state.errorDosage != null

    val isAddError = state.errorAdd != null

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.title_new_appointment),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToHome() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingvalues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingvalues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                CustomAutocompleteTextField(
                    suggestions = medicationNames,
                    onValueChange = { onEvent(MedicationEvent.OnNameChanged(it)) },
                    label = stringResource(id = R.string.medication_label_name),
                    placeholder = stringResource(id = R.string.medication_placeholder_name),
                    isError = isErrorName,
                    errorText = if (isErrorName) state.errorName else null
                )
                CustomDropdown(
                    listItem = stringArrayResource(id = R.array.list_instruction),
                    selectedItem = state.instruction,
                    onItemSelected = { onEvent(MedicationEvent.OnInstructionChanged(it)) },
                    label = stringResource(id = R.string.medication_label_instruction),
                    placeholder = stringResource(id = R.string.medication_placeholder_instruction),
                    isError = isErrorInstruction,
                    errorText = if (isErrorInstruction) state.errorInstruction else null
                )
                CustomDropdown(
                    listItem = stringArrayResource(id = R.array.list_frequency),
                    selectedItem = state.frequency,
                    onItemSelected = { onEvent(MedicationEvent.OnFrequencyChanged(it)) },
                    label = stringResource(id = R.string.medication_label_frequency),
                    placeholder = stringResource(id = R.string.medication_placeholder_frequency),
                    isError = isErrorFrequency,
                    errorText = if (isErrorFrequency) state.errorInstruction else null
                )
                CustomOutlinedNumberField(
                    value = state.remain?.toString() ?: "",
                    onValueChange = {
                        onEvent(MedicationEvent.OnRemainChanged(it ?: 0))
                    },
                    label = stringResource(id = R.string.medication_label_remain),
                    placeholder = stringResource(id = R.string.medication_placeholder_remain),
                    isError = isErrorRemain,
                    errorText = if (isErrorRemain) state.errorRemain else null
                )
                CustomOutlinedTextField(
                    value = state.note,
                    onValueChange = { onEvent(MedicationEvent.OnNoteChanged(it)) },
                    label = stringResource(id = R.string.appointment_label_note),
                    placeholder = stringResource(id = R.string.appointment_placeholder_note),
                    isError = isErrorNote,
                    errorText = if (isErrorNote) state.errorNote else null
                )
                CustomCheckBoxDays(
                    selectedDays = state.weeklySchedule.keys.toList(),
                    onDaySelected = { days ->
                        onEvent(
                            MedicationEvent.OnScheduleChanged(
                            days.associateWith { state.weeklySchedule[it] ?: emptyList() }
                        ))
                    }
                )
                CustomDropdownDosage(
                    selectedItem = state.dosage,
                    onItemSelected = {
                        onEvent(MedicationEvent.OnDosageChanged(it))
                        onEvent(MedicationEvent.OnTempTimesChanged(List(it) { null }))
                    },
                    isError = isErrorDosage,
                    errorText = if (isErrorDosage) state.errorDosage else null
                )
                state.tempTimes.forEachIndexed { index, time ->
                    CustomOutlineTimePicker(
                        selectedItem = time,
                        onTimeSelected = { selectedTime ->
                            val updatedTimes = state.tempTimes.toMutableList().apply { this[index] = selectedTime }
                            onEvent(MedicationEvent.OnTempTimesChanged(updatedTimes))
                        },
                        label = getTimePickerLabel(index),
                        placeholder = stringResource(id = R.string.medication_placeholder_time),
                        isError = isErrorTime,
                        errorText = if (isErrorTime) state.errorTimes else null
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(40.dp))
                CustomPrimaryButton(
                    value = stringResource(id = R.string.btn_done),
                    onClick = {
                        val updatedSchedule = state.weeklySchedule.toMutableMap()
                        state.tempTimes.forEach { time ->
                            state.weeklySchedule.keys.forEach { day ->
                                val timesForDay = updatedSchedule[day]?.toMutableList() ?: mutableListOf()
                                if (time != null) {
                                    timesForDay.add(ChecklistEntry(time = time))
                                }
                                updatedSchedule[day] = timesForDay
                            }
                        }
                        onEvent(MedicationEvent.OnScheduleChanged(updatedSchedule))
                        onEvent(MedicationEvent.AddMedication)
                    },
                    isLoading = state.isLoading
                )
            }
        }
    }

    LaunchedEffect(key1 = isAddError) {
        if (isAddError) {
            val errorMessage = state.errorAdd ?: "Terjadi kesalahan pada saat menambahkan jadwal"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(MedicationEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = state.isSuccessAdd) {
        if (state.isSuccessAdd) {
            Toast.makeText(context, "Jadwal minum obat berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            delay(500)
            navigateToHome()
        }
    }

}
