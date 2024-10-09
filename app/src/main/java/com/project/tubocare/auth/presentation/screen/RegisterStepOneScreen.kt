package com.project.tubocare.auth.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.auth.presentation.event.RegisterEvent
import com.project.tubocare.auth.presentation.state.RegisterState
import com.project.tubocare.core.presentation.components.CustomClickableAnnotatedText
import com.project.tubocare.core.presentation.components.CustomDivider
import com.project.tubocare.core.presentation.components.CustomHeaderComponent
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.presentation.components.CustomSecondaryButton
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun RegisterStepOneScreen(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    navigateToRegisterTwo: () -> Unit,
    navigateToLogin: () -> Unit
) {
    val isErrorEmail = state.registerErrorEmail != null
    val isErrorPassword = state.registerErrorPassword != null
    val isErrorConfirm = state.registerErrorConfirm != null
    val isError = state.registerError!= null
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
    ) {
        CustomHeaderComponent(value = stringResource(id = R.string.title_register_1))
        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(RegisterEvent.OnEmailChanged(it)) },
            label = stringResource(id = R.string.label_email),
            placeholder = stringResource(id = R.string.placeholder_email),
            isError = isErrorEmail,
            errorText = if (isErrorEmail) state.registerErrorEmail else null
        )

        CustomOutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(RegisterEvent.OnPasswordChanged(it)) },
            label = stringResource(id = R.string.label_password),
            placeholder = stringResource(id = R.string.placeholder_password),
            isPassword = true,
            isError = isErrorPassword,
            errorText = if (isErrorPassword) state.registerErrorPassword else null
        )

        CustomOutlinedTextField(
            value = state.confirmPassword,
            onValueChange = { onEvent(RegisterEvent.OnConfirmPasswordChanged(it)) },
            label = stringResource(id = R.string.label_confirm_password),
            placeholder = stringResource(id = R.string.placeholder_confirm_password),
            isPassword = true,
            isError = isErrorConfirm,
            errorText = if (isErrorConfirm) state.registerErrorConfirm else null
        )

        Spacer(modifier = Modifier.weight(1f))
        CustomPrimaryButton(
            value = stringResource(id = R.string.btn_register),
            onClick = { onEvent(RegisterEvent.Register) },
            isLoading = state.isLoading
        )
/*        CustomDivider(value = stringResource(id = R.string.divider))
        CustomSecondaryButton(
            value = stringResource(id = R.string.btn_daftar_dengan_google),
            onClick = { onEvent(RegisterEvent.RegisterWithGoogle) },
            isLoading = state.isLoading
        )*/
        Spacer(modifier = Modifier.height(20.dp))
        CustomClickableAnnotatedText(
            onLoginScreen = false,
            onTextSelected = {
                navigateToLogin()
            }
        )

        LaunchedEffect(key1 = isError) {
            if (isError) {
                val errorMessage = state.registerError ?: "Terjadi kesalahan tidak diketahui"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                onEvent(RegisterEvent.ClearToast)
            }
        }

        LaunchedEffect(key1 = state.isSuccessRegister) {
            if (state.isSuccessRegister) {
                Toast.makeText(context, "Pendaftaran berhasil, mohon untuk melengkapi data profil", Toast.LENGTH_SHORT).show()
                navigateToRegisterTwo()
            }
        }
    }
}

@Preview
@Composable
private fun RegisterStepOneScreenPreview() {
    TuboCareTheme {
        RegisterStepOneScreen(
            state = RegisterState(),
            onEvent = {},
            navigateToRegisterTwo = {},
            navigateToLogin = {}
        )
    }
}