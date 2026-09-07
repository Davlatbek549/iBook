package com.example.dz.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.rememberCoroutineScope
import com.example.dz.core.auth.GoogleSignInResult
import com.example.dz.core.auth.rememberGoogleSignInClient
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicDivider
import com.example.dz.designsystem.components.organic.OrganicField
import com.example.dz.designsystem.components.organic.OrganicSocialButton
import com.example.dz.designsystem.components.organic.OrganicSubtitle
import com.example.dz.designsystem.components.organic.OrganicTitle
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicSize
import com.example.dz.designsystem.theme.organicBodyFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.auth_apple
import dz.shared.generated.resources.auth_create_an_account
import dz.shared.generated.resources.auth_email_label
import dz.shared.generated.resources.auth_email_placeholder
import dz.shared.generated.resources.auth_forgot_password
import dz.shared.generated.resources.auth_google
import dz.shared.generated.resources.auth_login_subtitle
import dz.shared.generated.resources.auth_login_title
import dz.shared.generated.resources.auth_new_here
import dz.shared.generated.resources.auth_or
import dz.shared.generated.resources.auth_password_changed
import dz.shared.generated.resources.auth_password_label
import dz.shared.generated.resources.auth_password_placeholder
import dz.shared.generated.resources.auth_show
import dz.shared.generated.resources.auth_sign_in
import dz.shared.generated.resources.auth_signing_in
import org.jetbrains.compose.resources.stringResource

/**
 * Sign in, in the Organic direction. Geometry follows `#scr-sign-in` in the handoff: 28dp auth
 * gutter, 22dp between sections, 14dp inside the field group.
 *
 * The design has no back affordance above this screen in the app's own graph — sign in is reached
 * from onboarding, which pops — so the handoff's back circle is omitted here rather than wired to
 * a destination that does not exist.
 */
@Composable
fun LoginScreen(
    uiState: LoginUiState = LoginUiState(),
    onEvent: (LoginEvent) -> Unit = {}
) {
    val body = organicBodyFontFamily()
    val focusManager = LocalFocusManager.current
    val (googleAvailable, launchGoogle) = rememberGoogleLauncher(
        onToken = { onEvent(LoginEvent.GoogleTokenReceived(it)) },
        onFailure = { onEvent(LoginEvent.GoogleSignInFailed(it)) },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(
                start = OrganicSize.authGutter,
                end = OrganicSize.authGutter,
                bottom = 30.dp
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OrganicTitle(text = stringResource(Res.string.auth_login_title))
                OrganicSubtitle(
                    text = stringResource(Res.string.auth_login_subtitle)
                )
            }

            if (uiState.passwordJustReset) {
                // A reset ends here rather than on Home, because the server issues no session
                // for one. Said plainly, or arriving at a sign-in screen reads as a failure.
                Text(
                    text = stringResource(Res.string.auth_password_changed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(OrganicColors.accent2_200)
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    fontFamily = body,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = OrganicColors.accent2_900
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OrganicField(
                    value = uiState.email,
                    onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                    placeholder = stringResource(Res.string.auth_email_placeholder),
                    label = stringResource(Res.string.auth_email_label),
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                OrganicField(
                    value = uiState.password,
                    onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                    placeholder = stringResource(Res.string.auth_password_placeholder),
                    label = stringResource(Res.string.auth_password_label),
                    isPassword = true,
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError,
                    showLabel = stringResource(Res.string.auth_show),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onEvent(LoginEvent.SignInClicked)
                        }
                    )
                )

                Text(
                    text = stringResource(Res.string.auth_forgot_password),
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onEvent(LoginEvent.ForgotPasswordClicked) }
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    fontFamily = body,
                    fontSize = 14.sp,
                    color = OrganicColors.accent700
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    fontFamily = body,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = OrganicColors.danger
                )
            }

            OrganicButton(
                text = stringResource(
                    if (uiState.isLoading) Res.string.auth_signing_in else Res.string.auth_sign_in
                ),
                onClick = { onEvent(LoginEvent.SignInClicked) },
                isBusy = uiState.isLoading
            )

            OrganicDivider(text = stringResource(Res.string.auth_or))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OrganicSocialButton(
                    label = stringResource(Res.string.auth_google),
                    onClick = {
                        onEvent(LoginEvent.GoogleClicked)
                        launchGoogle()
                    },
                    modifier = Modifier.weight(1f),
                    // Inert where no platform implementation exists, rather than failing on tap.
                    enabled = googleAvailable && !uiState.isLoading
                )
                OrganicSocialButton(
                    label = stringResource(Res.string.auth_apple),
                    onClick = { onEvent(LoginEvent.AppleClicked) },
                    modifier = Modifier.weight(1f),
                    // Sign in with Apple is not built; showing it live would be a lie.
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = buildAnnotatedString {
                append(stringResource(Res.string.auth_new_here))
                append(" ")
                withStyle(SpanStyle(color = OrganicColors.accent700, fontWeight = FontWeight.SemiBold)) {
                    append(stringResource(Res.string.auth_create_an_account))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(LoginEvent.SignUpClicked) }
                .padding(vertical = 10.dp),
            fontFamily = body,
            fontSize = 14.sp,
            color = OrganicColors.neutral700,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}


/**
 * Runs the platform account picker and feeds the outcome back as events. Lives in the screen
 * because the picker needs a platform handle that only composition can provide; everything the
 * token then goes through is shared.
 */
@Composable
private fun rememberGoogleLauncher(
    onToken: (String) -> Unit,
    onFailure: (String?) -> Unit,
): Pair<Boolean, () -> Unit> {
    val client = rememberGoogleSignInClient()
    val scope = rememberCoroutineScope()
    return client.isAvailable to {
        scope.launch {
            when (val result = client.requestIdToken()) {
                is GoogleSignInResult.Success -> onToken(result.idToken)
                // Backing out is a choice, so this clears busy without an error message.
                GoogleSignInResult.Cancelled -> onFailure(null)
                is GoogleSignInResult.Failed -> onFailure(result.message)
                GoogleSignInResult.Unsupported -> onFailure(null)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
