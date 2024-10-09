package com.project.tubocare.healthcare.presentation.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.tubocare.R
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.util.formatLocalTimeToString
import com.project.tubocare.medication.presentation.component.CheckListStatus
import com.project.tubocare.medication.presentation.util.getDayOfWeekInIndonesian
import com.project.tubocare.profile.presentation.util.calculateAge
import com.project.tubocare.ui.theme.TuboCareTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Composable
fun PatientCard(
    patient: User,
    onClickPatient: (User) -> Unit
) {

    var age by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = patient.bdate) {
        if (patient.bdate != null){
            age = calculateAge(patient.bdate)
        }
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        modifier = Modifier
            .padding(6.dp)
            .clickable { onClickPatient(patient) },
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
                when {
                    patient.gender == "Laki-laki" -> {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_man),
                            contentDescription = "photo profile",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    patient.gender == "Perempuan" -> {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_girl),
                            contentDescription = "photo profile",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = patient.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "$age Tahun",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                CustomPrimaryButton(
                    value = stringResource(id = R.string.btn_see),
                    onClick = { onClickPatient(patient) },
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PatientCardPreview() {
    TuboCareTheme {
        PatientCard(
            patient = User(
                userId = "",
                name = "John Doe",
                email = "john.doe@example.com",
                puskesmas = "Puskesmas Sehat",
                role = "Admin",
                bdate = Date(),
                gender = "Perempuan",
                address = "Jl. Kesehatan No. 123",
                phone = "081234567890",
                imageUrl = ""
            ),
            onClickPatient = {}
        )
    }
}