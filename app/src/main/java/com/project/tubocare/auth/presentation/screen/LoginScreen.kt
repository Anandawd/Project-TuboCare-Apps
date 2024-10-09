package com.project.tubocare.auth.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.auth.presentation.event.LoginEvent
import com.project.tubocare.auth.presentation.state.LoginState
import com.project.tubocare.core.presentation.components.CustomClickableAnnotatedText
import com.project.tubocare.core.presentation.components.CustomClickableText
import com.project.tubocare.core.presentation.components.CustomDivider
import com.project.tubocare.core.presentation.components.CustomHeaderComponent
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.presentation.components.CustomSecondaryButton
import com.project.tubocare.core.presentation.components.CustomOutlinedTextField
import com.project.tubocare.ui.theme.TuboCareTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
) {
    val isErrorEmail = state.loginErrorEmail != null
    val isErrorPassword = state.loginErrorPassword != null
    val isErrorEmailReset = state.resetErrorEmail != null
    val isErrorResetPassword = state.resetError != null
    val isError = state.loginError != null
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeaderComponent(value = stringResource(id = R.string.title_selamat_datang))
        Spacer(modifier = Modifier.height(20.dp))

        CustomOutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(LoginEvent.OnEmailChanged(it)) },
            label = stringResource(id = R.string.label_email),
            placeholder = stringResource(id = R.string.placeholder_email),
            isError = isErrorEmail,
            errorText = if (isErrorEmail) state.loginErrorEmail else null,
            modifier = Modifier.testTag("email_field")
        )

        CustomOutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(LoginEvent.OnPasswordChanged(it)) },
            label = stringResource(id = R.string.label_password),
            placeholder = stringResource(id = R.string.label_password),
            isPassword = true,
            isError = isErrorPassword,
            errorText = if (isErrorPassword) state.loginErrorPassword else null,
            modifier = Modifier.testTag("password_field")
        )

        CustomClickableText(
            value = stringResource(id = R.string.lupa_password)
        ) {
            showBottomSheet = true
        }
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(1f))
        CustomPrimaryButton(
            value = stringResource(id = R.string.btn_masuk),
            onClick = { onEvent(LoginEvent.Login) },
            isLoading = state.isLoading,
            modifier = Modifier.testTag("login_button")
        )
