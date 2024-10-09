package com.project.tubocare.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    onNewMedicationClick: () -> Unit,
    onNewAppointmentClick: () -> Unit,
    onNewSymptomClick: () -> Unit,
    onNewWeightClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = { showBottomSheet = false },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.title_new),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomSheetItem(
                    painter = R.drawable.ic_pill_plus,
                    title = "Jadwal\nMinum Obat",
                    onClick = {
                        onNewMedicationClick()
                    }
                )
                BottomSheetItem(
                    painter = R.drawable.ic_date_plus,
                    title = "Jadwal\nKontrol Rutin",
                    onClick = {
                        onNewAppointmentClick()
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomSheetItem(
                    painter = R.drawable.ic_sick_plus,
                    title = "Riwayat\nKeluhan Dini",
                    onClick = {
                        onNewSymptomClick()
                    }
                )
                BottomSheetItem(
                    painter = R.drawable.ic_weight_plus,
                    title = "Riwayat\nBerat Badan",
                    onClick = {
                        onNewWeightClick()
                    }
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun BottomSheetItem(
    modifier: Modifier = Modifier,
    painter: Int,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape)
                .border(BorderStroke(color = Color.White, width = 2.dp), shape = CircleShape),
            onClick = { onClick() }
        ) {
            Icon(
                painter = painterResource(id = painter),
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetItemPreview() {
    TuboCareTheme {
        BottomSheetItem(
            painter = R.drawable.ic_pill_plus,
            title = "Jadwal\n Minum Obat",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun CustomBottomSheetPreview() {
    TuboCareTheme {
        CustomBottomSheet(
            onNewMedicationClick = {},
            onNewAppointmentClick = {},
            onNewSymptomClick = {},
            onNewWeightClick = {}
        )
    }
}