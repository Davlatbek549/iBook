package com.example.dz.presentation.auth.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicBackButton
import com.example.dz.designsystem.components.organic.OrganicButton
import com.example.dz.designsystem.components.organic.OrganicCodeField
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicSize
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.auth_back
import dz.shared.generated.resources.auth_nothing_arrived
import dz.shared.generated.resources.auth_resend_code
import dz.shared.generated.resources.auth_sending_code
import dz.shared.generated.resources.auth_resend_in
import dz.shared.generated.resources.auth_verification_copy_prefix
import dz.shared.generated.resources.auth_verification_copy_no_email
import dz.shared.generated.resources.auth_verification_reset_no_email
import dz.shared.generated.resources.auth_verification_reset_prefix
import dz.shared.generated.resources.auth_verification_reset_suffix
import dz.shared.generated.resources.auth_verification_title
import dz.shared.generated.resources.auth_verify
import dz.shared.generated.resources.auth_verifying
import org.jetbrains.compose.resources.stringResource

/**
 * Code entry, per `#scr-verification`: 24dp between sections, 64dp boxes at a 20dp radius.
 *
 * The handoff's PNG and HTML draw four boxes while its copy promises a six-digit code; six wins,
 * because the copy is product text and four digits is a thousand-guess space on the most attacked
 * endpoint in the app. [VERIFICATION_CODE_LENGTH] is the one place that decides it.
 */
@Composable
fun VerificationScreen(
    uiState: VerificationUiState = VerificationUiState(),
    onEvent: (VerificationEvent) -> Unit = {}
) {
    val body = organicBodyFontFamily()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
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
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OrganicBackButton(
            onClick = { onEvent(VerificationEvent.BackClicked) },
            contentDescription = stringResource(Res.string.auth_back),
            isBusy = uiState.isLeaving
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = stringResource(Res.string.auth_verification_title),
                fontFamily = organicHeadingFontFamily(),
                fontSize = 30.sp,
                lineHeight = 34.5.sp,
                color = OrganicColors.text
            )
            Text(
                // Naming the address is how a typo on the previous screen gets caught, rather
                // than waiting for a code that was never going to arrive. It matters more now
                // that asking for a reset code comes straight here without confirming first.
                //
                // A reset hedges where sign-up does not: the reader may have typed an address
                // that has no account, and the server answers both alike on purpose, so this is
                // the one screen that has to carry that promise without breaking it.
                text = buildAnnotatedString {
                    val isReset = uiState.purpose == VerificationPurpose.ResetPassword
                    if (uiState.email.isBlank()) {
                        append(
                            stringResource(
                                if (isReset) Res.string.auth_verification_reset_no_email
                                else Res.string.auth_verification_copy_no_email
                            )
                        )
                    } else {
                        append(
                            stringResource(
                                if (isReset) Res.string.auth_verification_reset_prefix
                                else Res.string.auth_verification_copy_prefix
                            )
                        )
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = OrganicColors.text)) {
                            append(uiState.email)
                        }
                        append(
                            if (isReset) stringResource(Res.string.auth_verification_reset_suffix)
                            else "."
                        )
                    }
                },
                fontFamily = body,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                color = OrganicColors.neutral700
            )
        }

        OrganicCodeField(
            code = uiState.code,
            onCodeChange = { onEvent(VerificationEvent.CodeChanged(it)) },
            length = VERIFICATION_CODE_LENGTH,
            isError = uiState.errorMessage != null,
            onComplete = { onEvent(VerificationEvent.VerifyClicked) }
        )

        Text(
            // The prompt leads, the action follows in accent — the same shape as "New here?
            // Create an account" on sign-in. Without it the countdown states a fact without
            // saying why anyone should care about it.
            text = buildAnnotatedString {
                append(stringResource(Res.string.auth_nothing_arrived))
                append(" ")
                if (uiState.isSendingCode) {
                    append(stringResource(Res.string.auth_sending_code))
                } else if (uiState.canResend) {
                    withStyle(
                        SpanStyle(color = OrganicColors.accent700, fontWeight = FontWeight.SemiBold)
                    ) {
                        append(stringResource(Res.string.auth_resend_code))
                    }
                } else {
                    append(stringResource(Res.string.auth_resend_in))
                    withStyle(
                        SpanStyle(color = OrganicColors.accent700, fontWeight = FontWeight.Bold)
                    ) {
                        append(uiState.countdown)
                    }
                }
            },
            modifier = Modifier
                .clickable(enabled = uiState.canResend && !uiState.isSendingCode) {
                    onEvent(VerificationEvent.ResendClicked)
                }
                .padding(vertical = 4.dp),
            fontFamily = body,
            fontSize = 14.sp,
            color = OrganicColors.neutral700
        )

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
                if (uiState.isLoading) Res.string.auth_verifying else Res.string.auth_verify
            ),
            onClick = { onEvent(VerificationEvent.VerifyClicked) },
            enabled = uiState.isComplete,
            isBusy = uiState.isLoading
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun VerificationScreenPreview() {
    VerificationScreen()
}
