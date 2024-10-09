package com.project.tubocare.healthcare.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.appointment.presentation.component.AppointmentList
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.component.MedicationList
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.medication.presentation.util.groupMedicationsByDay
import com.project.tubocare.medication.presentation.util.processMedications
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.symptom.presentation.component.SymptomList
import com.project.tubocare.ui.theme.TuboCareTheme
import com.project.tubocare.weight.domain.model.Weight
import com.project.tubocare.weight.presentation.component.WeightList
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientTab(
    medicationList: List<Medication>,
    appointmentList: List<Appointment>,
    symptomList: List<Symptom>,
    weightList: List<Weight>,
    onClickMedication: (Medication) -> Unit,
    onClickAppointment: (Appointment) -> Unit,
    onClickSymptom: (Symptom) -> Unit,
    onClickWeight: (Weight) -> Unit,
) {

    val (medications) = groupMedicationsByDay(medicationList)

    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Minum Obat", "Kontrol", "Keluhan", "Berat Badan")
    Column {
        PrimaryTabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        when (state) {
            0 -> {
                LazyColumn {
                    medications.forEach { (day, medications) ->
                        item {
                            MedicationList(
                                title = "Jadwal $day",
                                prefix = "Hari $day pukul ",
                                medicationList = medications,
                                onClick = { onClickMedication(it) },
                                onChecklistUpdated = { _, _, _ -> },
                                navigateToMedicationAll = {},
                                readOnly = true,
                                showAllButton = false
                            )
                        }
                    }
                }
            }

            1 -> AppointmentList(
                appointmentList = appointmentList,
                onClick = { onClickAppointment(it) },
                onClickCheck = { _, _ -> },
                readOnly = true
            )

            2 -> SymptomList(
                symptomList = symptomList,
                onClick = { onClickSymptom(it) }
            )

            3 -> WeightList(
                weightList = weightList,
                onClick = { onClickWeight(it) },
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PatientTabPreview() {
    TuboCareTheme {
        Column(Modifier.fillMaxSize()) {
            PatientTab(
                medicationList = emptyList(),
                appointmentList = listOf(
                    Appointment(
                        appointmentId = "",
                        userId = "",
                        name = "Pengambilan Obat",
                        date = Date(),
                        time = LocalTime.now(),
                        location = "Puskesmas Dinoyo",
                        note = "Ambil obat untuk satu bulan ke depan",
                        done = true
                    ),
                    Appointment(
                        appointmentId = "",
                        userId = "",
                        name = "Cek Lab",
                        date = Date(),
                        time = LocalTime.now(),
                        location = "Puskesmas Dinoyo",
                        note = "Ambil obat untuk satu bulan ke depan",
                        done = true
                    ),
                ),
                symptomList = listOf(
                    Symptom(
                        symptomId = "",
                        userId = "",
                        name = "Demam",
                        date = Date(),
                        time = LocalTime.now(),
                        note = "Demam tinggi sejak pagi, suhu tubuh 39°C"
                    ),
                ),
                weightList = listOf(
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                    Weight(
                        weightId = "",
                        userId = "",
                        date = Date(),
                        time = LocalTime.now(),
                        weight = 70,
                        height = 175,
                        note = "Berat badan bertambah"
                    ),
                ),
                onClickMedication = {},
                onClickAppointment = {},
                onClickSymptom = {},
                onClickWeight = {}
            )
        }
    }
}