/*        CustomDivider(value = stringResource(id = R.string.divider))
        CustomSecondaryButton(
            value = stringResource(id = R.string.btn_masuk_dengan_google),
            onClick = { onEvent(LoginEvent.LoginWithGoogle(context)) },
            isLoading = state.isLoading,
        )*/
        Spacer(modifier = Modifier.height(20.dp))
        CustomClickableAnnotatedText(
            onLoginScreen = true,
            onTextSelected = {
                navigateToRegister.invoke()
            }
        )

        LaunchedEffect(key1 = isError) {
            if (isError) {
                val errorMessage = state.loginError ?: "Terjadi kesalahan yang tidak diketahui"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                onEvent(LoginEvent.ClearToast)
            }
        }

        LaunchedEffect(key1 = state.isSuccessLogin) {
            if (state.isSuccessLogin) {
                Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
        }

    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.secondary,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.title_forgot_password),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.subtitle_forgot_password),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomOutlinedTextField(
                    value = state.resetPassword,
                    onValueChange = { onEvent(LoginEvent.OnResetPasswordChanged(it)) },
                    label = stringResource(id = R.string.label_email),
                    placeholder = stringResource(id = R.string.placeholder_email),
                    isError = isErrorEmailReset,
                    errorText = if (isErrorEmailReset) state.resetErrorEmail else null
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomPrimaryButton(
                    value = stringResource(id = R.string.btn_kirim),
                    onClick = { onEvent(LoginEvent.ResetPassword) },
                    isLoading = state.isLoading
                )
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    LaunchedEffect(key1 = isErrorResetPassword) {
        if (isErrorResetPassword) {
            val errorMessage = state.resetError ?: "Terjadi kesalahan yang tidak diketahui"
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            onEvent(LoginEvent.ClearToast)
        }
    }

    LaunchedEffect(key1 = state.isSuccessResetPassword) {
        if (state.isSuccessResetPassword) {
            Toast.makeText(context, "Email berhasil dikirim, mohon periksa email Anda", Toast.LENGTH_SHORT).show()
            showBottomSheet = false
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    TuboCareTheme {
        LoginScreen(
            state = LoginState(),
            onEvent = {},
            navigateToHome = {},
            navigateToRegister = {}
        )
    }
}

/*@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToForgotPassword: () -> Unit,
) {
    val loginState = loginViewModel?.loginState
    val isError = loginState?.loginError != null
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
    ) {

        if (isError) {
            Text(
                text = loginState?.loginError ?: "unknown error",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.heightIn(min = 20.dp, max = 60.dp))
        CustomHeaderComponent(value = stringResource(id = R.string.title_selamat_datang))
        Spacer(modifier = Modifier.heightIn(min = 20.dp, max = 60.dp))

        CustomOutlinedTextField(
            value = loginState?.email ?: "",
            onValueChange = { loginViewModel?.onLoginEmailChange(it) },
            label = stringResource(id = R.string.label_email),
            placeholder = stringResource(id = R.string.placeholder_email),
            isError = isError
        )

        CustomOutlinedTextField(
            value = loginState?.password ?: "",
            onValueChange = { loginViewModel?.onLoginPasswordChange(it) },
            label = stringResource(id = R.string.label_password),
            placeholder = stringResource(id = R.string.label_password),
            isPassword = true,
            isError = isError
        )

        CustomClickableText(
            value = stringResource(id = R.string.lupa_password)
        ) {
            navigateToForgotPassword()
        }
        Spacer(modifier = Modifier.weight(1f))
        CustomPrimaryButtom(
            value = stringResource(id = R.string.btn_masuk),
            onClick = { loginViewModel?.loginUser(context) }
        )
        CustomDivider(value = stringResource(id = R.string.divider))
        CustomSecondaryButtom(value = stringResource(id = R.string.btn_masuk_dengan_google)) {

        }
        Spacer(modifier = Modifier.height(20.dp))
        CustomClickableAnnotatedText(
            onLoginScreen = true,
            onTextSelected = {
                navigateToRegister.invoke()
            }
        )

        if (loginState?.isLoading == true) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        LaunchedEffect(key1 = loginViewModel?.hasUser) {
            if (loginViewModel?.hasUser == true) {
                navigateToHome.invoke()
            }
        }
    }
}*/

/*@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToForgotPassword: () -> Unit,
) {
    val loginState = loginViewModel?.loginState
    val isError = loginState?.errorMessageLoginProcess != null
    val context = LocalContext.current

    NavDestinationHelper(
        shouldNavigate = {
            loginViewModel?.loginState?.isSuccessfullyLoggedIn ?: false
        },
        destination = {
            navigateToHome()
        }
    )

    loginState?.toastMessage?.let { message ->
        LaunchedEffect(message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            loginViewModel.clearToastMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
    ) {

        if (isError){
            Text(
                text = loginState?.errorMessageLoginProcess ?: "unknown error",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.heightIn(min = 20.dp, max = 60.dp))
        CustomHeaderComponent(value = stringResource(id = R.string.title_selamat_datang))
        Spacer(modifier = Modifier.heightIn(min = 20.dp, max = 60.dp))
        CustomOutlinedTextField(
            value = loginState?.email ?: "",
            onValueChange = { loginViewModel?.onEmailInputChange(it)},
            label = stringResource(id = R.string.label_email),
            placeholder = stringResource(id = R.string.placeholder_email),
            isSingleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = isError
        )
        CustomPasswordOutlinedTextField(
            value = loginState?.password ?: "",
            onValueChange = { loginViewModel?.onPasswordInputChange(it)},
            label = stringResource(id = R.string.label_password),
            placeholder = stringResource(id = R.string.placeholder_password),
            isError = isError
        )
        CustomClickableText(
            value = stringResource(id = R.string.lupa_password)
        ) {
            navigateToForgotPassword()
        }
        Spacer(modifier = Modifier.weight(1f))
        CustomPrimaryButtom(
            value = stringResource(id = R.string.btn_masuk),
            onClick = { loginViewModel?.onLoginClick() }
        )
        CustomDivider(value = stringResource(id = R.string.divider))
        CustomSecondaryButtom(value = stringResource(id = R.string.btn_masuk_dengan_google)) {

        }
        Spacer(modifier = Modifier.height(20.dp))
        CustomClickableAnnotatedText(
            onLoginScreen = true,
            onTextSelected = {
                navigateToRegister()
            }
        )
    }
}*/

/*
@Preview(device = Devices.PIXEL_4)
@Composable
private fun LoginScreenPreview() {
    TuboCareTheme {
        LoginScreen(
            loginViewModel = null,
            navigateToHome = {},
            navigateToRegister = {},
            navigateToForgotPassword = {}
        )
    }
}*/
