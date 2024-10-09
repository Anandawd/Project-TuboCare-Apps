package com.project.tubocare.core.presentation.nav_graph

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.*
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.tubocare.appointment.presentation.AppointmentViewModel
import com.project.tubocare.appointment.presentation.ArticleScreen
import com.project.tubocare.appointment.presentation.screen.AppointmentScreen
import com.project.tubocare.article.presentation.ArticleViewModel
import com.project.tubocare.core.util.ArticleRouteScreen
import com.project.tubocare.core.util.Graph
import com.project.tubocare.core.util.MainRouteScreen
import com.project.tubocare.medication.presentation.MedicationViewModel
import com.project.tubocare.medication.presentation.screen.MedicationScreen
import com.project.tubocare.profile.presentation.ProfileViewModel
import com.project.tubocare.profile.presentation.screen.ProfileScreen

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = homeNavController,
        route = Graph.MainScreenGraph,
        startDestination = MainRouteScreen.Medication.route,
    ) {
        composable(
            route = MainRouteScreen.Medication.route,
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800)
                )
            },
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800)
                )
            }
        ) {
            val medicationViewModel = hiltViewModel<MedicationViewModel>()
            val state = medicationViewModel.medicationState
            MedicationScreen(
                state = state,
                onEvent = medicationViewModel::onEvent,
                navigateToNotification = {
                    rootNavController.navigate(route = Graph.NotificationGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToAll = {
                    rootNavController.navigate(route = Graph.MedicationAllGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToDetail = { medication ->
                    rootNavController.currentBackStackEntry?.savedStateHandle?.set("medication", medication.medicationId)
                    rootNavController.navigate(route = Graph.MedicationGraph) {
                        launchSingleTop = true
                    }
                },
                innerPadding = innerPadding
            )
        }
        composable(
            route = MainRouteScreen.Appointment.route,
            exitTransition = {
                when (targetState.destination.route) {
                    MainRouteScreen.Article.route -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(800)
                    )
                    MainRouteScreen.Profile.route -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(800)
                    )
                    else -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(800)
                    )
                }
            },
            enterTransition = {
                when (initialState.destination.route) {
                    MainRouteScreen.Medication.route -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(800)
                    )

                    else -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(800)
                    )
                }
            }
        ) {
            val viewModel = hiltViewModel<AppointmentViewModel>()
            val state = viewModel.appointmentState
            AppointmentScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToDetail = { appointment ->
                    rootNavController.currentBackStackEntry?.savedStateHandle?.set("appointment", appointment.appointmentId)
                    rootNavController.navigate(route = Graph.AppointmentGraph) {
                        launchSingleTop = true
                    }
                },
                innerPadding = innerPadding
            )
        }
        composable(
            route = MainRouteScreen.Article.route,
            exitTransition = {
                when (targetState.destination.route) {
                    MainRouteScreen.Profile.route -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(800)
                    )
                    Graph.ArticleGraph -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(800)
                    )
                    else -> slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(800)
                    )
                }
            },
            enterTransition = {
                when (initialState.destination.route) {
                    MainRouteScreen.Appointment.route -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(800)
                    )
                    MainRouteScreen.Medication.route -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(800)
                    )
                    else -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(800)
                    )
                }
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val articleViewModel = hiltViewModel<ArticleViewModel>()
            val state = articleViewModel.state
            ArticleScreen(
                state = state,
                onEvent = articleViewModel::onEvent,
                navigateToDetail = { article ->
                    rootNavController.currentBackStackEntry?.savedStateHandle?.set("article", article.documentId)
                    rootNavController.navigate(Graph.ArticleGraph) {
                        launchSingleTop = true
                    }
                },
                innerPadding = innerPadding
            )
        }
        composable(
            route = MainRouteScreen.Profile.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(800)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(800)
                )
            }
        ) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            val state = profileViewModel.profileState
            ProfileScreen(
                state = state,
                onEvent = profileViewModel::onEvent,
                navigateToEditProfile = {
                    rootNavController.navigate(Graph.ProfileEditGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToWeight = {
                    rootNavController.navigate(Graph.WeightGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToSymptom = {
                    rootNavController.navigate(Graph.SymptomGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToPatientList = {
                    rootNavController.navigate(Graph.HealthcareGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToAbout = {
                    rootNavController.navigate(Graph.AboutGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToLogin = {
                    rootNavController.navigate(Graph.AuthGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.MainScreenGraph) {
                            inclusive = true
                        }
                    }
                },
                innerPadding = innerPadding
            )
        }
    }
}