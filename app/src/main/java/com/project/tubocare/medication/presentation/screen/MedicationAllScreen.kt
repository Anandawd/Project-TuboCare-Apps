package com.project.tubocare.medication.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.component.MedicationList
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.medication.presentation.util.groupMedicationsByDay
import com.project.tubocare.ui.theme.TuboCareTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationAllScreen(
    state: MedicationState,
    onEvent: (MedicationEvent) -> Unit,
    navigateToDetail: (Medication) -> Unit,
    navigateToHome: () -> Unit
) {

    val (medications) = groupMedicationsByDay(state.medicationList.data ?: emptyList())
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
                        text = stringResource(id = R.string.title_medication_list),
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
            LazyColumn {
                medications.forEach { (day, medications) ->
                    item {
                        MedicationList(
                            title = "Jadwal $day",
                            prefix = "Hari $day pukul ",
                            medicationList = medications,
                            onClick = { navigateToDetail(it) },
                            showAllButton = false,
                            navigateToMedicationAll = {},
                            onChecklistUpdated = { medicationId, day, updatedChecklist ->
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
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MedicationAllScreenPreview() {
    TuboCareTheme {
        MedicationAllScreen(
            state = MedicationState(),
            onEvent = {},
            navigateToDetail = {},
            navigateToHome = {}
        )
    }
}