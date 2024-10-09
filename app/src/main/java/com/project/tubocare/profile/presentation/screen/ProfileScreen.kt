package com.project.tubocare.profile.presentation.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.profile.presentation.component.CustomClickableProfileMenu
import com.project.tubocare.profile.presentation.component.CustomOutlineButtom
import com.project.tubocare.profile.presentation.component.ProfileHeader
import com.project.tubocare.profile.presentation.event.ProfileEvent
import com.project.tubocare.profile.presentation.state.ProfileState
import com.project.tubocare.profile.presentation.util.calculateAge
import com.project.tubocare.ui.theme.ChangeStatusBarColor
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToSymptom: () -> Unit,
    navigateToWeight: () -> Unit,
    navigateToAbout: () -> Unit,
    navigateToPatientList: () -> Unit,
    navigateToLogin: () -> Unit,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    /*ChangeStatusBarColor(
        color = MaterialTheme.colorScheme.primary
    )*/

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        onEvent(ProfileEvent.LoadData)
    }

    var age by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = state.bdate) {
        state.bdate?.let { age = calculateAge(it) }
    }
    Log.d("ProfileScreen", "state: $state")

    Scaffold(
        modifier = Modifier.padding(innerPadding),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.title_profile),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                },
            )
        },
    ) { paddingvalues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingvalues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(state, age)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CustomClickableProfileMenu(
                    value = stringResource(id = R.string.profile_menu_edit),
                    onClick = { navigateToEditProfile() }
                )
                HorizontalDivider(
                    modifier = Modifier.width(270.dp),
                    color = MaterialTheme.colorScheme.background
                )
                CustomClickableProfileMenu(
                    value = stringResource(id = R.string.profile_menu_symptom),
                    onClick = { navigateToSymptom() })

                HorizontalDivider(
                    modifier = Modifier.width(270.dp),
                    color = MaterialTheme.colorScheme.background
                )
                CustomClickableProfileMenu(
                    value = stringResource(id = R.string.profile_menu_weight),
                    onClick = { navigateToWeight() }
                )
                HorizontalDivider(
                    modifier = Modifier.width(270.dp),
                    color = MaterialTheme.colorScheme.background
                )
                if (state.role == "Tenaga Kesehatan"){
                    CustomClickableProfileMenu(
                        value = stringResource(id = R.string.profile_menu_patients),
                        onClick = { navigateToPatientList() }
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(270.dp),
                        color = MaterialTheme.colorScheme.background
                    )
                }
                CustomClickableProfileMenu(
                    value = stringResource(id = R.string.profile_menu_about),
                    onClick = { navigateToAbout() }
                )

                CustomOutlineButtom(
                    value = stringResource(id = R.string.btn_logout),
                    onClick = {
                        onEvent(ProfileEvent.Logout)
                    }
                )
            }
        }

        LaunchedEffect(key1 = state.isSuccessLogout) {
            if (state.isSuccessLogout) {
                Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    TuboCareTheme {
        ProfileScreen(
            state = ProfileState(
                role = "Tenaga Kesehatan"
            ),
            onEvent = {},
            navigateToEditProfile = {},
            navigateToSymptom = {},
            navigateToWeight = {},
            navigateToAbout = {},
            navigateToPatientList = {},
            navigateToLogin = {}
        )
    }
}