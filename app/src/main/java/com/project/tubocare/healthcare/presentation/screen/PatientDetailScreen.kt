package com.project.tubocare.healthcare.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.tubocare.R
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.appointment.presentation.state.AppointmentState
import com.project.tubocare.healthcare.presentation.component.PatientTab
import com.project.tubocare.healthcare.presentation.event.HealthcareEvent
import com.project.tubocare.healthcare.presentation.state.HealthcareState
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.profile.presentation.state.ProfileState
import com.project.tubocare.profile.presentation.util.calculateAge
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.symptom.presentation.state.SymptomState
import com.project.tubocare.ui.theme.TuboCareTheme
import com.project.tubocare.weight.domain.model.Weight
import com.project.tubocare.weight.presentation.state.WeightState
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    patientId: String,
    patientState: ProfileState,
    healthcareState: HealthcareState,
    medicationState: MedicationState,
    appointmentState: AppointmentState,
    symptomState: SymptomState,
    weightState: WeightState,
    onEvent: (HealthcareEvent) -> Unit,
    navigateToList: () -> Unit,
    navigateToDetailMedication: (Medication) -> Unit,
    navigateToDetailAppointment: (Appointment) -> Unit,
    navigateToDetailSymptom: (Symptom) -> Unit,
    navigateToDetailWeight: (Weight) -> Unit,
) {
    Log.d("PatientDetailScreen", "patientState: $patientState")
    LaunchedEffect(key1 = Unit) {
        onEvent(HealthcareEvent.GetPatientById(patientId))
    }

    LaunchedEffect(key1 = patientState.userId) {
        if (patientState.userId.isNotBlank()){
            onEvent(HealthcareEvent.GetMedications(patientState.userId))
            onEvent(HealthcareEvent.GetAppointments(patientState.userId))
            onEvent(HealthcareEvent.GetSymptoms(patientState.userId))
            onEvent(HealthcareEvent.GetWeights(patientState.userId))
        }
    }

    var age by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = patientState.bdate) {
        if (patientState.bdate != null){
            age = calculateAge(patientState.bdate)
        }
    }

    val isErrorGetDetail = healthcareState.errorGetDetailPatient != null
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.title_patient_detail),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToList() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
        },
    ) { paddingvalues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingvalues),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomEnd = 30.dp,
                            bottomStart = 30.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.background)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when {
                            patientState.gender == "Laki-laki" -> {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar_man),
                                    contentDescription = "photo profile",
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                            patientState.gender == "Perempuan" -> {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar_girl),
                                    contentDescription = "photo profile",
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = patientState.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Text(
                                text = if (age != 0) "$age Tahun" else "",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.background)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.title_gender),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier.width(120.dp)
                        )
                        Text(
                            text = ": ${patientState.gender}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.title_address),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier.width(120.dp)
                        )
                        Text(
                            text = ": ${patientState.address}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.title_phone),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier.width(120.dp)
                        )
                        Text(
                            text = ": ${patientState.phone}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                    }
                }
            }
            PatientTab(
                medicationList = medicationState.medicationList.data ?: emptyList(),
                appointmentList = appointmentState.appointmentList.data ?: emptyList(),
                symptomList = symptomState.symptomList.data ?: emptyList(),
                weightList = weightState.weightList.data ?: emptyList(),
                onClickMedication = { navigateToDetailMedication(it) },
                onClickAppointment = { navigateToDetailAppointment(it) },
                onClickSymptom = { navigateToDetailSymptom(it) },
                onClickWeight = { navigateToDetailWeight(it) }
            )
        }
    }
    LaunchedEffect(key1 = isErrorGetDetail) {
        if (isErrorGetDetail) {
            val errorMessage = healthcareState.errorGetDetailPatient ?: "Terjadi kesalahan saat memuat data pasien"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(HealthcareEvent.ClearToast)
        }
    }
}

@Preview
@Composable
private fun PatientDetailScreenPreview() {
   TuboCareTheme {
       PatientDetailScreen(
           patientId = "",
           healthcareState = HealthcareState(),
           patientState = ProfileState(
               userId = "",
               name = "John Doe",
               email = "john.doe@example.com",
               puskesmas = "Puskesmas Dinoyo",
               role = "Admin",
               bdate = Date(),
               gender = "Perempuan",
               address = "Jl. Kesehatan No. 123",
               phone = "081234567890",
               imageUrl = ""
           ),
           medicationState = MedicationState(),
           appointmentState = AppointmentState(),
           symptomState = SymptomState(),
           weightState = WeightState(),
           onEvent = {},
           navigateToList = {},
           navigateToDetailMedication = {},
           navigateToDetailAppointment = {},
           navigateToDetailSymptom = {},
           navigateToDetailWeight = {}
       )
   }
}

/*
Column(
modifier = Modifier
.fillMaxWidth()
.padding(horizontal = 20.dp, vertical = 40.dp),
horizontalAlignment = Alignment.CenterHorizontally,
verticalArrangement = Arrangement.Center
) {

}*/
