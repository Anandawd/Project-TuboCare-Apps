package com.project.tubocare.core.presentation.nav_graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.tubocare.core.presentation.MainScreen
import com.project.tubocare.core.util.Graph

@Composable
fun RootNavGraph(
    startDestination: String,
) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        route = Graph.RootGraph,
        startDestination = startDestination
    ) {
        authNavGraph(navController)

        composable(
            route = Graph.MainScreenGraph,
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            MainScreen(
                rootNavController = navController,
                navigateToNewMedication = {
                    navController.navigate(Graph.NewMedicationGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToNewAppointment = {
                    navController.navigate(Graph.NewAppointmentGraph){
                        launchSingleTop = true
                    }
                },
                navigateToNewSymptom = {
                    navController.navigate(Graph.NewSymptomGraph){
                        launchSingleTop = true
                    }
                },
                navigateToNewWeight = {
                    navController.navigate(Graph.NewWeightGraph){
                        launchSingleTop = true
                    }
                }
            )
        }

        medicationNavGraph(navController)
        appointmentNavGraph(navController)
        articleNavGraph(navController)

        profileEditNavGraph(navController)
        symptomNavGraph(navController)
        weightNavGraph(navController)
        healthcareNavGraph(navController)
        aboutNavGraph(navController)
        notificationNavGraph(navController)
        medicationAll(navController)

        newMedicationNavGraph(navController)
        newAppointmentNavGraph(navController)
        newSymptomNavGraph(navController)
        newWeightNavGraph(navController)
    }
}

