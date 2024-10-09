package com.project.tubocare.core.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.auth.util.Tools
import com.project.tubocare.ui.theme.CustomDarkBlue
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlineDatePicker(
    value: String,
    onDateSelected: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorText: String? = null
) {
    var dateResult by remember { mutableStateOf(value) }
    var openDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker
    )
    val confirmEnabled = derivedStateOf { true }

    LaunchedEffect(key1 = value) {
        dateResult = value
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .padding(0.dp, 5.dp),
            value = dateResult,
            onValueChange = { dateResult = it },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelLarge,
                    color = CustomGrayBlue
                )
            },
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
                    painter = painterResource(id = R.drawable.ic_date_white),
                    contentDescription = null,
                    tint = CustomDarkBlue,
                    modifier = Modifier.clickable {
                        openDialog = true
                    }
                )
            }
        )
        if (isError && errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }


    if (openDialog) {
        DatePickerDialog(
            tonalElevation = 10.dp,
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                        var date = "no selection"
                        if (datePickerState.selectedDateMillis != null) {
                            date = Tools.convertLongToTime(datePickerState.selectedDateMillis!!)
                            dateResult = date
                            onDateSelected(dateResult)
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = {
                    Text(
                        text = "Pilih Tanggal",
                        modifier = Modifier.padding(20.dp),
                    )
                },
                colors = DatePickerDefaults.colors(
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    headlineContentColor = MaterialTheme.colorScheme.secondary,
                    containerColor = MaterialTheme.colorScheme.background,
                    yearContentColor = MaterialTheme.colorScheme.secondary,
                    weekdayContentColor = MaterialTheme.colorScheme.secondary,
                    subheadContentColor = MaterialTheme.colorScheme.secondary,
                    navigationContentColor = MaterialTheme.colorScheme.secondary,
                    dayContentColor = MaterialTheme.colorScheme.secondary,
                    dividerColor = MaterialTheme.colorScheme.secondary,
                ),
                showModeToggle = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomOutlineDatePickerPreview() {
    TuboCareTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.padding(20.dp)) {
                CustomOutlineDatePicker(
                    value = "",
                    label = "Tanggal Lahir",
                    placeholder = "Masukkan tanggal lahir",
                    onDateSelected = { /* Handle date selection */ },
                    isError = true,
                    errorText = "tanggal lahir tidak boleh kosong"
                )

            }
        }
    }
}


