package com.example.dz.screens.sign_up

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.dz.app_components.ink.InkField
import com.example.dz.app_components.ink.InkFooterLink
import com.example.dz.app_components.ink.InkLabel
import com.example.dz.app_components.ink.InkOrDivider
import com.example.dz.app_components.ink.InkSocialButton
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.auth_apple
import dz.shared.generated.resources.auth_already_reader
import dz.shared.generated.resources.auth_create_account
import dz.shared.generated.resources.auth_email_placeholder
import dz.shared.generated.resources.auth_full_name
import dz.shared.generated.resources.auth_google
import dz.shared.generated.resources.auth_or
import dz.shared.generated.resources.auth_password_placeholder
import dz.shared.generated.resources.auth_sign_in
import dz.shared.generated.resources.auth_signup_eyebrow
import dz.shared.generated.resources.auth_signup_title
import dz.shared.generated.resources.auth_username_placeholder
import dz.shared.generated.resources.ic_apple
import dz.shared.generated.resources.ic_google
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignUpScreen(
    onSignInClick: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {}
) {
    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(start = 26.dp, end = 26.dp, bottom = 26.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 14.dp)
        ) {
            InkLabel(text = stringResource(Res.string.auth_signup_eyebrow), colors = colors)

            Text(
                text = stringResource(Res.string.auth_signup_title),
                modifier = Modifier.padding(top = 14.dp),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = 32.sp,
                lineHeight = 35.sp,
                color = colors.ink
            )

            Spacer(modifier = Modifier.height(26.dp))

            InkField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = stringResource(Res.string.auth_full_name),
                leadingIcon = InkIcons.User,
                colors = colors
            )
            Spacer(modifier = Modifier.height(12.dp))
            InkField(
                value = username,
                onValueChange = { username = it },
                placeholder = stringResource(Res.string.auth_username_placeholder),
                leadingIcon = InkIcons.At,
                colors = colors
            )
            Spacer(modifier = Modifier.height(12.dp))
            InkField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(Res.string.auth_email_placeholder),
                leadingIcon = InkIcons.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = colors
            )
            Spacer(modifier = Modifier.height(12.dp))
            InkField(
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(Res.string.auth_password_placeholder),
                leadingIcon = InkIcons.Lock,
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = colors
            )

            Text(
                text = buildAnnotatedString {
                    append("By continuing you agree to DZ’s ")
                    withStyle(SpanStyle(color = colors.inkSoft, fontWeight = FontWeight.SemiBold)) {
                        append("Terms")
                    }
                    append(" and ")
                    withStyle(SpanStyle(color = colors.inkSoft, fontWeight = FontWeight.SemiBold)) {
                        append("Privacy policy")
                    }
                    append(".")
                },
                modifier = Modifier.padding(start = 2.dp, end = 2.dp, top = 16.dp),
                fontFamily = bodyFont,
                fontSize = 12.sp,
                lineHeight = 19.sp,
                color = colors.muted
            )

            Spacer(modifier = Modifier.height(18.dp))

            InkButton(
                text = stringResource(Res.string.auth_create_account),
                onClick = onSignUpSuccess,
                colors = colors
            )

            Spacer(modifier = Modifier.height(22.dp))
            InkOrDivider(text = stringResource(Res.string.auth_or), colors = colors)
            Spacer(modifier = Modifier.height(22.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                InkSocialButton(
                    icon = Res.drawable.ic_google,
                    label = stringResource(Res.string.auth_google),
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = colors
                )
                InkSocialButton(
                    icon = Res.drawable.ic_apple,
                    label = stringResource(Res.string.auth_apple),
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = colors
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            InkFooterLink(
                prefix = stringResource(Res.string.auth_already_reader),
                action = stringResource(Res.string.auth_sign_in),
                onClick = onSignInClick,
                colors = colors
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}
