package com.example.dz.presentation.auth.new_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicBackButton
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicField
import com.example.dz.designsystem.components.organic.OrganicSubtitle
import com.example.dz.designsystem.components.organic.OrganicTitle
import com.example.dz.designsystem.theme.OrganicSize
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.auth_back
import dz.shared.generated.resources.auth_confirm_password_placeholder
import dz.shared.generated.resources.auth_go_to_sign_in
import dz.shared.generated.resources.auth_new_password_copy
import dz.shared.generated.resources.auth_new_password_placeholder
import dz.shared.generated.resources.auth_new_password_title
import dz.shared.generated.resources.auth_password_changed
import dz.shared.generated.resources.auth_save_and_sign_in
import dz.shared.generated.resources.auth_saving
import dz.shared.generated.resources.auth_show
import org.jetbrains.compose.resources.stringResource

/**
 * Choosing the replacement password, the step that actually completes a reset.
 *
 * The handoff does not draw this screen — its recovery flow stops at the code — so this follows
 * the nearest pattern it does specify: the Forgot password frame, minus the roundel, with the
 * confirmation field added.
 */
@Composable
fun NewPasswordScreen(
    uiState: NewPasswordUiState = NewPasswordUiState(),
    onEvent: (NewPasswordEvent) -> Unit = {}
) {
    val colors = organicColors()
    val body = organicBodyFontFamily()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(
                start = OrganicSize.authGutter,
                end = OrganicSize.authGutter,
                top = 18.dp,
                bottom = 30.dp
            ),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        OrganicBackButton(
            onClick = { onEvent(NewPasswordEvent.BackClicked) },
            contentDescription = stringResource(Res.string.auth_back),
            colors = colors
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OrganicTitle(
                text = stringResource(Res.string.auth_new_password_title),
                fontSize = 30.sp,
                colors = colors
            )
            OrganicSubtitle(
                text = stringResource(Res.string.auth_new_password_copy),
                colors = colors
            )
        }

        if (!uiState.isSaved) Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OrganicField(
                value = uiState.password,
                onValueChange = { onEvent(NewPasswordEvent.PasswordChanged(it)) },
                placeholder = stringResource(Res.string.auth_new_password_placeholder),
                isPassword = true,
                showLabel = stringResource(Res.string.auth_show),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = colors
            )

            OrganicField(
                value = uiState.confirmation,
                onValueChange = { onEvent(NewPasswordEvent.ConfirmationChanged(it)) },
                placeholder = stringResource(Res.string.auth_confirm_password_placeholder),
                isPassword = true,
                isError = uiState.confirmationError != null,
                errorMessage = uiState.confirmationError,
                showLabel = stringResource(Res.string.auth_show),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onEvent(NewPasswordEvent.SaveClicked)
                    }
                ),
                colors = colors
            )
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                fontFamily = body,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = colors.danger
            )
        }

        if (uiState.isSaved) {
            Text(
                text = stringResource(Res.string.auth_password_changed),
                fontFamily = body,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = colors.neutral700
            )
        }

        OrganicButton(
            text = stringResource(
                when {
                    uiState.isSaved -> Res.string.auth_go_to_sign_in
                    uiState.isLoading -> Res.string.auth_saving
                    else -> Res.string.auth_save_and_sign_in
                }
            ),
            onClick = {
                onEvent(
                    if (uiState.isSaved) NewPasswordEvent.SignInClicked
                    else NewPasswordEvent.SaveClicked
                )
            },
            isBusy = uiState.isLoading,
            colors = colors
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun NewPasswordScreenPreview() {
    NewPasswordScreen()
}
