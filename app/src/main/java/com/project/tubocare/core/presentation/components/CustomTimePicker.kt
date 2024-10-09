package com.project.tubocare.core.presentation.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.project.tubocare.R
import com.project.tubocare.core.util.formatLocalTimeToHourMinute
import com.project.tubocare.medication.presentation.util.formatTimeTo12Hour
import com.project.tubocare.ui.theme.CustomDarkBlue
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlineTimePicker(
    selectedItem: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorText: String? = null
) {
    var timeResult by remember { mutableStateOf(selectedItem?.let { formatLocalTimeToHourMinute(it) } ?: "") }
    var openDialog by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(
        initialHour = selectedItem?.hour ?: 0,
        initialMinute = selectedItem?.minute ?: 0,
        is24Hour = false
    )
    val cal = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .padding(0.dp, 5.dp),
            value = timeResult,
            onValueChange = {  },
            label = { Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            ) },
            placeholder = { Text(
                text = placeholder,
                style = MaterialTheme.typography.labelLarge,
                color = CustomGrayBlue
            ) },
            textStyle = MaterialTheme.typography.labelLarge,
            readOnly = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = CustomGrayBlue,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.secondary,
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                focusedPlaceholderColor = CustomGrayBlue,
                unfocusedPlaceholderColor = CustomGrayBlue,
            ),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alarm),
                    contentDescription = null,
                    tint = CustomDarkBlue,
                    modifier = Modifier.clickable {
                        openDialog = true
                    }
                )
            }
        )
    }
    if (isError && errorText != null) {
        Text(
            text = errorText,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }

    if (openDialog) {
        TimePickerDialog(
            onCancel = { openDialog = false },
            onConfirm = {
                cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                cal.set(Calendar.MINUTE, timePickerState.minute)
                cal.isLenient = false

                val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                timeResult = formatTimeTo12Hour(selectedTime)

                onTimeSelected(selectedTime)
                openDialog = false
            }
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.White,
                    clockDialSelectedContentColor = Color.White,
                    clockDialUnselectedContentColor = MaterialTheme.colorScheme.secondary,
                    selectorColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    periodSelectorBorderColor = MaterialTheme.colorScheme.secondary,
                    periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondary,
                    periodSelectorSelectedContentColor = Color.White,
                    periodSelectorUnselectedContainerColor = Color.White,
                    periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.secondary,
                    timeSelectorSelectedContainerColor = Color.White,
                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.secondary,
                    timeSelectorUnselectedContainerColor = CustomGrayBlue,
                    timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.secondary
                ),
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface
                )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            )
                    }
                    TextButton(onClick = onConfirm) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            )
                    }
                }
            }
        }
    }
}

fun getTimePickerLabel(index: Int): String {
    return when(index) {
        0 -> "Waktu minum obat pertama"
        1 -> "Waktu minum obat kedua"
        2 -> "Waktu minum obat ketiga"
        3 -> "Waktu minum obat keempat"
        4 -> "Waktu minum obat kelima"
        else -> "Waktu minum obat ke-${index + 1}"
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomOutlineTimePickerPreview() {
    TuboCareTheme {
        Box(Modifier.padding(20.dp)) {
            CustomOutlineTimePicker(
                selectedItem = null,
                onTimeSelected = {},
                label = "Waktu",
                placeholder = "Masukkan waktu keluhan",
            )
        }

    }
}