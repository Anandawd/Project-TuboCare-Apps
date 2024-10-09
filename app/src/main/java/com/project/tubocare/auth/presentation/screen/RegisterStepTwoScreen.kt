package com.project.tubocare.auth.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.auth.presentation.event.RegisterEvent
import com.project.tubocare.auth.presentation.state.RegisterState
import com.project.tubocare.auth.util.Constants.AVATAR_URL_1
import com.project.tubocare.auth.util.Constants.AVATAR_URL_2
import com.project.tubocare.core.presentation.components.CustomDropdown
import com.project.tubocare.core.presentation.components.CustomHeaderComponent
import com.project.tubocare.core.presentation.components.CustomOutlineDatePicker
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.presentation.components.CustomRadioButton
import com.project.tubocare.core.util.formatDateToString2
import com.project.tubocare.core.util.parseStringToDate
import com.project.tubocare.ui.theme.TuboCareTheme


@Composable
fun RegisterStepTwoScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    navigateToHome: () -> Unit,
) {
    val isErrorName = state.registerErrorName != null
    val isErrorBdate = state.registerErrorBdate != null
    val isErrorGender = state.registerErrorGender != null
    val isErrorAddress = state.registerErrorAddress != null
    val isErrorPhone = state.registerErrorPhone != null
    val isErrorPuskesmas = state.registerErrorPuskesmas != null
    val isErrorRole = state.registerErrorRole != null
    val isErrorCode = state.registerErrorCode != null

    val isSaveError = state.registerSaveError!= null
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
    ) {
        CustomHeaderComponent(value = stringResource(id = R.string.title_register_2))
        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = state.name,
            onValueChange = { onEvent(RegisterEvent.OnNameChanged(it)) },
            label = stringResource(id = R.string.label_name),
            placeholder = stringResource(id = R.string.placeholder_name),
            isError = isErrorName,
            errorText = if (isErrorName) state.registerErrorName else null
        )

        CustomOutlineDatePicker(
            value = formatDateToString2(date = state.bdate),
            label = stringResource(id = R.string.label_bdate),
            placeholder = stringResource(id = R.string.placeholder_bdate),
            onDateSelected = { onEvent(RegisterEvent.OnBdateChanged(parseStringToDate(it))) },
            isError = isErrorBdate,
            errorText = if (isErrorBdate) state.registerErrorBdate else null
        )

        CustomRadioButton(
            selectedGender = state.gender,
            onGenderSelected = { onEvent(RegisterEvent.OnGenderChanged(it)) },
            label = stringResource(id = R.string.label_gender),
            isError = isErrorGender,
            errorText = if (isErrorGender) state.registerErrorGender else null
        )

        CustomOutlinedTextField(
            value = state.address,
            onValueChange = { onEvent(RegisterEvent.OnAddressChanged(it)) },
            label = stringResource(id = R.string.label_address),
            placeholder = stringResource(id = R.string.placeholder_address),
            isSingleLine = false,
            isError = isErrorAddress,
            errorText = if (isErrorAddress) state.registerErrorAddress else null
        )

        CustomOutlinedTextField(
            value = state.phone,
            onValueChange = { onEvent(RegisterEvent.OnPhoneChanged(it)) },
            label = stringResource(id = R.string.label_phone),
            placeholder = stringResource(id = R.string.placeholder_phone),
            keyboardType = KeyboardType.Phone,
            isError = isErrorPhone,
            errorText = if (isErrorPhone) state.registerErrorPhone else null
        )

        CustomDropdown(
            listItem = stringArrayResource(id = R.array.list_puskesmas),
            selectedItem = state.puskesmas,
            onItemSelected = { onEvent(RegisterEvent.OnPuskesmasChanged(it)) },
            label = stringResource(id = R.string.label_puskesmas),
            placeholder = stringResource(id = R.string.placeholder_puskesmas),
            isError = isErrorPuskesmas,
            errorText = if (isErrorPuskesmas) state.registerErrorPuskesmas else null
        )

        CustomDropdown(
            listItem = stringArrayResource(id = R.array.list_role),
            selectedItem = state.role,
            onItemSelected = { onEvent(RegisterEvent.OnRoleChanged(it)) },
            label = stringResource(id = R.string.label_role),
            placeholder = stringResource(id = R.string.placeholder_role),
            isError = isErrorRole,
            errorText = if (isErrorRole) state.registerErrorRole else null
        )

        if (state.role == "Tenaga Kesehatan"){
            CustomOutlinedTextField(
                value = state.code,
                onValueChange = { onEvent(RegisterEvent.OnCodeChanged(it)) },
                label = stringResource(id = R.string.label_code),
                placeholder = stringResource(id = R.string.placeholder_code),
                isPassword = true,
                isError = isErrorCode,
                errorText = if (isErrorCode) state.registerErrorCode else null
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.weight(1f))
        CustomPrimaryButton(
            value = stringResource(id = R.string.btn_save),
            onClick = { onEvent(RegisterEvent.Save) },
            isLoading = state.isLoading
        )

        LaunchedEffect(key1 = isSaveError) {
            if (isSaveError) {
                val errorMessage = state.registerSaveError ?: "Gagal menyimpan data profil"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                onEvent(RegisterEvent.ClearToast)
            }
        }

        LaunchedEffect(key1 = state.isSuccessSave) {
            if (state.isSuccessSave) {
                Toast.makeText(context, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
        }
    }
}

@Preview
@Composable
private fun RegisterStepTwoScreenPreview() {
    TuboCareTheme {
        RegisterStepTwoScreen(
            state = RegisterState(),
            onEvent = {},
            navigateToHome = {}
        )
    }
}