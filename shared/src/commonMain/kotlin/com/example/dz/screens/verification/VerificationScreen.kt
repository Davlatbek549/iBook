package com.example.dz.screens.verification

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.screens.forgot_password.BackButton
import com.example.dz.screens.forgot_password.BackButtonMetrics
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

private data class VerificationMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backTitleSpacing: Dp,
    val titleBodySpacing: Dp,
    val bodyOtpSpacing: Dp,
    val otpResendSpacing: Dp,
    val otpCellGap: Dp,
    val otpCellSize: Dp,
    val otpBorderWidth: Dp,
    val otpTextSize: TextUnit,
    val backButtonMetrics: BackButtonMetrics
)

@Composable
fun VerificationScreen() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val metrics = rememberVerificationMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val primaryText = MaterialTheme.colorScheme.onSurface
        val secondaryText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = metrics.horizontalPadding)
        ) {
            Spacer(Modifier.height(metrics.topSpacing))
            BackButton(
                onClick = { /*TODO*/ },
                metrics = metrics.backButtonMetrics
            )

            Spacer(modifier = Modifier.height(metrics.backTitleSpacing))

            Text(
                text = stringResource(Res.string.verification_title),
                style = MaterialTheme.typography.headlineLarge,
                color = primaryText
            )

            Text(
                text = stringResource(Res.string.verification_body),
                modifier = Modifier.padding(top = metrics.titleBodySpacing),
                style = MaterialTheme.typography.bodyMedium,
                color = secondaryText
            )

            Spacer(modifier = Modifier.height(metrics.bodyOtpSpacing))
            OtpInput(metrics = metrics)

            Spacer(modifier = Modifier.height(metrics.otpResendSpacing))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(Res.string.verification_did_not_receive),
                    color = secondaryText,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(Res.string.verification_resend),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* TODO */ },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun OtpInput(
    length: Int = 4,
    metrics: VerificationMetrics
) {
    val values = remember { mutableStateListOf(*Array(length) { "" }) }
    val focusRequesters = remember { List(length) { FocusRequester() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(metrics.otpCellGap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(length) { index ->
            val isFilled = values[index].isNotEmpty()

            BasicTextField(
                value = values[index],
                onValueChange = { newValue ->
                    if (newValue.length > 1) {
                        newValue.take(length).forEachIndexed { i, c ->
                            values[i] = c.toString()
                        }
                        focusRequesters[minOf(newValue.length, length) - 1].requestFocus()
                        return@BasicTextField
                    }

                    if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                        values[index] = newValue

                        if (newValue.isNotEmpty() && index < length - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = metrics.otpTextSize,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(metrics.otpCellSize)
                    .focusRequester(focusRequesters[index])

                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {

                            if (values[index].isEmpty() && index > 0) {
                                values[index - 1] = ""
                                focusRequesters[index - 1].requestFocus()
                            } else {
                                values[index] = ""
                            }

                            true
                        } else {
                            false
                        }
                    }

                    .clip(CircleShape)
                    .border(
                        width = metrics.otpBorderWidth,
                        color = if (isFilled)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        innerTextField()
                    }
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

@Composable
private fun rememberVerificationMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): VerificationMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 390.dp).coerceIn(0.86f, 1.22f)
    val heightScale = (maxHeight / 844.dp).coerceIn(0.82f, 1.18f)
    val compactness = minOf(widthScale, heightScale)

    VerificationMetrics(
        horizontalPadding = (24.dp * widthScale).coerceIn(20.dp, 34.dp),
        topSpacing = (52.dp * heightScale).coerceIn(36.dp, 68.dp),
        backTitleSpacing = (32.dp * heightScale).coerceIn(24.dp, 40.dp),
        titleBodySpacing = (8.dp * heightScale).coerceIn(6.dp, 12.dp),
        bodyOtpSpacing = (32.dp * heightScale).coerceIn(22.dp, 40.dp),
        otpResendSpacing = (32.dp * heightScale).coerceIn(22.dp, 40.dp),
        otpCellGap = (12.dp * widthScale).coerceIn(8.dp, 16.dp),
        otpCellSize = (64.dp * compactness).coerceIn(54.dp, 72.dp),
        otpBorderWidth = (1.5.dp * compactness).coerceIn(1.dp, 2.dp),
        otpTextSize = (24f * compactness).coerceIn(20f, 28f).sp,
        backButtonMetrics = BackButtonMetrics(
            size = (56.dp * compactness).coerceIn(48.dp, 62.dp),
            borderWidth = (1.dp * compactness).coerceIn(1.dp, 1.5.dp),
            iconSize = (24.dp * compactness).coerceIn(20.dp, 28.dp)
        )
    )
}

@Composable
@Preview(showBackground = true)
fun VerificationScreenPreview() {
    VerificationScreen()
}
