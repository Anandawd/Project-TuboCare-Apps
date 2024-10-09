package com.project.tubocare.core.presentation.nav_graph

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.project.tubocare.appointment.presentation.AppointmentViewModel
import com.project.tubocare.appointment.presentation.screen.AppointmentDetailScreen
import com.project.tubocare.appointment.presentation.screen.AppointmentEditScreen
import com.project.tubocare.appointment.presentation.screen.NewAppointmentScreen
import com.project.tubocare.article.presentation.ArticleViewModel
import com.project.tubocare.article.presentation.screen.ArticleDetailScreen
import com.project.tubocare.auth.presentation.LoginViewModel
import com.project.tubocare.auth.presentation.RegisterViewModel
import com.project.tubocare.auth.presentation.screen.LoginScreen
import com.project.tubocare.auth.presentation.screen.RegisterStepOneScreen
import com.project.tubocare.auth.presentation.screen.RegisterStepTwoScreen
import com.project.tubocare.core.presentation.onboarding.OnBoardingScreen
import com.project.tubocare.core.presentation.onboarding.OnBoardingViewModel
import com.project.tubocare.core.util.AppointmentRouteScreen
import com.project.tubocare.core.util.ArticleRouteScreen
import com.project.tubocare.core.util.AuthRouteScreen
import com.project.tubocare.core.util.Graph
import com.project.tubocare.core.util.HealthcareRouteScreen
import com.project.tubocare.core.util.MedicationAllRoute
import com.project.tubocare.core.util.MedicationRouteScreen
import com.project.tubocare.core.util.NewAppointmentRouteScreen
import com.project.tubocare.core.util.NewMedicationRouteScreen
import com.project.tubocare.core.util.NewSymptomRouteScreen
import com.project.tubocare.core.util.NewWeightRouteScreen
import com.project.tubocare.core.util.NotificationRouteScreen
import com.project.tubocare.core.util.ProfileRouteScreen
import com.project.tubocare.core.util.SymptomRouteScreen
import com.project.tubocare.core.util.WeightRouteScreen
import com.project.tubocare.healthcare.presentation.HealthcareViewModel
import com.project.tubocare.healthcare.presentation.screen.MedicationReportScreen
import com.project.tubocare.healthcare.presentation.screen.PatientDetailScreen
import com.project.tubocare.healthcare.presentation.screen.PatientListScreen
import com.project.tubocare.medication.presentation.MedicationViewModel
import com.project.tubocare.medication.presentation.screen.MedicationAllScreen
import com.project.tubocare.medication.presentation.screen.MedicationDetailScreen
import com.project.tubocare.medication.presentation.screen.MedicationEditScreen
import com.project.tubocare.medication.presentation.screen.NewMedicationScreen
import com.project.tubocare.notification.presentation.NotificationViewModel
import com.project.tubocare.notification.presentation.screen.NotificationScreen
import com.project.tubocare.profile.presentation.ProfileViewModel
import com.project.tubocare.profile.presentation.screen.AboutScreen
import com.project.tubocare.profile.presentation.screen.ProfileEditScreen
import com.project.tubocare.symptom.presentation.SymptomViewModel
import com.project.tubocare.symptom.presentation.screen.NewSymptomScreen
import com.project.tubocare.symptom.presentation.screen.SymptomEditScreen
import com.project.tubocare.symptom.presentation.screen.SymptomScreen
import com.project.tubocare.weight.presentation.WeightViewModel
import com.project.tubocare.weight.presentation.screen.NewWeightScreen
import com.project.tubocare.weight.presentation.screen.WeightEditScreen
import com.project.tubocare.weight.presentation.screen.WeightScreen


fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AuthGraph,
        startDestination = AuthRouteScreen.OnBoarding.route
    ) {
        composable(route = AuthRouteScreen.OnBoarding.route) {
            val viewModel: OnBoardingViewModel = hiltViewModel()
            OnBoardingScreen(
                onEvent = viewModel::onEvent,
                navigateToLogin = {
                    navController.navigate(AuthRouteScreen.Login.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // login route
        composable(route = AuthRouteScreen.Login.route) {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val state = loginViewModel.loginState
            LoginScreen(
                state = state,
                onEvent = loginViewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.AuthGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateToRegister = {
                    navController.navigate(AuthRouteScreen.RegisterStepOne.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = AuthRouteScreen.RegisterStepOne.route) {
            val registerViewModel = hiltViewModel<RegisterViewModel>()
            val state = registerViewModel.registerState
            RegisterStepOneScreen(
                state = state,
                onEvent = registerViewModel::onEvent,
                navigateToRegisterTwo = {
                    navController.navigate(AuthRouteScreen.RegisterStepTwo.route) {
                        launchSingleTop = true
                    }
                },
                navigateToLogin = {
                    navController.navigate(AuthRouteScreen.Login.route) {
                        launchSingleTop = true
                        popUpTo(AuthRouteScreen.RegisterStepOne.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AuthRouteScreen.RegisterStepTwo.route) {
            val registerViewModel = hiltViewModel<RegisterViewModel>()
            val state = registerViewModel.registerState
            RegisterStepTwoScreen(
                state = state,
                onEvent = registerViewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.AuthGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.medicationNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.MedicationGraph,
        startDestination = MedicationRouteScreen.MedicationDetail.route
    ) {
        composable(
            route = MedicationRouteScreen.MedicationDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val medicationViewModel = hiltViewModel<MedicationViewModel>()
            val state = medicationViewModel.medicationState
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>("medication")
                ?.let { medicationId ->
                    MedicationDetailScreen(
                        medicationId = medicationId,
                        state = state,
                        onEvent = medicationViewModel::onEvent,
                        navigateToEdit = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "medicationId",
                                medicationId
                            )
                            navController.navigate(route = MedicationRouteScreen.MedicationEdit.route) {
                                launchSingleTop = true
                            }
                        },
                        navigateToHome = {
                            navController.navigate(Graph.MainScreenGraph) {
                                launchSingleTop = true
                                popUpTo(Graph.MedicationGraph) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
        }
        composable(
            route = MedicationRouteScreen.MedicationEdit.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val medicationViewModel = hiltViewModel<MedicationViewModel>()
            val state = medicationViewModel.medicationState
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>("medicationId")
                ?.let { medicationId ->
                    MedicationEditScreen(
                        medicationId = medicationId,
                        state = state,
                        onEvent = medicationViewModel::onEvent,
                        navigateToDetail = {
                            navController.navigate(MedicationRouteScreen.MedicationDetail.route) {
                                launchSingleTop = true
                                popUpTo(MedicationRouteScreen.MedicationEdit.route) {
                                    inclusive = true
                                }
                            }
                        },
                        navigateToHome = {
                            navController.navigate(Graph.MainScreenGraph) {
                                launchSingleTop = true
                                popUpTo(Graph.MedicationGraph) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
        }
    }
}

fun NavGraphBuilder.appointmentNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AppointmentGraph,
        startDestination = AppointmentRouteScreen.AppointmentDetail.route
    ) {
        composable(
            route = AppointmentRouteScreen.AppointmentDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<AppointmentViewModel>()
            val state = viewModel.appointmentState
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>("appointment")
                ?.let { appointmentId ->
                    AppointmentDetailScreen(
                        appointmentId = appointmentId,
                        state = state,
                        onEvent = viewModel::onEvent,
                        navigateToEdit = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "appointmentId",
                                appointmentId
                            )
                            navController.navigate(AppointmentRouteScreen.AppointmentEdit.route) {
                                launchSingleTop = true
                            }
                        },
                        navigateToHome = {
                            navController.navigate(Graph.MainScreenGraph) {
                                launchSingleTop = true
                                popUpTo(Graph.AppointmentGraph) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
        }
        composable(
            route = AppointmentRouteScreen.AppointmentEdit.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<AppointmentViewModel>()
            val state = viewModel.appointmentState
            navController.previousBackStackEntry?.savedStateHandle?.get<String>("appointmentId")
                ?.let { appointmentId ->
                    AppointmentEditScreen(
                        appointmentId = appointmentId,
                        state = state,
                        onEvent = viewModel::onEvent,
                        navigateToDetail = {
                            navController.navigate(AppointmentRouteScreen.AppointmentDetail.route) {
                                launchSingleTop = true
                                popUpTo(AppointmentRouteScreen.AppointmentEdit.route) {
                                    inclusive = true
                                }
                            }
                        },
                        navigateToHome = {
                            navController.navigate(Graph.MainScreenGraph) {
                                launchSingleTop = true
                                popUpTo(Graph.AppointmentGraph) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
        }
    }
}

fun NavGraphBuilder.articleNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.ArticleGraph,
        startDestination = ArticleRouteScreen.ArticleDetail.route
    ) {
        composable(
            route = ArticleRouteScreen.ArticleDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<ArticleViewModel>()
            val state = viewModel.state
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>("article")
                ?.let { articleId ->
                    ArticleDetailScreen(
                        id = articleId,
                        state = state,
                        onEvent = viewModel::onEvent,
                        navigateToHome = {
                            navController.popBackStack()
                        }
                    )
                }
        }
    }
}

fun NavGraphBuilder.weightNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.WeightGraph,
        startDestination = WeightRouteScreen.Weight.route
    ) {
        composable(
            route = WeightRouteScreen.Weight.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<WeightViewModel>()
            val state = viewModel.weightState
            WeightScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.WeightGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateToEdit = { weight ->
                    navController.currentBackStackEntry?.savedStateHandle?.set<String?>(
                        "weightId",
                        weight.weightId
                    )
                    navController.navigate(WeightRouteScreen.WeightEdit.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = WeightRouteScreen.WeightEdit.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<WeightViewModel>()
            val state = viewModel.weightState
            navController.previousBackStackEntry?.savedStateHandle?.get<String>("weightId")
                ?.let { weightId ->
                    WeightEditScreen(
                        weightId = weightId,
                        state = state,
                        onEvent = viewModel::onEvent,
                        navigateToList = {
                            navController.navigate(WeightRouteScreen.Weight.route) {
                                launchSingleTop = true
                                popUpTo(WeightRouteScreen.WeightEdit.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
        }
    }
}

fun NavGraphBuilder.symptomNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.SymptomGraph,
        startDestination = SymptomRouteScreen.Symptom.route
    ) {
        composable(
            route = SymptomRouteScreen.Symptom.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<SymptomViewModel>()
            val state = viewModel.symptomState
            SymptomScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.SymptomGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateToEdit = { symptom ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "symptomId",
                        symptom.symptomId
                    )
                    navController.navigate(SymptomRouteScreen.SymptomEdit.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = SymptomRouteScreen.SymptomEdit.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
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
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<SymptomViewModel>()
            val state = viewModel.symptomState
            navController.previousBackStackEntry?.savedStateHandle?.get<String>("symptomId")
                ?.let { symptomId ->
                    SymptomEditScreen(
                        symptomId = symptomId,
                        state = state,
                        onEvent = viewModel::onEvent,
                        navigateToList = {
                            navController.navigate(SymptomRouteScreen.Symptom.route) {
                                launchSingleTop = true
                                popUpTo(SymptomRouteScreen.SymptomEdit.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
        }
    }
}

fun NavGraphBuilder.profileEditNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.ProfileEditGraph,
        startDestination = ProfileRouteScreen.ProfileEdit.route
    ) {
        composable(
            route = ProfileRouteScreen.ProfileEdit.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            val state = profileViewModel.profileState
            ProfileEditScreen(
                state = state,
                onEvent = profileViewModel::onEvent,
                navigateToProfile = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.ProfileEditGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.healthcareNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.HealthcareGraph,
        startDestination = HealthcareRouteScreen.PatientList.route
    ) {
        composable(
            route = HealthcareRouteScreen.PatientList.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<HealthcareViewModel>()
            val healthcareState by viewModel.healthcareState.collectAsState()
            PatientListScreen(
                state = healthcareState,
                onEvent = viewModel::onEvent,
                navigateToDetail = { patient ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "patientId",
                        patient.userId
                    )
                    navController.navigate(HealthcareRouteScreen.PatientDetail.route) {
                        launchSingleTop = true
                    }
                },
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.HealthcareGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = HealthcareRouteScreen.PatientDetail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<HealthcareViewModel>()
            val healthcareState by viewModel.healthcareState.collectAsState()
            val patientState by viewModel.profileState.collectAsState()
            val medicationState = viewModel.medicationState
            val appointmentState = viewModel.appointmentState
            val symptomState = viewModel.symptomState
            val weightState = viewModel.weightState
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>("patientId")
                ?.let { patientId ->
                    PatientDetailScreen(
                        patientId = patientId,
                        patientState = patientState,
                        healthcareState = healthcareState,
                        medicationState = medicationState,
                        appointmentState = appointmentState,
                        symptomState = symptomState,
                        weightState = weightState,
                        onEvent = viewModel::onEvent,
                        navigateToList = {
                            navController.navigate(HealthcareRouteScreen.PatientList.route) {
                                launchSingleTop = true
                                popUpTo(HealthcareRouteScreen.PatientDetail.route) {
                                    inclusive = true
                                }
                            }
                        },
                        navigateToDetailMedication = { medication ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "medicationId",
                                medication.medicationId
                            )
                            navController.navigate(HealthcareRouteScreen.MedicationReport.route) {
                                launchSingleTop = true
                            }
                        },
                        navigateToDetailAppointment = {},
                        navigateToDetailSymptom = {},
                        navigateToDetailWeight = {}
                    )
                }
        }
        composable(
            route = HealthcareRouteScreen.MedicationReport.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<HealthcareViewModel>()
            val medicationState = viewModel.medicationState
            navController.previousBackStackEntry?.savedStateHandle?.get<String?>("medicationId")
                ?.let { medicationId ->
                    MedicationReportScreen(
                        id = medicationId,
                        state = medicationState,
                        onEvent = viewModel::onEvent,
                        navigateToDetailPatient = {
                            navController.navigate(HealthcareRouteScreen.PatientDetail.route) {
                                launchSingleTop = true
                                popUpTo(HealthcareRouteScreen.MedicationReport.route) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
        }
    }
}

fun NavGraphBuilder.aboutNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AboutGraph,
        startDestination = ProfileRouteScreen.About.route
    ) {
        composable(
            route = ProfileRouteScreen.About.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            AboutScreen(
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.AboutGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.notificationNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.NotificationGraph,
        startDestination = NotificationRouteScreen.Notification.route
    ) {
        composable(
            route = NotificationRouteScreen.Notification.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val notificationViewModel = hiltViewModel<NotificationViewModel>()
            val state = notificationViewModel.notificationState
            NotificationScreen(
                state = state,
                onEvent = notificationViewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.NotificationGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.medicationAll(navController: NavHostController) {
    navigation(
        route = Graph.MedicationAllGraph,
        startDestination = MedicationAllRoute.MedicationList.route
    ) {
        composable(
            route = MedicationAllRoute.MedicationList.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<MedicationViewModel>()
            val state = viewModel.medicationState
            MedicationAllScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToDetail = { medication ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "medication",
                        medication.medicationId
                    )
                    navController.navigate(route = Graph.MedicationGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.MedicationAllGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.newMedicationNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.NewMedicationGraph,
        startDestination = NewMedicationRouteScreen.NewMedicationStepOne.route
    ) {
        composable(
            route = NewMedicationRouteScreen.NewMedicationStepOne.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val medicationViewModel = hiltViewModel<MedicationViewModel>()
            val state = medicationViewModel.medicationState
            NewMedicationScreen(
                state = state,
                onEvent = medicationViewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.NewMedicationGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.newAppointmentNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.NewAppointmentGraph,
        startDestination = NewAppointmentRouteScreen.NewAppointment.route
    ) {
        composable(
            route = NewAppointmentRouteScreen.NewAppointment.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<AppointmentViewModel>()
            val state = viewModel.appointmentState
            NewAppointmentScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.NewAppointmentGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.newSymptomNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.NewSymptomGraph,
        startDestination = NewSymptomRouteScreen.NewSymptom.route
    ) {
        composable(
            route = NewSymptomRouteScreen.NewSymptom.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<SymptomViewModel>()
            val state = viewModel.symptomState
            NewSymptomScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.NewSymptomGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.newWeightNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.NewWeightGraph,
        startDestination = NewWeightRouteScreen.NewWeight.route
    ) {
        composable(
            route = NewWeightRouteScreen.NewWeight.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(800)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(800)
                )
            },
        ) {
            val viewModel = hiltViewModel<WeightViewModel>()
            val state = viewModel.weightState
            NewWeightScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToHome = {
                    navController.navigate(Graph.MainScreenGraph) {
                        launchSingleTop = true
                        popUpTo(Graph.NewWeightGraph) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}