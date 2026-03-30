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
import androidx.compose.ui.unit.dp
import com.example.ibook.R
import com.example.ibook.app_components.inputs.InputKind
import com.example.ibook.app_components.inputs.UniversalInputField

@Composable
fun LoginScreen() {

    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(72.dp))

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
            modifier = Modifier.padding(top = 40.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = secondaryText
        )

        UniversalInputField(
            value = email,
            onValueChange = { email = it },
            hint = stringResource(R.string.email_hint),
            leftIcon = painterResource(R.drawable.ic_email),
            modifier = Modifier
                .padding(top = 8.dp)
                .border(1.5.dp, borderColor, RoundedCornerShape(16.dp)),
            inputType = InputKind.Email,
            borderColor = Color.Transparent
        )

        Text(
            text = stringResource(R.string.password),
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = secondaryText
        )

        UniversalInputField(
            value = password,
            onValueChange = { password = it },
            hint = stringResource(R.string.password_hint),
            leftIcon = painterResource(R.drawable.ic_password),
            modifier = Modifier
                .padding(top = 8.dp)
                .border(1.5.dp, borderColor, RoundedCornerShape(16.dp)),
            inputType = InputKind.Password,
            borderColor = Color.Transparent
        )

        Text(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp)
                .clickable { /* TODO */ },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(20.dp))
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
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )
            Text(
                text = stringResource(R.string.or),
                modifier = Modifier.padding(horizontal = 8.dp),
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
            borderColor = borderColor
        )

        Spacer(modifier = Modifier.height(15.dp))

        SocialButton(
            text = stringResource(R.string.sign_in_with_google),
            icon = R.drawable.ic_google,
            borderColor = borderColor
        )

        Spacer(modifier = Modifier.height(24.dp))

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

@Composable
fun SocialButton(
    text: String,
    icon: Int,
    borderColor: Color
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}