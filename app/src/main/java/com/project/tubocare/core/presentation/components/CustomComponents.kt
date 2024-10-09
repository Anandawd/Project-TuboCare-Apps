package com.project.tubocare.core.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.project.tubocare.R
import com.project.tubocare.ui.theme.CustomBlue
import com.project.tubocare.ui.theme.CustomDarkBlue
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun CustomHeaderComponent(
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "logo")
        Text(
            text = value,
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            color = CustomBlue,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    isSingleLine: Boolean = true,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null
) {
    val isVisible = remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .clip(shapes.small)
                .padding(0.dp, 5.dp),
            value = value,
            onValueChange = onValueChange,
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
                errorTextColor = MaterialTheme.colorScheme.secondary
            ),
            trailingIcon = trailingIcon ?: if (isPassword) {
                {
                    val iconImage = if (isVisible.value) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    val description = if (isVisible.value) {
                        stringResource(id = R.string.hide_password)
                    } else {
                        stringResource(id = R.string.show_password)
                    }

                    IconButton(onClick = { isVisible.value = !isVisible.value }) {
                        Icon(
                            imageVector = iconImage,
                            contentDescription = description,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            } else null,
            visualTransformation = if (isPassword && !isVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = isSingleLine,
            isError = isError,
        )
        if (isError && errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
fun CustomOutlinedNumberField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (Int?) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorText: String? = null
) {
    val input = remember { mutableStateOf(value) }

    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .clip(shapes.small)
                .padding(0.dp, 5.dp),
            value = input.value,
            onValueChange = {
                input.value = it
                onValueChange(it.toIntOrNull())
            },
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
                errorTextColor = MaterialTheme.colorScheme.secondary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = isError,
        )
        if (isError && errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAutocompleteTextField(
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorText: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }
    val filteredSuggestions = remember { mutableStateListOf<String>() }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(input) {
        filteredSuggestions.clear()
        if (input.length >= 2) {
            filteredSuggestions.addAll(suggestions.filter {
                it.contains(input, ignoreCase = true)
            })
            expanded = filteredSuggestions.isNotEmpty()
        } else {
            expanded = false
        }
    }

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = {
                    input = it
                    onValueChange(it)
                },
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
                    .clip(shapes.small)
                    .padding(0.dp, 5.dp)
                    .onGloballyPositioned { coordinates ->
                        // Update the size of the TextField
                        textFieldSize = coordinates.size.toSize()
                    }
                    .menuAnchor(),
                isError = isError
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                    .align(Alignment.Start) // Ensure the dropdown aligns with the start of the TextField
            ) {
                filteredSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        onClick = {
                            input = suggestion
                            onValueChange(suggestion)
                            expanded = false
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
        if (isError && errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomAutocompleteTextFieldPreview() {
    val suggestions = listOf("Paracetamol", "Ibuprofen", "Aspirin", "Amoxicillin", "Metformin")

    TuboCareTheme {
        CustomAutocompleteTextField(
            suggestions = suggestions,
            label = "Medication Name",
            placeholder = "Enter medication name",
            onValueChange = {},
            isError = true,
            errorText = "Field cannot be empty"
        )
    }
}


@Composable
fun CustomClickableText(
    value: String,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clickable {
                onClick()
            },
        text = value,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        textAlign = TextAlign.Right
    )
}

@Composable
fun CustomClickableAnnotatedText(
    onLoginScreen: Boolean = true,
    onTextSelected: (String) -> Unit,
) {
    val initialText = if (onLoginScreen) "Belum punya akun? " else "Sudah punya akun? "
    val clickableText = if (onLoginScreen) "Daftar" else "Login"

    val annotatedString = buildAnnotatedString {

        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary,
            )
        ) {
            pushStringAnnotation(tag = initialText, annotation = initialText)
            append(initialText)
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        ) {
            pushStringAnnotation(tag = clickableText, annotation = clickableText)
            append(clickableText)
        }

    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableRegisterText", "{${span.item}}")

                    if (span.item == clickableText) {
                        onTextSelected(span.item)
                    }
                }
        })
}

@Composable
fun CustomClickableLoginText(
    onTextSelected: (String) -> Unit,
) {
    val initialText = "Sudah punya akun? "
    val loginText = "Login"

    val annotatedString = buildAnnotatedString {

        withStyle(
            style = SpanStyle(
                color = CustomDarkBlue,
            )
        ) {
            pushStringAnnotation(tag = initialText, annotation = initialText)
            append(initialText)
        }
        withStyle(
            style = SpanStyle(
                color = CustomDarkBlue,
                fontWeight = FontWeight.Bold
            )
        ) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }

    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth(),
        style = TextStyle(
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableLoginText", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }
        })
}

