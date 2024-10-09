package com.project.tubocare.notification.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.medication.presentation.component.CustomAlertDialog
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.notification.presentation.component.NotificationList
import com.project.tubocare.notification.presentation.event.NotificationEvent
import com.project.tubocare.notification.presentation.state.NotificationState
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    state: NotificationState,
    onEvent: (NotificationEvent) -> Unit,
    navigateToHome: () -> Unit,
) {

    LaunchedEffect(Unit) {
        onEvent(NotificationEvent.GetNotifications)
    }

    Log.d("NotificationScreen", "state: ${state.notificationDataList}")

    val isDeleteError = state.errorDelete != null
    val isGetNotifications = state.errorDelete != null

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
                        text = stringResource(id = R.string.title_notification),
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
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background
            )
            Column(
                modifier = Modifier
                    .padding(20.dp),
            ) {
                NotificationList(
                    notificationDataList = state.notificationDataList
                )
            }
        }
    }

    if (openDialog) {
        CustomAlertDialog(
            onDismissRequest = { openDialog = false },
            onConfirmation = {
                onEvent(NotificationEvent.DeleteNotifications)
            },
            dialogTitle = stringResource(id = R.string.alert_dialog_title),
            dialogText = stringResource(id = R.string.alert_dialog_text_3),
            isLoading = state.isLoadingDelete
        )
    }

    LaunchedEffect(key1 = isGetNotifications) {
        if (isGetNotifications) {
            val errorMessage = state.errorGetNotifications ?: "Gagal memuat notifikasi"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(NotificationEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = isDeleteError) {
        if (isDeleteError) {
            val errorMessage = state.errorDelete ?: "Gagal menghapus notifikasi"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(NotificationEvent.ClearToast)
            onEvent(NotificationEvent.GetNotifications)
        }
    }

    LaunchedEffect(key1 = state.isSuccessDelete) {
        if (state.isSuccessDelete) {
            openDialog = false
            Toast.makeText(context, "Daftar notifikasi berhasil dihapus", Toast.LENGTH_SHORT).show()
            onEvent(NotificationEvent.ResetStatus)
            onEvent(NotificationEvent.GetNotifications)
        }
    }
}


@Preview
@Composable
private fun NotificationScreenPreview() {
    TuboCareTheme {
        NotificationScreen(
            state = NotificationState(),
            onEvent = {},
            navigateToHome = {},
        )
    }
}