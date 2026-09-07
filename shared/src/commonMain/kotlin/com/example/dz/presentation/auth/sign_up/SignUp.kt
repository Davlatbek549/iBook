package com.example.dz.presentation.auth.sign_up

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicCheckbox
import com.example.dz.designsystem.components.organic.OrganicDivider
import com.example.dz.designsystem.components.organic.OrganicField
import com.example.dz.designsystem.components.organic.OrganicLegalSheet
import com.example.dz.designsystem.components.organic.OrganicSocialButton
import com.example.dz.designsystem.components.organic.OrganicStrengthMeter
import com.example.dz.presentation.auth.passwordStrengthLabel
import com.example.dz.designsystem.components.organic.OrganicSubtitle
import com.example.dz.designsystem.components.organic.OrganicTitle
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicSize
import com.example.dz.designsystem.theme.organicBodyFontFamily
import dz.shared.generated.resources.Res
import com.example.dz.core.legal.LegalDocuments
import dz.shared.generated.resources.auth_agree_terms_desc
import dz.shared.generated.resources.auth_legal_agree
import dz.shared.generated.resources.auth_legal_keep_reading
import dz.shared.generated.resources.auth_apple
import dz.shared.generated.resources.auth_already_have_one
import dz.shared.generated.resources.auth_create_account
import dz.shared.generated.resources.auth_creating_account
import dz.shared.generated.resources.auth_email_placeholder
import dz.shared.generated.resources.auth_full_name
import dz.shared.generated.resources.auth_google
import dz.shared.generated.resources.auth_or
import dz.shared.generated.resources.auth_password_placeholder
import dz.shared.generated.resources.auth_privacy_lower
import dz.shared.generated.resources.auth_show
import dz.shared.generated.resources.auth_sign_in
import dz.shared.generated.resources.auth_signup_subtitle
import dz.shared.generated.resources.auth_signup_title
import dz.shared.generated.resources.auth_strength_fair
import dz.shared.generated.resources.auth_strength_good
import dz.shared.generated.resources.auth_strength_strong
import dz.shared.generated.resources.auth_strength_weak
import dz.shared.generated.resources.auth_terms_agree_prefix
import dz.shared.generated.resources.auth_terms_join
import dz.shared.generated.resources.auth_terms_lower
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Create account, per `#scr-sign-up`: 20dp between sections, 12dp inside the field group, no
 * uppercase labels — this screen leans on placeholders where sign in uses kickers.
 *
 * The Organic design has no username field, so the handle that the previous direction collected
 * is not asked for here.
 */
