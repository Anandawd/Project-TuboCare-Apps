package com.project.tubocare.symptom.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.appointment.presentation.event.AppointmentEvent
import com.project.tubocare.core.presentation.components.CustomOutlineDatePicker
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.presentation.components.CustomOutlineTimePicker
import com.project.tubocare.core.util.formatDateToString2
import com.project.tubocare.core.util.parseStringToDate
import com.project.tubocare.medication.presentation.component.CustomAlertDialog
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.symptom.presentation.event.SymptomEvent
import com.project.tubocare.symptom.presentation.state.SymptomState
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomEditScreen(
    symptomId: String,
    state: SymptomState,
    onEvent: (SymptomEvent) -> Unit,
    navigateToList: () -> Unit,
) {
    LaunchedEffect(Unit) {
        onEvent(SymptomEvent.GetSymptomById(symptomId))
    }

    val isErrorName = state.errorName != null
    val isErrorDate = state.errorDate != null
    val isErrorTime = state.errorTime != null
    val isErrorNote = state.errorNote != null

    val isUpdateError = state.errorUpdate != null
    val isDeleteError = state.errorDelete != null

    var openDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
    ) {
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
                            text = stringResource(id = R.string.title_edit_symptom),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(10.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigateToList() }) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                               )
                        }
                    },
                    actions = {
                        IconButton(onClick = { openDialog = true }) {
                            Icon(painter = painterResource(id = R.drawable.ic_delete),
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
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    CustomOutlinedTextField(
                        value = state.name,
                        onValueChange = { onEvent(SymptomEvent.OnNameChanged(it))},
                        label = stringResource(id = R.string.symptom_label_name),
                        placeholder = stringResource(id = R.string.symptom_placeholder_name),
                        isError = isErrorName,
                        errorText = if (isErrorName) state.errorName else null
                    )
                    CustomOutlineDatePicker(
                        value = formatDateToString2(date = state.date),
                        onDateSelected = { onEvent(SymptomEvent.OnDateChanged(parseStringToDate(it)))},
                        label = stringResource(id = R.string.symptom_label_date),
                        placeholder = stringResource(id = R.string.symptom_placeholder_date),
                        isError = isErrorDate,
                        errorText = if (isErrorDate) state.errorDate else null
                    )
                    if (state.time != null){
                        CustomOutlineTimePicker(
                            selectedItem = state.time,
                            onTimeSelected = {
                                onEvent(SymptomEvent.OnTimeChanged(it))
                            },
                            label = stringResource(id = R.string.symptom_label_time),
                            placeholder = stringResource(id = R.string.symptom_placeholder_time),
                            isError = isErrorTime,
                            errorText = if (isErrorTime) state.errorTime else null
                        )
                    }
                    CustomOutlinedTextField(
                        value = state.note,
                        onValueChange = { onEvent(SymptomEvent.OnNoteChanged(it))},
                        label = stringResource(id = R.string.symptom_label_note),
                        placeholder = stringResource(id = R.string.symptom_placeholder_note),
                        isError = isErrorNote,
                        errorText = if (isErrorNote) state.errorNote else null
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CustomPrimaryButton(
                        value = stringResource(id = R.string.btn_save),
                        onClick = { onEvent(SymptomEvent.UpdateSymptom) },
                        isLoading = state.isLoading
                    )
                }
            }
        }
    }

    if (openDialog) {
        CustomAlertDialog(
            onDismissRequest = { openDialog = false },
            onConfirmation = {
                onEvent(SymptomEvent.DeleteSymptom(symptomId))
            },
            dialogTitle = stringResource(id = R.string.alert_dialog_title),
            dialogText = stringResource(id = R.string.alert_dialog_text_2),
            isLoading = state.isLoadingDelete
        )
    }

    LaunchedEffect(key1 = isUpdateError) {
        if (isUpdateError) {
            val errorMessage = state.errorUpdate ?: "Gagal memperbarui riwayat"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(SymptomEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = isDeleteError) {
        if (isDeleteError) {
            val errorMessage = state.errorDelete ?: "Gagal menghapus riwayat"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(SymptomEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = state.isSuccessUpdate) {
        if (state.isSuccessUpdate) {
            Toast.makeText(context, "Riwayat keluhan berhasil diperbarui", Toast.LENGTH_SHORT).show()
            onEvent(SymptomEvent.ResetStatus)
            navigateToList()
        }
    }

    LaunchedEffect(key1 = state.isSuccessDelete) {
        if (state.isSuccessDelete) {
            openDialog = false
            Toast.makeText(context, "Riwayat keluhan berhasil dihapus", Toast.LENGTH_SHORT).show()
            onEvent(SymptomEvent.ResetStatus)
            navigateToList()
        }
    }
}

@Preview
@Composable
private fun EditSymptomScreenPreview() {
    TuboCareTheme {
        SymptomEditScreen(
            symptomId = "",
            state = SymptomState(),
            onEvent = {},
            navigateToList = {}
        )
    }
}