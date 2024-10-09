package com.project.tubocare.medication.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.medication.presentation.util.getDayOfWeekInIndonesian
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme
import io.github.boguszpawlowski.composecalendar.day.Day
import kotlinx.datetime.DayOfWeek


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownInstruction(
    label: String = stringResource(id = R.string.medication_label_instruction),
    placeholder: String = stringResource(id = R.string.medication_placeholder_instruction),
) {
    val listInstruction = stringArrayResource(id = R.array.list_instruction)

    var isExpanded by remember { mutableStateOf(false) }
    var onSelectedField by remember { mutableStateOf("") }

    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                OutlinedTextField(
                    value = onSelectedField,
                    onValueChange = {},
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
                    trailingIcon = {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .padding(0.dp, 5.dp)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    listInstruction.forEach { text ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            },
                            onClick = {
                                onSelectedField = text
                                isExpanded = false
                            },
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomDropdownPreview() {
    TuboCareTheme {
        CustomDropdownInstruction()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownFrequency(
    label: String = stringResource(id = R.string.medication_label_frequency),
    placeholder: String = stringResource(id = R.string.medication_placeholder_frequency),
) {

    val listFrequency = stringArrayResource(id = R.array.list_frequency)

    var isExpanded by remember { mutableStateOf(false) }
    var onSelectedField by remember { mutableStateOf("") }

    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                OutlinedTextField(
                    value = onSelectedField,
                    onValueChange = {},
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
                    trailingIcon = {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .padding(0.dp, 5.dp)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    listFrequency.forEach { text ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            },
                            onClick = {
                                onSelectedField = text
                                isExpanded = false
                            },
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomDropdownFrequencyPreview() {
    TuboCareTheme {
        CustomDropdownFrequency()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownDosage(
    selectedItem: Int?,
    onItemSelected: (Int) -> Unit,
    label: String = stringResource(id = R.string.medication_label_dosage),
    placeholder: String = stringResource(id = R.string.medication_placeholder_dosage),
    isError: Boolean = false,
    errorText: String? = null
) {
    val listDosage = listOf(1, 2, 3, 4, 5)

    var isExpanded by remember { mutableStateOf(false) }

    val value = if (selectedItem == null) "" else "$selectedItem kali"

    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {},
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
                    trailingIcon = {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .padding(0.dp, 5.dp)
                        .menuAnchor(),
                    isError = isError
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    listDosage.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "$item kali",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            },
                            onClick = {
                                onItemSelected(item)
                                isExpanded = false
                            },
                            colors = MenuItemColors(
                                textColor = MaterialTheme.colorScheme.secondary,
                                leadingIconColor = MaterialTheme.colorScheme.secondary,
                                trailingIconColor = MaterialTheme.colorScheme.secondary,
                                disabledTextColor = CustomGrayBlue,
                                disabledLeadingIconColor = MaterialTheme.colorScheme.secondary,
                                disabledTrailingIconColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                }
            }
        }
        if (isError && errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomDropdownDosagePreview() {
    TuboCareTheme {
        CustomDropdownDosage(
            selectedItem = 1,
            onItemSelected = {}
        )
    }
}

@Composable
fun CustomCheckBoxDays(
    selectedDays: List<String>,
    onDaySelected: (List<String>) -> Unit,
    label: String = stringResource(id = R.string.medication_label_days)
) {

    val listDays = DayOfWeek.values().map { getDayOfWeekInIndonesian(it) }

    val checkedStates = remember { mutableStateListOf<Boolean>() }

    listDays.forEach { day ->
        checkedStates.add(selectedDays.contains(day))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 0.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            listDays.forEachIndexed { index, day ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Checkbox(
                        checked = checkedStates[index],
                        onCheckedChange = {
                            checkedStates[index] = it
                            val selected = listDays.filterIndexed { i, _ -> checkedStates[i] }
                            onDaySelected(selected)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CustomCheckBoxDaysPreview() {
    TuboCareTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CustomCheckBoxDays(
                selectedDays = emptyList(),
                onDaySelected = {}
            )
        }
    }
}

