package com.example.dz.presentation.auth.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
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
import dz.shared.generated.resources.auth_back_to_sign_in
import dz.shared.generated.resources.auth_email_placeholder
import dz.shared.generated.resources.auth_enter_code
import dz.shared.generated.resources.auth_forgot_copy
import dz.shared.generated.resources.auth_forgot_title
import dz.shared.generated.resources.auth_reset_sent
import dz.shared.generated.resources.auth_send_reset
import dz.shared.generated.resources.auth_sending_reset
import org.jetbrains.compose.resources.stringResource

/**
 * Recovery start, per `#scr-forgot-password`: 22dp between sections, an 88dp sage roundel above
 * the title, then one field and one action.
 *
 * The screen confirms in place rather than navigating on tap, so a mistyped address can be fixed
 * without going back — the button becomes the way forward once a code has been asked for.
 */
@Composable
fun ForgotPasswordScreen(
    uiState: ForgotPasswordUiState = ForgotPasswordUiState(),
    onEvent: (ForgotPasswordEvent) -> Unit = {}
) {
    val colors = organicColors()
    val body = organicBodyFontFamily()
    val focusManager = LocalFocusManager.current
    val sentTo = uiState.sentTo

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
            onClick = { onEvent(ForgotPasswordEvent.BackClicked) },
            contentDescription = stringResource(Res.string.auth_back),
            colors = colors
        )

        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(colors.accent2200),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = InkIcons.Message,
                contentDescription = null,
                tint = colors.accent2900,
                modifier = Modifier.size(34.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OrganicTitle(
                text = stringResource(Res.string.auth_forgot_title),
                fontSize = 30.sp,
                colors = colors
            )
            OrganicSubtitle(text = stringResource(Res.string.auth_forgot_copy), colors = colors)
        }

        OrganicField(
            value = uiState.email,
            onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
            placeholder = stringResource(Res.string.auth_email_placeholder),
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(ForgotPasswordEvent.SendLinkClicked)
                }
            ),
            colors = colors
        )

        OrganicButton(
            text = stringResource(
                when {
                    uiState.isLoading -> Res.string.auth_sending_reset
                    sentTo != null -> Res.string.auth_enter_code
                    else -> Res.string.auth_send_reset
                }
            ),
            onClick = {
                onEvent(
                    if (sentTo != null) ForgotPasswordEvent.ContinueClicked
                    else ForgotPasswordEvent.SendLinkClicked
                )
            },
            isBusy = uiState.isLoading,
            colors = colors
        )

        if (sentTo != null) {
            Text(
                // Deliberately non-committal: saying whether the address is registered would
                // turn this screen into a way to test which emails have accounts.
                text = stringResource(Res.string.auth_reset_sent, sentTo),
                fontFamily = body,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = colors.neutral700
            )
        }

        Text(
            text = stringResource(Res.string.auth_back_to_sign_in),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(ForgotPasswordEvent.BackClicked) }
                .padding(vertical = 8.dp),
            fontFamily = body,
            fontSize = 14.sp,
            color = colors.neutral700,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}
