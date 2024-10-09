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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.core.presentation.components.CustomDropdown
import com.project.tubocare.core.presentation.components.CustomOutlineTimePicker
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.presentation.components.getTimePickerLabel
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.presentation.component.CustomAlertDialog
import com.project.tubocare.medication.presentation.component.CustomDropdownDosage
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationEditScreen(
    medicationId: String,
    state: MedicationState,
    onEvent: (MedicationEvent) -> Unit,
    navigateToDetail: () -> Unit,
    navigateToHome: () -> Unit
) {
    LaunchedEffect(Unit) {
        onEvent(MedicationEvent.GetMedicationById(medicationId))
    }

    LaunchedEffect(state.dosage) {
        if (state.dosage != null) {
            if (state.tempTimes.isEmpty()) {
                onEvent(MedicationEvent.OnTempTimesChanged(List(state.dosage) { index ->
                    state.weeklySchedule.values.flatten().getOrNull(index)?.time
                }))
            } else {
                val newTempTimes = if (state.dosage > state.tempTimes.size) {
                    state.tempTimes + List(state.dosage - state.tempTimes.size) { null }
                } else {
                    state.tempTimes.take(state.dosage)
                }
                onEvent(MedicationEvent.OnTempTimesChanged(newTempTimes))
                // Update weeklySchedule based on the new dosage
                val updatedSchedule = state.weeklySchedule.mapValues { entry ->
                    val newEntries = if (state.dosage > entry.value.size) {
                        entry.value + List(state.dosage - entry.value.size) { ChecklistEntry(time = null) }
                    } else {
                        entry.value.take(state.dosage)
                    }
                    newEntries.filterNotNull()
                }
                onEvent(MedicationEvent.OnScheduleChanged(updatedSchedule))
            }
        }
    }

    val isErrorName = state.errorName != null
    val isErrorInstruction = state.errorInstruction != null
    val isErrorFrequency = state.errorFrequency != null
    val isErrorRemain = state.errorRemain != null
    val isErrorNote = state.errorNote != null
    val isErrorTime = state.errorTimes != null
    val isErrorDosage = state.errorDosage != null

    val isDeleteError = state.errorDelete != null
    val isUpdateError = state.errorUpdate != null

    var openDialog by remember { mutableStateOf(false) }

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
                        text = stringResource(id = R.string.title_edit_medication),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToDetail() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { openDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
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
                .padding(paddingvalues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
               /* CustomOutlinedTextField(
                    value = state.name,
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
                    errorText = if (isErrorFrequency) state.errorFrequency else null
                )*/
                CustomOutlinedTextField(
                    value = state.remain?.toString() ?: "",
                    onValueChange = {
                        it.toIntOrNull()?.let { intValue ->
                            onEvent(MedicationEvent.OnRemainChanged(intValue))
                        }
                    },
                    label = stringResource(id = R.string.medication_label_remain),
                    placeholder = stringResource(id = R.string.medication_placeholder_remain),
                    keyboardType = KeyboardType.Number,
                    isError = isErrorRemain,
                    errorText = if (isErrorRemain) state.errorRemain else null
                )
                /*CustomOutlinedTextField(
                    value = state.note,
                    onValueChange = { onEvent(MedicationEvent.OnNoteChanged(it)) },
                    label = stringResource(id = R.string.appointment_label_note),
                    placeholder = stringResource(id = R.string.appointment_placeholder_note),
                    isError = isErrorNote,
                    errorText = if (isErrorNote) state.errorNote else null
                )*/
                /*CustomDropdownDosage(
                    selectedItem = state.dosage,
                    onItemSelected = {
                        onEvent(MedicationEvent.OnDosageChanged(it))
                        val newTempTimes = if (it > state.tempTimes.size) {
                            state.tempTimes + List(it - state.tempTimes.size) { null }
                        } else {
                            state.tempTimes.take(it)
                        }
                        onEvent(MedicationEvent.OnTempTimesChanged(newTempTimes))

                        // Update weeklySchedule based on the new dosage
                        val updatedSchedule = state.weeklySchedule.mapValues { entry ->
                            val newEntries = if (it > entry.value.size) {
                                entry.value + List(it - entry.value.size) { ChecklistEntry(time = null) }
                            } else {
                                entry.value.take(it)
                            }
                            newEntries
                        }
                        onEvent(MedicationEvent.OnScheduleChanged(updatedSchedule))
                    },
                    isError = isErrorDosage,
                    errorText = if (isErrorDosage) state.errorDosage else null
                )*/
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
                    value = stringResource(id = R.string.btn_save),
                    onClick = {
                        val updatedSchedule = state.weeklySchedule.mapValues {
                            state.tempTimes.filterNotNull().map { ChecklistEntry(it) }
                        }
                        onEvent(MedicationEvent.OnScheduleChanged(updatedSchedule))
                        onEvent(MedicationEvent.UpdateMedication)
                    },
                    isLoading = state.isLoading
                )
            }
        }
    }

    if (openDialog) {
        CustomAlertDialog(
            onDismissRequest = { openDialog = false },
            onConfirmation = {
                onEvent(MedicationEvent.DeleteMedication(state.selectedMedication))
            },
            dialogTitle = stringResource(id = R.string.alert_dialog_title),
            dialogText = stringResource(id = R.string.alert_dialog_text),
            isLoading = state.isLoadingDelete
        )
    }

    LaunchedEffect(key1 = isUpdateError) {
        if (isUpdateError) {
            val errorMessage = state.errorUpdate ?: "Gagal memperbarui data"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(MedicationEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = isDeleteError) {
        if (isDeleteError) {
            val errorMessage = state.errorDelete ?: "Gagal menghapus data"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(MedicationEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = state.isSuccessUpdate) {
        if (state.isSuccessUpdate) {
            Toast.makeText(context, "Jadwal minum obat berhasil diperbarui", Toast.LENGTH_SHORT).show()
            onEvent(MedicationEvent.ResetStatus)
            navigateToDetail()
        }
    }

    LaunchedEffect(key1 = state.isSuccessDelete) {
        if (state.isSuccessDelete) {
            openDialog = false
            Toast.makeText(context, "Jadwal minum obat berhasil dihapus", Toast.LENGTH_SHORT).show()
            onEvent(MedicationEvent.ResetStatus)
            navigateToHome()
        }
    }
}

@Preview
@Composable
private fun MedicationEditScreenPreview() {
    TuboCareTheme {
        MedicationEditScreen(
            medicationId = "",
            state = MedicationState(),
            onEvent = {},
            navigateToDetail = {},
            navigateToHome = {}
        )
    }
}
