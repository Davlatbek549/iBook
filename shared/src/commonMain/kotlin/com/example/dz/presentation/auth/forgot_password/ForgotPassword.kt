package com.example.dz.presentation.auth.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkButton
import com.example.dz.designsystem.components.ink.InkField
import com.example.dz.designsystem.components.ink.InkFooterLink
import com.example.dz.designsystem.components.ink.InkIconButton
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.auth_back_to_sign_in
import dz.shared.generated.resources.auth_email_placeholder
import dz.shared.generated.resources.auth_forgot_copy
import dz.shared.generated.resources.auth_forgot_title
import dz.shared.generated.resources.auth_remembered
import dz.shared.generated.resources.auth_send_reset
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit = {},
    onSendLink: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    var email by remember { mutableStateOf("") }

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
            text = stringResource(Res.string.auth_forgot_title),
            modifier = Modifier.padding(top = 30.dp),
            fontFamily = displayFont,
            fontWeight = FontWeight.Medium,
            fontSize = 30.sp,
            lineHeight = 34.5.sp,
            color = colors.ink
        )

        Text(
            text = stringResource(Res.string.auth_forgot_copy),
            modifier = Modifier.padding(top = 14.dp),
            fontFamily = bodyFont,
            fontSize = 14.sp,
            lineHeight = 23.sp,
            color = colors.inkSoft
        )

        Spacer(modifier = Modifier.height(26.dp))

        InkField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(Res.string.auth_email_placeholder),
            leadingIcon = InkIcons.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = colors
        )

        Spacer(modifier = Modifier.height(18.dp))

        InkButton(
            text = stringResource(Res.string.auth_send_reset),
            onClick = onSendLink,
            colors = colors
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            InkFooterLink(
                prefix = stringResource(Res.string.auth_remembered),
                action = stringResource(Res.string.auth_back_to_sign_in),
                onClick = onBack,
                colors = colors
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}
