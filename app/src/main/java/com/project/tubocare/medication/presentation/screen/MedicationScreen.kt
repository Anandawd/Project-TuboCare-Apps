package com.project.tubocare.medication.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.component.MedicationBox
import com.project.tubocare.medication.presentation.component.MedicationList
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.medication.presentation.util.processMedications
import com.project.tubocare.ui.theme.ChangeStatusBarColor
import com.project.tubocare.ui.theme.CustomDarkBlue
import com.project.tubocare.ui.theme.CustomWhiteBlue
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen(
    state: MedicationState,
    onEvent: (MedicationEvent) -> Unit,
    navigateToNotification: () -> Unit,
    navigateToDetail: (Medication) -> Unit,
    navigateToAll: () -> Unit,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {

    LaunchedEffect(Unit) {
        onEvent(MedicationEvent.GetMedications)
        onEvent(MedicationEvent.GetUserName)
    }

    Log.i("MedicationScreen", "State: ${state.medicationList}")

    val userName = if (state.userName.isNotBlank()) "Halo,\n${state.userName}" else "Halo,\nTuboCare"

    val (
        todayMedications,
        tomorrowMedications,
        nextMedications,
        otherMedications
    ) = processMedications(state.medicationList.data ?: emptyList())

    val isErrorGetMedications = state.errorGetMedications != null
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp, topEnd = 0.dp, bottomEnd = 30.dp, bottomStart = 30.dp
                        )
                    )
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = userName,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                        IconButton(
                            onClick = { navigateToNotification.invoke() },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = CustomWhiteBlue,
                                contentColor = CustomDarkBlue,
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_notification),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.medication_feedback),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    MedicationBox(
                        nextMedications = nextMedications,
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    MedicationList(
                        title = "Jadwal Hari Ini",
                        prefix = "Hari ini pukul ",
                        prefixEmpty = "hari ini",
                        medicationList = todayMedications,
                        onClick = { navigateToDetail(it) },
                        navigateToMedicationAll = { navigateToAll() },
                        onChecklistUpdated = {medicationId, day, updatedChecklist ->
                            run {
                                onEvent(
                                    MedicationEvent.UpdateChecklistEntry(
                                        medicationId,
                                        day,
                                        updatedChecklist
                                    )
                                )
                            }
                        }
                    )
                }
                item {
                    MedicationList(
                        title = "Jadwal Besok",
                        prefix = "Besok pukul ",
                        prefixEmpty = "besok",
                        medicationList = tomorrowMedications,
                        onClick = { navigateToDetail(it) },
                        navigateToMedicationAll = { navigateToAll() },
                        onChecklistUpdated = {medicationId, day, updatedChecklist ->
                            run {
                                onEvent(
                                    MedicationEvent.UpdateChecklistEntry(
                                        medicationId,
                                        day,
                                        updatedChecklist
                                    )
                                )
                            }
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
    LaunchedEffect(key1 = isErrorGetMedications) {
        if (isErrorGetMedications) {
            val errorMessage = state.errorGetMedications ?: "Gagal memuat jadwal minum obat"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(MedicationEvent.ClearToast)
        }
    }
}

@Preview
@Composable
private fun MedicationScreenPreview() {
    TuboCareTheme {
        MedicationScreen(
            state = MedicationState(),
            onEvent = {},
            navigateToNotification = {},
            navigateToDetail = {},
            navigateToAll = {}
        )
    }
}