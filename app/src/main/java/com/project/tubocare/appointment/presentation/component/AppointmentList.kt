package com.project.tubocare.appointment.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.core.util.formatDateToDayMonthYear
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun AppointmentList(
    modifier: Modifier = Modifier,
    appointmentList: List<Appointment>,
    onClickCheck: (String, Boolean) -> Unit,
    onClick: (Appointment) -> Unit,
    readOnly: Boolean = false
) {

    val sortedAppointmentItems = appointmentList.sortedBy { it.date }

    Column(
        modifier = modifier.padding(horizontal = 14.dp)
    ) {
        if (appointmentList.isEmpty()) {
            EmptyAppointmentScreen()
        }

        sortedAppointmentItems.forEach { appointment ->
            Column {
                Text(
                    text = formatDateToDayMonthYear(appointment.date),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = MaterialTheme.typography.bodyLarge.fontWeight),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(6.dp)
                )
                AppointmentCard(
                    appointment = appointment,
                    onClickCheck = { id, isChecked -> onClickCheck(id, isChecked) },
                    onClickAppointment = { onClick(it) },
                    readOnly = readOnly
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun EmptyAppointmentScreen() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        modifier = Modifier
            .padding(6.dp),
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
                text = "Jadwal kontrol tidak tersedia",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = MaterialTheme.typography.bodyMedium.fontWeight),
                color = CustomGrayBlue,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppointmentListPreview() {

    TuboCareTheme {
        AppointmentList(
            appointmentList = emptyList(),
            onClickCheck = { _, _ -> },
            onClick = {}

        )
    }
}