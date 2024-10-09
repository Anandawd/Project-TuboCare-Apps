package com.project.tubocare.core.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.tubocare.R
import com.project.tubocare.core.presentation.components.BottomSheetItem
import com.project.tubocare.core.presentation.components.navigation_bar.CustomBottomNavigation
import com.project.tubocare.core.presentation.components.navigation_bar.list_menu
import com.project.tubocare.core.presentation.nav_graph.MainNavGraph
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    navigateToNewMedication: () -> Unit,
    navigateToNewAppointment: () -> Unit,
    navigateToNewWeight: () -> Unit,
    navigateToNewSymptom: () -> Unit,
) {
    val homeNavController: NavHostController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(
                items = list_menu,
                currentRoute = currentRoute,
                onItemClick = { currentNavigationItem ->
                    homeNavController.navigate(currentNavigationItem.route){
                        homeNavController.graph.startDestinationRoute?.let { startDestinationRoute ->
                            popUpTo(startDestinationRoute){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                },
                modifier = Modifier
                    .offset(y = 50.dp)
                    .size(62.dp)
                    .border(
                        border = BorderStroke(
                            1.dp,
                            color = MaterialTheme.colorScheme.primary
                        ), shape = CircleShape
                    ),
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus_blue),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
        MainNavGraph(
            rootNavController = rootNavController,
            homeNavController = homeNavController,
            innerPadding = innerPadding
        )

        if (showBottomSheet) {
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
                                showBottomSheet = false
                                navigateToNewMedication()
                            }
                        )
                        BottomSheetItem(
                            painter = R.drawable.ic_date_plus,
                            title = "Jadwal\nKontrol Rutin",
                            onClick = {
                                showBottomSheet = false
                                navigateToNewAppointment()
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
                                showBottomSheet = false
                                navigateToNewSymptom()
                            }
                        )
                        BottomSheetItem(
                            painter = R.drawable.ic_weight_plus,
                            title = "Riwayat\nBerat Badan",
                            onClick = {
                                showBottomSheet = false
                                navigateToNewWeight()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}


@Preview
@Composable
private fun CustomNavigationScreenPreview() {
    TuboCareTheme {
        MainScreen(
            rootNavController = rememberNavController(),
            navigateToNewMedication = {},
            navigateToNewAppointment = {},
            navigateToNewWeight = {},
            navigateToNewSymptom = {}
        )
    }
}