@Composable
fun SignUpScreen(
    uiState: SignUpUiState = SignUpUiState(),
    onEvent: (SignUpEvent) -> Unit = {}
) {
    val body = organicBodyFontFamily()
    val focusManager = LocalFocusManager.current
    val next = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
    val (googleAvailable, launchGoogle) = rememberGoogleLauncher(
        onToken = { onEvent(SignUpEvent.GoogleTokenReceived(it)) },
        onFailure = { onEvent(SignUpEvent.GoogleSignInFailed(it)) },
    )

    uiState.openDocument?.let { kind ->
        OrganicLegalSheet(
            document = LegalDocuments.of(kind),
            onAgree = { onEvent(SignUpEvent.DocumentAgreed) },
            onDismiss = { onEvent(SignUpEvent.DocumentDismissed) },
            agreeLabel = stringResource(Res.string.auth_legal_agree),
            keepReadingLabel = stringResource(Res.string.auth_legal_keep_reading)
        )
    }

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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OrganicTitle(text = stringResource(Res.string.auth_signup_title))
                OrganicSubtitle(
                    text = stringResource(Res.string.auth_signup_subtitle)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OrganicField(
                    value = uiState.fullName,
                    onValueChange = { onEvent(SignUpEvent.FullNameChanged(it)) },
                    placeholder = stringResource(Res.string.auth_full_name),
                    isError = uiState.nameError != null,
                    errorMessage = uiState.nameError,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = next
                )
                OrganicField(
                    value = uiState.email,
                    onValueChange = { onEvent(SignUpEvent.EmailChanged(it)) },
                    placeholder = stringResource(Res.string.auth_email_placeholder),
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = next
                )
                OrganicField(
                    value = uiState.password,
                    onValueChange = { onEvent(SignUpEvent.PasswordChanged(it)) },
                    placeholder = stringResource(Res.string.auth_password_placeholder),
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
                            onEvent(SignUpEvent.CreateAccountClicked)
                        }
                    )
                )

                if (uiState.password.isNotEmpty()) {
                    OrganicStrengthMeter(
                        filled = uiState.passwordStrength,
                        label = stringResource(passwordStrengthLabel(uiState.passwordStrength)),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OrganicCheckbox(
                    checked = uiState.termsAccepted,
                    onCheckedChange = { onEvent(SignUpEvent.TermsToggled(it)) },
                    contentDescription = stringResource(Res.string.auth_agree_terms_desc)
                )
                val linkStyles = TextLinkStyles(style = SpanStyle(color = OrganicColors.accent700))
                // Resolved before the builder: its lambdas are not composable scopes.
                val agreePrefix = stringResource(Res.string.auth_terms_agree_prefix)
                val termsWord = stringResource(Res.string.auth_terms_lower)
                val joinWord = stringResource(Res.string.auth_terms_join)
                val privacyWord = stringResource(Res.string.auth_privacy_lower)
                Text(
                    text = buildAnnotatedString {
                        append(agreePrefix)
                        // Real links rather than coloured text: the words look tappable, so they
                        // have to be, and this keeps the tap target on the word itself.
                        withLink(
                            LinkAnnotation.Clickable("terms", linkStyles) {
                                onEvent(SignUpEvent.TermsClicked)
                            }
                        ) { append(termsWord) }
                        append(joinWord)
                        withLink(
                            LinkAnnotation.Clickable("privacy", linkStyles) {
                                onEvent(SignUpEvent.PrivacyClicked)
                            }
                        ) { append(privacyWord) }
                        append(".")
                    },
                    fontFamily = body,
                    fontSize = 13.sp,
                    lineHeight = 19.5.sp,
                    color = OrganicColors.neutral700
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
                    if (uiState.isLoading) Res.string.auth_creating_account
                    else Res.string.auth_create_account
                ),
                onClick = { onEvent(SignUpEvent.CreateAccountClicked) },
                enabled = uiState.termsAccepted,
                isBusy = uiState.isLoading
            )

            OrganicDivider(text = stringResource(Res.string.auth_or))

            // Federated sign-up creates an account just as the form does, so it sits behind the
            // same agreement. Leaving these live would make the checkbox a formality anyone
            // could step around.
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OrganicSocialButton(
                    label = stringResource(Res.string.auth_google),
                    onClick = {
                        onEvent(SignUpEvent.GoogleClicked)
                        launchGoogle()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = uiState.termsAccepted && googleAvailable && !uiState.isLoading
                )
                OrganicSocialButton(
                    label = stringResource(Res.string.auth_apple),
                    onClick = { onEvent(SignUpEvent.AppleClicked) },
                    modifier = Modifier.weight(1f),
                    // Sign in with Apple is not built; showing it live would be a lie.
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = buildAnnotatedString {
                append(stringResource(Res.string.auth_already_have_one))
                append(" ")
                withStyle(SpanStyle(color = OrganicColors.accent700, fontWeight = FontWeight.SemiBold)) {
                    append(stringResource(Res.string.auth_sign_in))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEvent(SignUpEvent.SignInClicked) }
                .padding(vertical = 10.dp),
            fontFamily = body,
            fontSize = 14.sp,
            color = OrganicColors.neutral700,
            textAlign = TextAlign.Center
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
fun SignUpScreenPreview() {
    SignUpScreen()
}
