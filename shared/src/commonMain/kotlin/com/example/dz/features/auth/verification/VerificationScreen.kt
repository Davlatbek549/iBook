package com.example.dz.features.auth.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.app_components.ink.InkButton
import com.example.dz.app_components.ink.InkIconButton
import com.example.dz.theme.InkColors
import com.example.dz.theme.InkShape
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.auth_nothing_arrived
import dz.shared.generated.resources.auth_resend_code
import dz.shared.generated.resources.auth_verification_copy
import dz.shared.generated.resources.auth_verification_title
import dz.shared.generated.resources.auth_verify
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

private const val CODE_LENGTH = 4
private const val RESEND_SECONDS = 45

@Composable
fun VerificationScreen(
    onVerified: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    var code by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableIntStateOf(RESEND_SECONDS) }

    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(start = 26.dp, end = 26.dp, top = 4.dp, bottom = 26.dp)
    ) {
        InkIconButton(icon = InkIcons.Back, onClick = onBack, colors = colors)

        Text(
            text = stringResource(Res.string.auth_verification_title),
            modifier = Modifier.padding(top = 30.dp),
            fontFamily = displayFont,
            fontWeight = FontWeight.Medium,
            fontSize = 30.sp,
            lineHeight = 34.5.sp,
            color = colors.ink
        )

        Text(
            text = stringResource(Res.string.auth_verification_copy),
            modifier = Modifier.padding(top = 14.dp),
            fontFamily = bodyFont,
            fontSize = 14.sp,
            lineHeight = 23.sp,
            color = colors.inkSoft
        )

        Spacer(modifier = Modifier.height(30.dp))

        CodeInput(
            code = code,
            onCodeChange = { code = it },
            colors = colors
        )

        Spacer(modifier = Modifier.height(26.dp))

        InkButton(
            text = stringResource(Res.string.auth_verify),
            onClick = onVerified,
            colors = colors
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(Res.string.auth_nothing_arrived))
                    append(" ")
                    withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(Res.string.auth_resend_code))
                    }
                    if (secondsLeft > 0) {
                        append(" · 0:")
                        append(secondsLeft.toString().padStart(2, '0'))
                    }
                },
                modifier = Modifier
                    .clickable(enabled = secondsLeft == 0) { secondsLeft = RESEND_SECONDS }
                    .padding(4.dp),
                fontFamily = bodyFont,
                fontSize = 13.sp,
                color = colors.muted
            )
        }
    }
}

/** Four code boxes backed by a single invisible text field. */
@Composable
private fun CodeInput(
    code: String,
    onCodeChange: (String) -> Unit,
    colors: InkColors,
) {
    var focused by remember { mutableStateOf(false) }
    val displayFont = inkDisplayFontFamily()
    val shape = RoundedCornerShape(InkShape.radiusSm + 2.dp)

    BasicTextField(
        value = code,
        onValueChange = { new ->
            if (new.length <= CODE_LENGTH && new.all { it.isDigit() }) onCodeChange(new)
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.isFocused },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        decorationBox = { innerTextField ->
            // real field is zero-sized; the boxes below render its state
            Box {
                Box(modifier = Modifier.size(0.dp)) { innerTextField() }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(CODE_LENGTH) { i ->
                        val active = focused && i == code.length
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                                .clip(shape)
                                .background(colors.surface)
                                .border(1.dp, if (active) colors.accent else colors.line, shape),
                            contentAlignment = Alignment.Center
                        ) {
                            val digit = code.getOrNull(i)?.toString()
                            if (digit != null) {
                                Text(
                                    text = digit,
                                    fontFamily = displayFont,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 26.sp,
                                    color = colors.ink
                                )
                            } else if (active) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(26.dp)
                                        .background(colors.accent)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun VerificationScreenPreview() {
    VerificationScreen()
}
