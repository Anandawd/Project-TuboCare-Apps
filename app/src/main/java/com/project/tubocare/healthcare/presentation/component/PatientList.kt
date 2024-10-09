package com.project.tubocare.healthcare.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.tubocare.R
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme
import java.util.Date


@Composable
fun PatientList(
    title: String = "",
    patientList: List<User>,
    onClick: (User) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = MaterialTheme.typography.bodyLarge.fontWeight),
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(6.dp)
        )

        if (patientList.isEmpty()) {
            EmptyPatientScreen()
        }

        patientList.forEach { user ->
            PatientCard(
                patient = user,
                onClickPatient = { onClick(it) }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun EmptyPatientScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.img_404_2),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PatientListPreview() {
    TuboCareTheme {
        Column(Modifier.fillMaxSize()) {
            PatientList(
                title = "",
                patientList = emptyList(),
                onClick = {}
            )
        }
    }
}