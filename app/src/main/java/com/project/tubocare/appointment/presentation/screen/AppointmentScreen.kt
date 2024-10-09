package com.project.tubocare.appointment.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.appointment.domain.model.Appointment
import com.project.tubocare.appointment.presentation.component.AppointmentList
import com.project.tubocare.appointment.presentation.component.Calender.CustomCalender
import com.project.tubocare.appointment.presentation.event.AppointmentEvent
import com.project.tubocare.appointment.presentation.state.AppointmentState
import com.project.tubocare.core.presentation.components.PullToRefreshLazyColumn
import com.project.tubocare.ui.theme.ChangeStatusBarColor
import com.project.tubocare.ui.theme.TuboCareTheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    state: AppointmentState,
    onEvent: (AppointmentEvent) -> Unit,
    navigateToDetail: (Appointment) -> Unit,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    LaunchedEffect(Unit) { onEvent(AppointmentEvent.GetAppointments) }

    val isErrorGetAppointments = state.errorGetAppointments != null
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(innerPadding),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.title_appointment),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.padding(10.dp)
                    )
                },
            )
        },
    ) { paddingvalues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingvalues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomEnd = 30.dp,
                                bottomStart = 30.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(
                            start = 20.dp,
                            top = 0.dp,
                            end = 20.dp,
                            bottom = 20.dp
                        ),
                ) {
                    CustomCalender(state.appointmentList.data ?: emptyList())
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                AppointmentList(
                    appointmentList = state.appointmentList.data ?: emptyList(),
                    onClickCheck = { id, isChecked ->
                        onEvent(AppointmentEvent.OnCheckChanged(id, isChecked))
                    },
                    onClick = { navigateToDetail(it) }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    LaunchedEffect(key1 = isErrorGetAppointments) {
        if (isErrorGetAppointments) {
            val errorMessage = state.errorGetAppointments ?: "Terjadi kesalahan pada saat memuat jadwal"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(AppointmentEvent.ClearToast)
        }
    }
}

@Preview
@Composable
private fun AppointmentScreenPreview() {
    TuboCareTheme {
        AppointmentScreen(
            state = AppointmentState(),
            onEvent = {},
            navigateToDetail = {},
        )
    }
}