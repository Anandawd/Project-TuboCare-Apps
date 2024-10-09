package com.project.tubocare.symptom.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.tubocare.R
import com.project.tubocare.core.util.formatDateToDayMonthYear
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.ui.theme.CustomGrayBlue
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun SymptomList(
    symptomList: List<Symptom>,
    onClick: (Symptom) -> Unit
) {

    val sortedSymptomItems = symptomList.sortedByDescending { it.date }

    Column(
        modifier = Modifier.padding(horizontal = 14.dp)
    ) {
        if (symptomList.isEmpty()) {
            EmptySymptomScreen()
        }

        LazyColumn {
            items(sortedSymptomItems) { symptom ->
                Column {
                    Text(
                        text = formatDateToDayMonthYear(symptom.date),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(6.dp)
                    )
                    SymptomCard(
                        symptom = symptom,
                        onClickSymptom = { onClick(it) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
fun EmptySymptomScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.img_404),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )
        Text(
            text = "Belum ada riwayat",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tambahkan riwayat keluhan\nterlebih dahulu",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun SymptomListPreview() {

    TuboCareTheme {
        SymptomList(
            symptomList = emptyList(),
            onClick = {}

        )
    }
}