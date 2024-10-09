package com.project.tubocare.appointment.presentation.screen

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
import com.project.tubocare.appointment.presentation.state.AppointmentState
import com.project.tubocare.core.presentation.components.CustomOutlineDatePicker
import com.project.tubocare.core.presentation.components.CustomOutlineTimePicker
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.util.formatDateToString2
import com.project.tubocare.core.util.parseStringToDate
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewAppointmentScreen(
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    navigateToHome: () -> Unit,
) {

    val isErrorName = state.errorName != null
    val isErrorDate = state.errorDate != null
    val isErrorTime = state.errorTime != null
    val isErrorLocation = state.errorLocation != null

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
                    IconButton(onClick = { }) {
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
                    onValueChange = { onEvent(AppointmentEvent.OnNameChanged(it)) },
                    label = stringResource(id = R.string.appointment_label_name),
                    placeholder = stringResource(id = R.string.appointment_placeholder_name),
                    isError = isErrorName,
                    errorText = if (isErrorName) state.errorName else null
                )
                CustomOutlineDatePicker(
                    value = formatDateToString2(date = state.date),
                    onDateSelected = { onEvent(AppointmentEvent.OnDateChanged(parseStringToDate(it)))},
                    label = stringResource(id = R.string.appointment_label_date),
                    placeholder = stringResource(id = R.string.appointment_placeholder_date),
                    isError = isErrorDate,
                    errorText = if (isErrorDate) state.errorDate else null
                )
                CustomOutlineTimePicker(
                    selectedItem = state.time,
                    onTimeSelected = { onEvent(AppointmentEvent.OnTimeChanged(it))},
                    label = stringResource(id = R.string.appointment_label_time),
                    placeholder = stringResource(id = R.string.appointment_placeholder_time),
                    isError = isErrorTime,
                    errorText = if (isErrorTime) state.errorTime else null
                )
                CustomOutlinedTextField(
                    value = state.location,
                    onValueChange = { onEvent(AppointmentEvent.OnLocationChanged(it))},
                    label = stringResource(id = R.string.appointment_label_location),
                    placeholder = stringResource(id = R.string.appointment_placeholder_location),
                    isError = isErrorLocation,
                    errorText = if (isErrorLocation) state.errorLocation else null
                )
                CustomOutlinedTextField(
                    value = state.note ?: "",
                    onValueChange = {onEvent(AppointmentEvent.OnNoteChanged(it))},
                    label = stringResource(id = R.string.appointment_label_note),
                    placeholder = stringResource(id = R.string.appointment_placeholder_note),
                )
                Spacer(modifier = Modifier.weight(1f))
                CustomPrimaryButton(
                    value = stringResource(id = R.string.btn_done),
                    onClick = {
                        onEvent(AppointmentEvent.AddAppointment)
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
            onEvent(AppointmentEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = state.isSuccessAdd) {
        if (state.isSuccessAdd) {
            Toast.makeText(context, "Jadwal kontrol berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            onEvent(AppointmentEvent.ResetStatus)
            navigateToHome()
        }
    }
}

@Preview
@Composable
private fun NewAppointmentScreenPreview() {
    TuboCareTheme {
        NewAppointmentScreen(
            state = AppointmentState(),
            onEvent = {},
            navigateToHome = {}
        )
    }
}