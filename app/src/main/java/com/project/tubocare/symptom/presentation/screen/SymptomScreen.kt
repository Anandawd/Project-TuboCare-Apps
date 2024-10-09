package com.project.tubocare.symptom.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.symptom.domain.model.Symptom
import com.project.tubocare.symptom.presentation.component.SymptomList
import com.project.tubocare.symptom.presentation.event.SymptomEvent
import com.project.tubocare.symptom.presentation.state.SymptomState
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomScreen(
    state: SymptomState,
    onEvent: (SymptomEvent) -> Unit,
    navigateToHome: () -> Unit,
    navigateToEdit: (Symptom) -> Unit,
) {
    LaunchedEffect(Unit) {
        onEvent(SymptomEvent.GetSymptoms)
    }

    val isErrorGetSymptoms = state.errorGetSymptoms != null
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.title_symptom),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateToHome() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingvalues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingvalues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background
            )
            Column(modifier = Modifier.padding(vertical = 20.dp)) {
                SymptomList(
                    symptomList = state.symptomList.data ?: emptyList(),
                    onClick = { navigateToEdit(it) }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
    LaunchedEffect(key1 = isErrorGetSymptoms) {
        if (isErrorGetSymptoms) {
            val errorMessage = state.errorGetSymptoms ?: "Terjadi kesalahan pada saat memuat riwayat"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(SymptomEvent.ClearToast)
        }
    }
}

@Preview
@Composable
private fun SymptomScreenPreview() {
    TuboCareTheme {
        SymptomScreen(
            state = SymptomState(),
            onEvent = {},
            navigateToHome = {},
            navigateToEdit = {}
        )
    }
}