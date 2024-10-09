package com.project.tubocare.medication.presentation.component

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.core.presentation.components.CustomOutlinedButton
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: Painter = painterResource(id = R.drawable.ic_delete),
    isLoading: Boolean = false,
) {
    AlertDialog(
        icon = {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = dialogTitle,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        text = {
            Text(
                text = dialogText,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        dismissButton = {
            CustomOutlinedButton(value = "Batal", onClick = { onDismissRequest() })
        },
        confirmButton = {
            CustomPrimaryButton(
                value = "Hapus",
                onClick = { onConfirmation() },
                color = MaterialTheme.colorScheme.error,
                isLoading = isLoading
            )
        }
    )
}

@Preview
@Composable
private fun CustomAlertDialogPreview() {
    TuboCareTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomAlertDialog(
                onDismissRequest = {},
                onConfirmation = {},
                dialogTitle = stringResource(id = R.string.alert_dialog_title),
                dialogText = stringResource(id = R.string.alert_dialog_text),
                icon = painterResource(id = R.drawable.ic_delete),
            )
        }
    }
}