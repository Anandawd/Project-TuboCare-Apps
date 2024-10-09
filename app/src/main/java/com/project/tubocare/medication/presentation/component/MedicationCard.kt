package com.project.tubocare.medication.presentation.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.core.util.formatLocalDateTimeToHourMinute
import com.project.tubocare.core.util.formatLocalTimeToString
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.util.getDayOfWeekInIndonesian
import com.project.tubocare.ui.theme.CustomSoftBlueTwo
import com.project.tubocare.ui.theme.TuboCareTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Composable
fun MedicationCard(
    modifier: Modifier = Modifier,
    prefix: String = "",
    checklist: ChecklistEntry,
    medication: Medication,
    onClickMedication: (Medication) -> Unit,
    onChecklistUpdated: (String, String, List<ChecklistEntry>) -> Unit,
    readOnly: Boolean = false
) {
    Log.d("MedicationCard", "checklist: $checklist")
    val checkedState = remember { mutableStateOf(checklist.checked ?: false) }
    val timestampState = remember { mutableStateOf(checklist.timestamp) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        modifier = modifier
            .padding(6.dp)
            .clickable { onClickMedication(medication) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pill_blue),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = prefix + checklist.time?.let { formatLocalTimeToString(it) },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    Box(
                        modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center
                    ) {
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = {
                               if (!readOnly){
                                   checkedState.value = it
                                   val updatedChecklist = checklist.copy(
                                       checked = it,
                                       timestamp = if (it) LocalDateTime.now() else null
                                   )
                                   timestampState.value = updatedChecklist.timestamp

                                   val updatedEntries = medication.weeklySchedule[getDayOfWeekInIndonesian(LocalDate.now().dayOfWeek)]?.map { entry ->
                                       if (entry.time == checklist.time) updatedChecklist else entry
                                   } ?: listOf()

                                   onChecklistUpdated(medication.medicationId, getDayOfWeekInIndonesian(LocalDate.now().dayOfWeek), updatedEntries)
                               }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = MaterialTheme.colorScheme.primary,
                                disabledUncheckedColor =  MaterialTheme.colorScheme.primary,
                                disabledCheckedColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = !readOnly
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = checkedState.value,
            enter = expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500)),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        ) {
            CheckListStatus(timestampState = timestampState)
        }
    }
}

@Composable
fun CheckListStatus(
    timestampState: MutableState<LocalDateTime?>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(60.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp,
                )
            )
            .background(CustomSoftBlueTwo),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_time_blue),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = timestampState.value?.let { "Sudah diminum pukul ${formatLocalDateTimeToHourMinute(it)}" } ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomMedicationCardPreview() {
    TuboCareTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            MedicationCard(
                medication = Medication(
                    medicationId = "",
                    userId = "",
                    name = "Paracetamol",
                    frequency = "",
                    instruction = "",
                    remain = 10,
                    note = "",
                    dosage = 1,
                    weeklySchedule = emptyMap(),
                    image =""
                ),
                checklist = ChecklistEntry(
                    time = LocalTime.now(),
                    checked = true,
                    timestamp = LocalDateTime.now()
                ),
                onClickMedication = {},
                onChecklistUpdated = {_,_,_ ->}
            )
        }
    }
}