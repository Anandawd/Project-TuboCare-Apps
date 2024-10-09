package com.project.tubocare.weight.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.ui.theme.TuboCareTheme
import com.project.tubocare.weight.domain.model.Weight
import com.project.tubocare.weight.presentation.component.CustomIMTBox
import com.project.tubocare.weight.presentation.component.WeightList
import com.project.tubocare.weight.presentation.component.circural.CustomCircularComponent
import com.project.tubocare.weight.presentation.event.WeightEvent
import com.project.tubocare.weight.presentation.state.WeightState
import com.project.tubocare.weight.presentation.util.calculateIMT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(
    state: WeightState,
    onEvent: (WeightEvent) -> Unit,
    navigateToHome: () -> Unit,
    navigateToEdit: (Weight) -> Unit,
) {
    LaunchedEffect(Unit) {
        onEvent(WeightEvent.GetWeights)
    }

    // get the latest weight dan height values
    val latestWeightData = state.weightList.data?.maxByOrNull { it.date!! }
    val latestWeight = latestWeightData?.weight ?: 0f
    val latestHeight = latestWeightData?.height ?: 0f

    // calculate IMT
    val imt = calculateIMT(latestWeight.toFloat(), latestHeight.toFloat())

    val isErrorGetWeights = state.errorGetWeights != null
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
                        text = stringResource(id = R.string.title_weight),
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.background
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.imt_title),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                CustomCircularComponent(
                    indicatorValue = imt, maxIndicatorValue = 40f
                )
                CustomIMTBox(
                    heightTitle = stringResource(id = R.string.height_box_title),
                    weightTitle = stringResource(id = R.string.weight_box_title),
                    heightValue = latestHeight.toInt(),
                    weightValue = latestWeight.toInt(),
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                WeightList(
                    weightList = state.weightList.data ?: emptyList(),
                    onClick = { navigateToEdit(it) }
                )
            }
        }
    }
    LaunchedEffect(key1 = isErrorGetWeights) {
        if (isErrorGetWeights) {
            val errorMessage = state.errorGetWeights ?: "Terjadi kesalahan pada saat memuat riwayat"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(WeightEvent.ClearToast)
        }
    }
}

@Preview
@Composable
private fun WeightScreenPreview() {
    TuboCareTheme {
        WeightScreen(
            state = WeightState(),
            onEvent = {},
            navigateToHome = {},
            navigateToEdit = {}
        )
    }
}