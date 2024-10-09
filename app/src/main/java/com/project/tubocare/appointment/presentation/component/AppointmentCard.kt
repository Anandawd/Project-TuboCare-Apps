package com.project.tubocare.appointment.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.core.util.getAppointmentLabel
import com.project.tubocare.ui.theme.TuboCareTheme
import java.time.LocalTime
import java.util.Date

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onClickCheck: (String, Boolean) -> Unit,
    onClickAppointment: (Appointment) -> Unit,
    readOnly: Boolean = false
) {

    val checkedState = remember { mutableStateOf(appointment.done ?: false) }

    val label = if (appointment.date != null && appointment.time != null) getAppointmentLabel(
        appointment.date,
        appointment.time
    ) else ""

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(70.dp)
            .clickable { onClickAppointment(appointment) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
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
                        painter = painterResource(id = R.drawable.ic_date_blue),
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
                        text = appointment.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = label,
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
                                if (!readOnly) {
                                    checkedState.value = it
                                    onClickCheck(appointment.appointmentId, it)
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
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomMedicationCardPreview() {

    TuboCareTheme {
        Box(modifier = Modifier.padding(20.dp)) {
            AppointmentCard(
                appointment = Appointment(
                    appointmentId = "",
                    userId = "",
                    name = "Pengambilan Obat",
                    date = Date(),
                    time = LocalTime.now(),
                    location = "Puskesmas Dinoyo",
                    note = "Ambil obat untuk satu bulan ke depan",
                    done = true
                ),
                onClickCheck = { _, _ -> },
                onClickAppointment = {}
            )
        }
    }
}