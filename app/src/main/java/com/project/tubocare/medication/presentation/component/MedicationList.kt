package com.project.tubocare.medication.presentation.component

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.tubocare.R
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.util.getUniqueTimeMedication
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme
import kotlin.reflect.jvm.internal.impl.util.Check

@Composable
fun MedicationList(
    title: String,
    prefix: String = "",
    prefixEmpty: String = "",
    medicationList: List<Pair<Medication, ChecklistEntry>>,
    onClick: (Medication) -> Unit,
    onChecklistUpdated: (String, String, List<ChecklistEntry>) -> Unit,
    readOnly: Boolean = false,
    showAllButton: Boolean = true,
    navigateToMedicationAll: () -> Unit
) {
    Log.d("MedicationList", "Title: $title, Medications: $medicationList")
    Column(
        modifier = Modifier.padding(horizontal = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = MaterialTheme.typography.bodyLarge.fontWeight),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(6.dp)
            )
            if (showAllButton){
                TextButton(onClick = { navigateToMedicationAll() }) {
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                            fontSize = 14.sp
                        ),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_next),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        if (medicationList.isEmpty()) {
            EmptyMedicationScreen(prefix = prefixEmpty)
        }

        medicationList.forEach { (medication, checklistEntry) ->
            MedicationCard(
                medication = medication,
                prefix = prefix,
                checklist = checklistEntry,
                onClickMedication = { onClick(medication) },
                onChecklistUpdated = onChecklistUpdated,
                readOnly = readOnly
            )
        }
    }
}

@Composable
fun EmptyMedicationScreen(
    modifier: Modifier = Modifier,
    prefix: String = ""
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        modifier = modifier.padding(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Jadwal $prefix tidak tersedia",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = MaterialTheme.typography.bodyMedium.fontWeight),
                color = CustomGrayBlue,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MedicationListPreview() {
    val uniqueTimeMedications = getUniqueTimeMedication(
        listOf(
            Medication(
                medicationId = "",
                userId = "",
                name = "Paracetamol",
                frequency = "",
                instruction = "",
                remain = 10,
                note = "",
                dosage = 1,
                weeklySchedule = emptyMap(),
                image = ""
            ),
        )
    )

    TuboCareTheme {
        MedicationList(
            title = "Jadwal hari ini",
            prefix = "Hari ini pukul ",
            medicationList = uniqueTimeMedications,
            onClick = {},
            onChecklistUpdated = { _, _, _ -> },
            navigateToMedicationAll = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyMedicationScreenPreview() {
    TuboCareTheme {
        EmptyMedicationScreen()
    }
}
