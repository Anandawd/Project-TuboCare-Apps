package com.project.tubocare.core.presentation.components.navigation_bar

import androidx.annotation.DrawableRes
import com.project.tubocare.R
import com.project.tubocare.core.util.MainRouteScreen

data class NavigationItem(
    val title: String,
    val route: String,
    @DrawableRes val selectedIconId: Int,
    @DrawableRes val unSelectedIconId: Int,
    val hasBadge: Boolean = false,
    val badgeCount: Int? = null
)

val list_menu = listOf(
    NavigationItem(
        title = "Home",
        route = MainRouteScreen.Medication.route,
        selectedIconId = R.drawable.ic_home_fill,
        unSelectedIconId = R.drawable.ic_home_stroke
    ),
    NavigationItem(
        title = "Appointment",
        route = MainRouteScreen.Appointment.route,
        selectedIconId = R.drawable.ic_appointment_fill,
        unSelectedIconId = R.drawable.ic_appointment_stroke
    ),
    NavigationItem(
        title = "Article",
        route = MainRouteScreen.Article.route,
        selectedIconId = R.drawable.ic_article_fill,
        unSelectedIconId = R.drawable.ic_article_stroke
    ),
    NavigationItem(
        title = "Profile",
        route = MainRouteScreen.Profile.route,
        selectedIconId = R.drawable.ic_profile_fill,
        unSelectedIconId = R.drawable.ic_profile_stroke
    )
)