package com.project.tubocare.profile.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.ui.theme.CustomBlue
import com.project.tubocare.ui.theme.CustomDarkBlue
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun CustomClickableProfileMenu(
    value: String,
    onClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Text(
            modifier = Modifier
                .clickable {
                    onClick()
                },
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = CustomDarkBlue,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CustomOutlineButtom(
    value: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomProfileTextField(
    value: String,
    labelValue: String,
    placeholder: String,
    isSingleLine: Boolean = true,
    keyboardOptions: KeyboardOptions
) {
    val textValue = remember {
        mutableStateOf(value)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .padding(0.dp, 5.dp),
        value = textValue.value,
        textStyle = MaterialTheme.typography.labelLarge,
        onValueChange = {
            textValue.value = it
        },
        label = { Text(text = labelValue) },
        placeholder = { Text(text = placeholder) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.secondary,
            unfocusedTextColor = CustomGrayBlue,
            cursorColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
        ),
        keyboardOptions = keyboardOptions,
        singleLine = isSingleLine
    )
}

@Preview(showBackground = true)
@Composable
private fun ClickableProfileMenuPreview() {
    TuboCareTheme {
        CustomClickableProfileMenu(value = "Ubah Profile", onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomOutlineButtomPreview() {
    TuboCareTheme {
        CustomOutlineButtom("Keluar", {})
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomProfileTextFieldPreview() {
    TuboCareTheme {
        CustomProfileTextField(
            value = "Ananda Widiastana",
            labelValue = "Nama Lengkap",
            placeholder = "Masukkan nama lengkap",
            keyboardOptions = KeyboardOptions.Default
        )
    }
}