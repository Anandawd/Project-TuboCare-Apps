package com.project.tubocare.profile.presentation.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project.tubocare.R
import com.project.tubocare.core.presentation.components.CustomOutlineDatePicker
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.util.formatDateToString2
import com.project.tubocare.core.util.parseStringToDate
import com.project.tubocare.profile.presentation.event.ProfileEvent
import com.project.tubocare.profile.presentation.state.ProfileState
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    navigateToProfile: () -> Unit
) {
    val isErrorName = state.profileErrorName != null
    val isErrorAddress = state.profileErrorAddress != null
    val isErrorPhone = state.profileErrorPhone != null
    val isErrorUpdate = state.updateError != null
    val context = LocalContext.current

    var uri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = state.imageUrl) {
        if (state.imageUrl.isNotBlank()) {
            uri = Uri.parse(state.imageUrl)
        }
    }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { result ->
            result?.let {
                uri = it
                onEvent(ProfileEvent.OnImageUrlChanged(it.toString()))
            }
        }
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    title = {
                        Text(
                            text = stringResource(id = R.string.title_edit_profile),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(10.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigateToProfile()
                        }) {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // foto profile
                    when {
                        state.localImagePath.isNullOrBlank() && state.gender == "Laki-laki" -> {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_man),
                                contentDescription = "photo profile",
                                modifier = Modifier.size(140.dp)
                            )
                        }
                        state.localImagePath.isNullOrBlank() && state.gender == "Perempuan" -> {
                            Image(
                                painter = painterResource(id = R.drawable.avatar_girl),
                                contentDescription = "photo profile",
                                modifier = Modifier.size(140.dp)
                            )
                        }
                        else -> {
                            AsyncImage(
                                model = uri ?: state.localImagePath,
                                contentDescription = "photo profile",
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    TextButton(
                        onClick = {
                            singlePhotoPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.edit_photo_profile_instruction),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomOutlinedTextField(
                        value = state.name,
                        onValueChange = {
                            onEvent(ProfileEvent.OnNameChanged(it))
                        },
                        label = stringResource(id = R.string.label_name),
                        placeholder = stringResource(id = R.string.placeholder_name),
                        isError = isErrorName,
                        errorText = if (isErrorName) state.profileErrorName else null
                    )
                    CustomOutlineDatePicker(
                        value = formatDateToString2(state.bdate),
                        onDateSelected = {
                            onEvent(ProfileEvent.OnBdateChanged(parseStringToDate(it)))
                        },
                        label = stringResource(id = R.string.label_bdate),
                        placeholder = stringResource(id = R.string.placeholder_bdate),
                    )
                    CustomOutlinedTextField(
                        value = state.address,
                        onValueChange = {
                            onEvent(ProfileEvent.OnAddressChanged(it))
                        },
                        label = stringResource(id = R.string.label_address),
                        placeholder = stringResource(id = R.string.placeholder_address),
                        isSingleLine = false,
                        isError = isErrorAddress,
                        errorText = if (isErrorAddress) state.profileErrorAddress else null
                    )
                    CustomOutlinedTextField(
                        value = state.phone,
                        onValueChange = {
                            onEvent(ProfileEvent.OnPhoneChanged(it))
                        },
                        label = stringResource(id = R.string.label_phone),
                        placeholder = stringResource(id = R.string.placeholder_phone),
                        keyboardType = KeyboardType.Phone,
                        isError = isErrorPhone,
                        errorText = if (isErrorPhone) state.profileErrorPhone else null
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CustomPrimaryButton(
                        value = stringResource(id = R.string.btn_save),
                        isLoading = state.isLoading,
                        onClick = {
                            onEvent(ProfileEvent.Update)
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = isErrorUpdate) {
        if (isErrorUpdate) {
            val errorMessage = state.updateError ?: "Terjadi kesalahan tidak diketahui"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(ProfileEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = state.isSuccessUpdate) {
        if (state.isSuccessUpdate) {
            Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            navigateToProfile()
        }
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    TuboCareTheme {
        ProfileEditScreen(
            state = ProfileState(
                name = "Ananda Widiastana"
            ),
            onEvent = {},
            navigateToProfile = {}
        )
    }
}