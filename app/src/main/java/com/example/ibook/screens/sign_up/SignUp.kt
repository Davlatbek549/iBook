package com.example.ibook.screens.sign_up

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibook.R
import com.example.ibook.app_components.buttons.AppButton
import com.example.ibook.app_components.inputs.InputKind
import com.example.ibook.app_components.inputs.UniversalInputField
import com.example.ibook.screens.login.SocialButton

// Data class for country information
data class Country(
    val name: String,
    val countryCode: String,
    val dialCode: String,
    val flag: String // Unicode flag emoji
)

val COUNTRIES = listOf(
    Country("Poland", "PL", "+48", "🇵🇱"),
    Country("Uzbekistan", "UZ", "+998", "🇺🇿"),
    Country("United States", "US", "+1", "🇺🇸"),
    Country("United Kingdom", "GB", "+44", "🇬🇧"),
    Country("Germany", "DE", "+49", "🇩🇪"),
    Country("France", "FR", "+33", "🇫🇷"),
    Country("Spain", "ES", "+34", "🇪🇸"),
    Country("Italy", "IT", "+39", "🇮🇹"),
    Country("Russia", "RU", "+7", "🇷🇺"),
    Country("Ukraine", "UA", "+380", "🇺🇦"),
    Country("Kazakhstan", "KZ", "+7", "🇰🇿"),
    Country("Turkey", "TR", "+90", "🇹🇷"),
    Country("India", "IN", "+91", "🇮🇳"),
    Country("China", "CN", "+86", "🇨🇳"),
    Country("Japan", "JP", "+81", "🇯🇵"),
    Country("Canada", "CA", "+1", "🇨🇦"),
    Country("Australia", "AU", "+61", "🇦🇺"),
    Country("Mexico", "MX", "+52", "🇲🇽"),
    Country("Brazil", "BR", "+55", "🇧🇷"),
    Country("South Korea", "KR", "+82", "🇰🇷"),
)

@Composable
fun SignUpScreen(onSignInClick: () -> Unit = {}) {

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(COUNTRIES[0]) } // Default to first country
    var expandedCountries by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = primaryText.copy(alpha = 0.7f)

    val filteredCountries = if (searchQuery.isEmpty()) {
        COUNTRIES
    } else {
        COUNTRIES.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.dialCode.contains(searchQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colors.surface)
                .padding(20.dp)
        ) {

            Text(
                text = stringResource(R.string.sign_up_with_email),
                style = typography.headlineLarge,
                color = primaryText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.create_account),
                style = typography.bodyMedium,
                color = secondaryText
            )

            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = stringResource(R.string.email),
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            UniversalInputField(
                value = email,
                onValueChange = { email = it },
                hint = stringResource(R.string.email_hint),
                leftIcon = painterResource(id = R.drawable.ic_email),
                inputType = InputKind.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.phone_number),
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, colors.outlineVariant, RoundedCornerShape(24.dp))
                    .background(colors.surface)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { expandedCountries = !expandedCountries },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCountry.flag,
                        style = typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(R.string.choose_your_country),
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(colors.outlineVariant)
                )

                Spacer(modifier = Modifier.width(12.dp))

                BasicTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    singleLine = true,
                    textStyle = typography.bodyMedium.copy(color = colors.onSurface),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        Box {
                            if (phone.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.phone_number_hint),
                                    style = typography.bodyMedium,
                                    color = colors.onSurfaceVariant
                                )
                            }
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.password),
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            UniversalInputField(
                value = password,
                onValueChange = { password = it },
                hint = stringResource(R.string.password_hint),
                leftIcon = painterResource(id = R.drawable.ic_password),
                inputType = InputKind.Password
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TERMS - FIXED
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    modifier = Modifier.padding(top = 2.dp)
                )

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = stringResource(R.string.agreement),
                        style = typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.terms_and_condition),
                        style = typography.bodySmall,
                        color = colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AppButton(
                onClick = { },
                text = stringResource(R.string.sign_up),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
                Text(
                    text = stringResource(R.string.or),
                    style = typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SocialButton(
                text = stringResource(R.string.sign_up_with_apple),
                icon = R.drawable.ic_apple,
                borderColor = colors.outline
            )

            Spacer(modifier = Modifier.height(12.dp))

            SocialButton(
                text = stringResource(R.string.sign_up_with_google),
                icon = R.drawable.ic_google,
                borderColor = colors.outline
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.have_an_account),
                    style = typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )

                Text(
                    text = stringResource(R.string.sign_in_here),
                    style = typography.bodyMedium,
                    color = colors.primary,
                    modifier = Modifier.clickable { onSignInClick() }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SignUpScreenPreview() {
    MaterialTheme {
        SignUpScreen()
    }
}