@Composable
fun CustomPrimaryButton(
    value: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    leadingIcon: Painter? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = color,
            disabledContentColor = Color.White,
            disabledContainerColor = color.copy(alpha = 0.5f)
        ),
        enabled = !isLoading
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                if (leadingIcon != null) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun CustomSecondaryButton(
    value: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    leadingIcon: Painter? = null
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        ),
        enabled = !isLoading
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                if (leadingIcon != null) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun CustomOutlinedButton(
    value: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    leadingIcon: Painter? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = color,
//            containerColor = color,
//            disabledContentColor = color.copy(alpha = 0.5f),
//            disabledContainerColor = color.copy(alpha = 0.5f)
        ),
        border = BorderStroke(color = color, width = 1.dp),
        enabled = !isLoading
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                if (leadingIcon != null) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
fun CustomDivider(
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = CustomDarkBlue,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = CustomDarkBlue,
            thickness = 1.dp
        )

    }
}

@Composable
fun CustomIconBack(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Icon(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .clickable { onClick() },
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "icon back",
            tint = CustomDarkBlue,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdown(
    listItem: Array<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean = false,
    errorText: String? = null,
    readOnly: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedItem,
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
                    readOnly = readOnly,
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
                        .clip(shapes.small)
                        .padding(0.dp, 5.dp)
                        .menuAnchor(),
                    isError = isError
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    listItem.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item,
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
private fun DropdownRolePreview() {
    TuboCareTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            CustomDropdown(
                listItem = stringArrayResource(id = R.array.list_role),
                selectedItem = "Pasien",
                onItemSelected = {},
                label = "Peran Anda",
                placeholder = stringResource(id = R.string.placeholder_role),
                isError = true,
                errorText = "Role cannot be empty"
            )
        }
    }
}

@Composable
fun CustomRadioButton(
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String? = null
) {
    val listGender = stringArrayResource(id = R.array.list_gender)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, top = 14.dp, end = 0.dp, bottom = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            listGender.forEach { gender ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = (gender == selectedGender),
                            onClick = { onGenderSelected(gender) }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = gender.equals(selectedGender, ignoreCase = true),
                        onClick = { onGenderSelected(gender) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = CustomDarkBlue,
                            unselectedColor = CustomDarkBlue,
                            disabledSelectedColor = CustomGrayBlue,
                            disabledUnselectedColor = CustomGrayBlue
                        ),
                    )
                    Text(
                        text = gender,
                        modifier = Modifier.padding(start = 4.dp, end = 30.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium
                    )
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
private fun CustomHeaderComponent() {
    TuboCareTheme {
        CustomHeaderComponent("Selamat Datang")
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomOutlinedTextFieldPreview() {
    TuboCareTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            CustomOutlinedTextField(
                value = "",
                onValueChange = {},
                label = "Email",
                placeholder = "masukkan email",
                isError = true,
                errorText = "Email cannot be empty"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomClickableTextPreview() {
    TuboCareTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            CustomClickableText("Lupa password?") {}
        }
    }
}

@Preview
@Composable
private fun CustomPrimaryButtomPreview() {
    TuboCareTheme {
        CustomPrimaryButton(value = "Masuk", onClick = {}, isLoading = false)
    }
}

@Preview
@Composable
private fun CustomSecondaryButtomPreview() {
    TuboCareTheme {
        CustomSecondaryButton(value = "Masuk dengan Google", onClick = {})
    }
}


@Preview(showBackground = true)
@Composable
private fun CustomDividerPreview() {
    TuboCareTheme {
        CustomDivider("atau")
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomClickableAnnotatedTextPreview() {
    TuboCareTheme {
        CustomClickableAnnotatedText(onTextSelected = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomIconBackPreview() {
    TuboCareTheme {
        CustomIconBack(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomRadioButtonPreview() {
    val listGender = stringArrayResource(id = R.array.list_gender)

    TuboCareTheme {
        CustomRadioButton(
            label = stringResource(id = R.string.label_gender),
            selectedGender = listGender[0],
            onGenderSelected = {},
            isError = true,
            errorText = "jenis kelamin tidak boleh kosong"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomOutlinedButtonPreview() {
    TuboCareTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            CustomOutlinedButton(
                value = "Batal",
                onClick = {}
            )
        }

    }
}

