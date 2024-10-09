package com.project.tubocare.healthcare.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.project.tubocare.R
import com.project.tubocare.core.util.formatLocalTimeToHourMinute
import com.project.tubocare.healthcare.presentation.event.HealthcareEvent
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.symptom.presentation.component.SymptomList
import com.project.tubocare.symptom.presentation.event.SymptomEvent
import com.project.tubocare.symptom.presentation.state.SymptomState
import com.project.tubocare.ui.theme.CustomSoftBlue
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationReportScreen(
    id: String,
    state: MedicationState,
    onEvent: (HealthcareEvent) -> Unit,
    navigateToDetailPatient: () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        onEvent(HealthcareEvent.GetMedicationById(id))
    }

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
                        text = stringResource(id = R.string.title_history_medication),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToDetailPatient() }) {
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
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(state.weeklySchedule.flatMap { (day, checklistEntries) -> checklistEntries.map { day to it } }) { (day, checklistEntry) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(CustomSoftBlue),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Detail Riwayat",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(12.dp)
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.secondary)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            if (checklistEntry.checked == true) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tanggal",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Text(
                                    text = "$day, ${checklistEntry.timestamp?.toLocalDate()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Waktu:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Text(
                                    text = formatLocalTimeToHourMinute(checklistEntry.timestamp?.toLocalTime()),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Keterangan:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Text(
                                    text = if (checklistEntry.checked) "Sudah minum obat" else "Belum minum obat",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Bukti Foto:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            if (state.image.isNotBlank()) {
                                AsyncImage(
                                    model = state.image,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(350.dp)
                                        .clip(MaterialTheme.shapes.small)
                                        .background(Color.White),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MedicationReportScreenPreview() {
    TuboCareTheme {
        MedicationReportScreen(
            id = "",
            state = MedicationState(),
            onEvent = {},
            navigateToDetailPatient = {}
        )
    }
}