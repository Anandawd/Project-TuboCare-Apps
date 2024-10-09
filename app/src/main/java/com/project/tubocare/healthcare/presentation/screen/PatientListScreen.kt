package com.project.tubocare.healthcare.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.auth.domain.model.User
import com.project.tubocare.healthcare.presentation.component.PatientList
import com.project.tubocare.healthcare.presentation.event.HealthcareEvent
import com.project.tubocare.healthcare.presentation.state.HealthcareState
import com.project.tubocare.ui.theme.CustomDarkBlue
import com.project.tubocare.ui.theme.CustomWhiteBlue
import com.project.tubocare.ui.theme.TuboCareTheme
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(
    state: HealthcareState,
    onEvent: (HealthcareEvent) -> Unit,
    navigateToDetail: (User) -> Unit,
    navigateToHome: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        onEvent(HealthcareEvent.getHealthcare)
    }

    LaunchedEffect(key1 = state.puskesmas) {
        if (state.puskesmas.isNotBlank()) {
            onEvent(HealthcareEvent.GetPatientsByPuskemas(state.puskesmas))
        }
    }

    val isErrorGetPatients = state.errorGetPatients != null
    val context = LocalContext.current

    val userName = if (state.name.isNotBlank()) "Halo,\n${state.name}" else "Halo,\nAdmin"

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp, topEnd = 0.dp, bottomEnd = 30.dp, bottomStart = 30.dp
                        )
                    )
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = userName,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                        IconButton(
                            onClick = { navigateToHome() },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = CustomWhiteBlue,
                                contentColor = CustomDarkBlue,
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_home_fill),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    PatientList(
                        title = "Daftar Pasien",
                        patientList = state.patients,
                        onClick = { navigateToDetail(it) }
                    )
                }
            }
        }
    }
    LaunchedEffect(key1 = isErrorGetPatients) {
        if (isErrorGetPatients) {
            val errorMessage = state.errorGetPatients ?: "Terjadi kesalahan saat memuat daftar pasien"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(HealthcareEvent.ClearToast)
        }
    }
}

@Preview
@Composable
private fun PatientListScreenPreview() {
    TuboCareTheme {
        PatientListScreen(
            state = HealthcareState(patients = listOf(
                User(
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
                User(
                    userId = "",
                    name = "John Doe",
                    email = "john.doe@example.com",
                    puskesmas = "Puskesmas Sehat",
                    role = "Admin",
                    bdate = Date(),
                    gender = "Laki-laki",
                    address = "Jl. Kesehatan No. 123",
                    phone = "081234567890",
                    imageUrl = ""
                ),
                User(
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
            )),
            onEvent = {},
            navigateToDetail = {},
            navigateToHome = {}
        )
    }
}