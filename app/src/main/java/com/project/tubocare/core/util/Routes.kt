package com.project.tubocare.core.util

object Graph {
    const val RootGraph = "rootGraph"
    const val AuthGraph = "authGraph"
    const val MainScreenGraph = "mainScreenGraph"
    const val MedicationGraph = "medicationGraph"
    const val AppointmentGraph = "appointmentGraph"
    const val WeightGraph = "weightGraph"
    const val SymptomGraph = "symptomGraph"
    const val ArticleGraph = "articleGraph"
    const val ProfileEditGraph = "profileEditGraph"
    const val AboutGraph = "aboutGraph"
    const val HealthcareGraph = "healthcareGraph"
    const val NotificationGraph = "notificationGraph"
    const val MedicationAllGraph = "medicationAll"

    const val NewMedicationGraph = "newMedicationGraph"
    const val NewAppointmentGraph = "newAppointmentGraph"
    const val NewSymptomGraph = "newSymptomGraph"
    const val NewWeightGraph = "newWeightGraph"
}
sealed class AuthRouteScreen(val route: String){
    object OnBoarding: AuthRouteScreen("onBoarding")
    object Login: AuthRouteScreen("login")
    object RegisterStepOne: AuthRouteScreen("registerStepOne")
    object RegisterStepTwo: AuthRouteScreen("registerStepTwo")
}

sealed class MainRouteScreen(val route: String){
    object Medication: MainRouteScreen("medication")
    object Appointment: MainRouteScreen("appointment")
    object Article: MainRouteScreen("article")
    object Profile: MainRouteScreen("profile")
}

sealed class MedicationRouteScreen(val route: String){
    object MedicationDetail: MedicationRouteScreen("medicationDetail")
    object MedicationEdit: MedicationRouteScreen("medicationEdit")
}

sealed class AppointmentRouteScreen(val route: String){
    object AppointmentDetail: AppointmentRouteScreen("appointmentDetail")
    object AppointmentEdit: AppointmentRouteScreen("appointmentEdit")
}

sealed class WeightRouteScreen(val route: String){
    object Weight: WeightRouteScreen("weight")
    object WeightEdit: WeightRouteScreen("weightEdit")
}

sealed class SymptomRouteScreen(val route: String){
    object Symptom: SymptomRouteScreen("symptom")
    object SymptomEdit: SymptomRouteScreen("symptomEdit")
}

sealed class ArticleRouteScreen(val route: String){
    object ArticleDetail: ArticleRouteScreen("articleDetail")
}

sealed class ProfileRouteScreen(val route: String){
    object ProfileEdit: ProfileRouteScreen("profileEdit")
    object Symptom: ProfileRouteScreen("symptom")
    object Weight: ProfileRouteScreen("weight")
    object Healthcare: ProfileRouteScreen("healthcare")
    object About: ProfileRouteScreen("about")
}

sealed class HealthcareRouteScreen(val route: String){
    object PatientList: HealthcareRouteScreen("patientList")
    object PatientDetail: HealthcareRouteScreen("patientDetail")
    object MedicationReport: HealthcareRouteScreen("medicationReport")
}

sealed class NotificationRouteScreen(val route: String){
    object Notification: NotificationRouteScreen("notification")
}

sealed class MedicationAllRoute(val route: String){
    object MedicationList: MedicationAllRoute("medicationList")
}

sealed class NewMedicationRouteScreen(val route: String){
    object NewMedicationStepOne: NewMedicationRouteScreen("newMedicationStepOne")
    object NewMedicationStepTwo: NewMedicationRouteScreen("newMedicationStepTwo")
    object NewMedicationStepThree: NewMedicationRouteScreen("newMedicationStepThree")
}

sealed class NewAppointmentRouteScreen(val route: String){
    object NewAppointment: NewAppointmentRouteScreen("newAppointment")
}

sealed class NewWeightRouteScreen(val route: String){
    object NewWeight: NewWeightRouteScreen("newWeight")
}

sealed class NewSymptomRouteScreen(val route: String){
    object NewSymptom: NewSymptomRouteScreen("newSymptom")
}
