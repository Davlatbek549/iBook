package com.example.ibook.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ibook.R
import com.example.ibook.app_components.inputs.InputKind
import com.example.ibook.app_components.inputs.UniversalInputField

data class SocialButtonMetrics(
    val height: Dp,
    val corner: Dp,
    val borderWidth: Dp,
    val horizontalPadding: Dp,
    val iconGap: Dp
)

private data class LoginMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val emailTopSpacing: Dp,
    val fieldTopSpacing: Dp,
    val passwordTopSpacing: Dp,
    val forgotPasswordTopSpacing: Dp,
    val signInTopSpacing: Dp,
    val signInHeight: Dp,
    val signInCorner: Dp,
    val dividerVerticalPadding: Dp,
    val dividerTextHorizontalPadding: Dp,
    val socialButtonGap: Dp,
    val bottomPromptTopSpacing: Dp,
    val fieldBorderWidth: Dp,
    val fieldCorner: Dp,
    val socialButtonMetrics: SocialButtonMetrics
)

@Composable
fun LoginScreen() {

    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberLoginMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = metrics.horizontalPadding)
        ) {

        Spacer(modifier = Modifier.height(metrics.topSpacing))

        Text(
            text = stringResource(R.string.sign_in_with_email),
            style = MaterialTheme.typography.headlineLarge,
            color = primaryText
        )

        Text(
            text = stringResource(R.string.input_your_registered_account),
            style = MaterialTheme.typography.bodyLarge,
            color = secondaryText
        )

        Text(
            text = stringResource(R.string.email),
            modifier = Modifier.padding(top = metrics.emailTopSpacing),
            style = MaterialTheme.typography.bodyMedium,
            color = secondaryText
        )

        UniversalInputField(
            value = email,
            onValueChange = { email = it },
            hint = stringResource(R.string.email_hint),
            leftIcon = painterResource(R.drawable.ic_email),
            modifier = Modifier
                .padding(top = metrics.fieldTopSpacing)
                .border(
                    metrics.fieldBorderWidth,
                    borderColor,
                    RoundedCornerShape(metrics.fieldCorner)
                ),
            inputType = InputKind.Email,
            borderColor = Color.Transparent
        )

        Text(
            text = stringResource(R.string.password),
            modifier = Modifier.padding(top = metrics.passwordTopSpacing),
            style = MaterialTheme.typography.bodyMedium,
            color = secondaryText
        )

        UniversalInputField(
            value = password,
            onValueChange = { password = it },
            hint = stringResource(R.string.password_hint),
            leftIcon = painterResource(R.drawable.ic_password),
            modifier = Modifier
                .padding(top = metrics.fieldTopSpacing)
                .border(
                    metrics.fieldBorderWidth,
                    borderColor,
                    RoundedCornerShape(metrics.fieldCorner)
                ),
            inputType = InputKind.Password,
            borderColor = Color.Transparent
        )

        Text(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = metrics.forgotPasswordTopSpacing)
                .clickable { /* TODO */ },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = metrics.signInTopSpacing)
                .height(metrics.signInHeight)
                .clip(RoundedCornerShape(metrics.signInCorner))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { /* TODO LOGIN */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = metrics.dividerVerticalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
            Text(
                text = stringResource(R.string.or),
                modifier = Modifier.padding(horizontal = metrics.dividerTextHorizontalPadding),
                color = secondaryText
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
        }

        SocialButton(
            text = stringResource(R.string.sign_in_with_apple),
            icon = R.drawable.ic_apple,
            borderColor = borderColor,
            metrics = metrics.socialButtonMetrics
        )

        Spacer(modifier = Modifier.height(metrics.socialButtonGap))

        SocialButton(
            text = stringResource(R.string.sign_in_with_google),
            icon = R.drawable.ic_google,
            borderColor = borderColor,
            metrics = metrics.socialButtonMetrics
        )

        Spacer(modifier = Modifier.height(metrics.bottomPromptTopSpacing))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.dont_have_account),
                color = secondaryText
            )
            Text(
                text = stringResource(R.string.sign_up_here),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
    }
}

@Composable
fun SocialButton(
    text: String,
    icon: Int,
    borderColor: Color,
    metrics: SocialButtonMetrics
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.height)
            .clip(RoundedCornerShape(metrics.corner))
            .border(metrics.borderWidth, borderColor, RoundedCornerShape(metrics.corner))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { }
            .padding(horizontal = metrics.horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(metrics.iconGap))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun rememberLoginMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): LoginMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 390.dp).coerceIn(0.86f, 1.22f)
    val heightScale = (maxHeight / 844.dp).coerceIn(0.82f, 1.18f)
    val compactness = minOf(widthScale, heightScale)

    LoginMetrics(
        horizontalPadding = (24.dp * widthScale).coerceIn(20.dp, 34.dp),
        topSpacing = (72.dp * heightScale).coerceIn(46.dp, 88.dp),
        emailTopSpacing = (40.dp * heightScale).coerceIn(28.dp, 48.dp),
        fieldTopSpacing = (8.dp * heightScale).coerceIn(6.dp, 10.dp),
        passwordTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        forgotPasswordTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        signInTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        signInHeight = (56.dp * compactness).coerceIn(50.dp, 62.dp),
        signInCorner = (20.dp * compactness).coerceIn(16.dp, 24.dp),
        dividerVerticalPadding = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        dividerTextHorizontalPadding = (8.dp * widthScale).coerceIn(6.dp, 12.dp),
        socialButtonGap = (15.dp * heightScale).coerceIn(12.dp, 20.dp),
        bottomPromptTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        fieldBorderWidth = (1.5.dp * compactness).coerceIn(1.dp, 2.dp),
        fieldCorner = (16.dp * compactness).coerceIn(14.dp, 20.dp),
        socialButtonMetrics = SocialButtonMetrics(
            height = (56.dp * compactness).coerceIn(50.dp, 62.dp),
            corner = (20.dp * compactness).coerceIn(16.dp, 24.dp),
            borderWidth = (1.5.dp * compactness).coerceIn(1.dp, 2.dp),
            horizontalPadding = (16.dp * widthScale).coerceIn(12.dp, 22.dp),
            iconGap = (12.dp * widthScale).coerceIn(10.dp, 16.dp)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}
