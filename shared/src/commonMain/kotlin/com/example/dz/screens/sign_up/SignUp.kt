package com.example.dz.screens.sign_up

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dz.app_components.buttons.app_button.AppButton
import com.example.dz.app_components.inputs.InputKind
import com.example.dz.app_components.inputs.UniversalInputField
import com.example.dz.screens.login.SocialButton
import com.example.dz.screens.login.SocialButtonMetrics
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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

private data class SignUpMetrics(
    val topSpacing: Dp,
    val cardCorner: Dp,
    val cardPadding: Dp,
    val titleSubtitleGap: Dp,
    val firstFieldTopSpacing: Dp,
    val labelFieldGap: Dp,
    val fieldGroupGap: Dp,
    val phoneCorner: Dp,
    val phoneBorderWidth: Dp,
    val phoneHorizontalPadding: Dp,
    val phoneVerticalPadding: Dp,
    val countryFlagGap: Dp,
    val countryIconSize: Dp,
    val phoneDividerHorizontalGap: Dp,
    val phoneDividerWidth: Dp,
    val phoneDividerHeight: Dp,
    val termsCheckboxTopPadding: Dp,
    val termsTextStartPadding: Dp,
    val signUpButtonTopSpacing: Dp,
    val signUpButtonHeight: Dp,
    val dividerTopSpacing: Dp,
    val socialTopSpacing: Dp,
    val socialButtonGap: Dp,
    val footerTopSpacing: Dp,
    val socialButtonMetrics: SocialButtonMetrics
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        val metrics = rememberSignUpMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(metrics.topSpacing))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(metrics.cardCorner))
                    .background(colors.surface)
                    .padding(metrics.cardPadding)
            ) {

            Text(
                text = stringResource(Res.string.sign_up_with_email),
                style = typography.headlineLarge,
                color = primaryText
            )

            Spacer(modifier = Modifier.height(metrics.titleSubtitleGap))

            Text(
                text = stringResource(Res.string.create_account),
                style = typography.bodyMedium,
                color = secondaryText
            )

            Spacer(modifier = Modifier.height(metrics.firstFieldTopSpacing))


            Text(
                text = stringResource(Res.string.email),
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(metrics.labelFieldGap))

            UniversalInputField(
                value = email,
                onValueChange = { email = it },
                hint = stringResource(Res.string.email_hint),
                leftIcon = painterResource(Res.drawable.ic_email),
                inputType = InputKind.Email
            )

            Spacer(modifier = Modifier.height(metrics.fieldGroupGap))

            Text(
                text = stringResource(Res.string.phone_number),
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(metrics.labelFieldGap))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(metrics.phoneCorner))
                    .border(
                        metrics.phoneBorderWidth,
                        colors.outlineVariant,
                        RoundedCornerShape(metrics.phoneCorner)
                    )
                    .background(colors.surface)
                    .padding(
                        horizontal = metrics.phoneHorizontalPadding,
                        vertical = metrics.phoneVerticalPadding
                    ),
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

                    Spacer(modifier = Modifier.width(metrics.countryFlagGap))

                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_drop_down),
                        contentDescription = stringResource(Res.string.choose_your_country),
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(metrics.countryIconSize)
                    )
                }

                Spacer(modifier = Modifier.width(metrics.phoneDividerHorizontalGap))

                Box(
                    modifier = Modifier
                        .width(metrics.phoneDividerWidth)
                        .height(metrics.phoneDividerHeight)
                        .background(colors.outlineVariant)
                )

                Spacer(modifier = Modifier.width(metrics.phoneDividerHorizontalGap))

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
                                    text = stringResource(Res.string.phone_number_hint),
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

            Spacer(modifier = Modifier.height(metrics.fieldGroupGap))
            Text(
                text = stringResource(Res.string.password),
                style = typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(metrics.labelFieldGap))

            UniversalInputField(
                value = password,
                onValueChange = { password = it },
                hint = stringResource(Res.string.password_hint),
                leftIcon = painterResource(Res.drawable.ic_password),
                inputType = InputKind.Password
            )

            Spacer(modifier = Modifier.height(metrics.fieldGroupGap))

            // TERMS - FIXED
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    modifier = Modifier.padding(top = metrics.termsCheckboxTopPadding)
                )

                Column(modifier = Modifier.padding(start = metrics.termsTextStartPadding)) {
                    Text(
                        text = stringResource(Res.string.agreement),
                        style = typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(Res.string.terms_and_condition),
                        style = typography.bodySmall,
                        color = colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(metrics.signUpButtonTopSpacing))

            AppButton(
                onClick = { },
                text = stringResource(Res.string.sign_up),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(metrics.signUpButtonHeight)
            )

            Spacer(modifier = Modifier.height(metrics.dividerTopSpacing))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
                Text(
                    text = stringResource(Res.string.or),
                    style = typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
            }

            Spacer(modifier = Modifier.height(metrics.socialTopSpacing))

            SocialButton(
                text = stringResource(Res.string.sign_up_with_apple),
                icon = Res.drawable.ic_apple,
                borderColor = colors.outline,
                metrics = metrics.socialButtonMetrics
            )

            Spacer(modifier = Modifier.height(metrics.socialButtonGap))

            SocialButton(
                text = stringResource(Res.string.sign_up_with_google),
                icon = Res.drawable.ic_google,
                borderColor = colors.outline,
                metrics = metrics.socialButtonMetrics
            )

            Spacer(modifier = Modifier.height(metrics.footerTopSpacing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.have_an_account),
                    style = typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )

                Text(
                    text = stringResource(Res.string.sign_in_here),
                    style = typography.bodyMedium,
                    color = colors.primary,
                    modifier = Modifier.clickable { onSignInClick() }
                )
            }
        }
        }
    }
}

@Composable
private fun rememberSignUpMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): SignUpMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 390.dp).coerceIn(0.86f, 1.2f)
    val heightScale = (maxHeight / 844.dp).coerceIn(0.78f, 1.16f)
    val compactness = minOf(widthScale, heightScale)

    SignUpMetrics(
        topSpacing = (30.dp * heightScale).coerceIn(18.dp, 38.dp),
        cardCorner = (20.dp * compactness).coerceIn(16.dp, 24.dp),
        cardPadding = (20.dp * widthScale).coerceIn(16.dp, 26.dp),
        titleSubtitleGap = (6.dp * heightScale).coerceIn(4.dp, 8.dp),
        firstFieldTopSpacing = (20.dp * heightScale).coerceIn(14.dp, 26.dp),
        labelFieldGap = (6.dp * heightScale).coerceIn(4.dp, 8.dp),
        fieldGroupGap = (16.dp * heightScale).coerceIn(10.dp, 20.dp),
        phoneCorner = (24.dp * compactness).coerceIn(18.dp, 28.dp),
        phoneBorderWidth = (1.dp * compactness).coerceIn(1.dp, 1.5.dp),
        phoneHorizontalPadding = (16.dp * widthScale).coerceIn(12.dp, 20.dp),
        phoneVerticalPadding = (14.dp * heightScale).coerceIn(10.dp, 16.dp),
        countryFlagGap = (4.dp * widthScale).coerceIn(3.dp, 6.dp),
        countryIconSize = (18.dp * compactness).coerceIn(16.dp, 22.dp),
        phoneDividerHorizontalGap = (12.dp * widthScale).coerceIn(8.dp, 16.dp),
        phoneDividerWidth = (1.dp * widthScale).coerceIn(1.dp, 1.5.dp),
        phoneDividerHeight = (24.dp * compactness).coerceIn(20.dp, 28.dp),
        termsCheckboxTopPadding = (2.dp * heightScale).coerceIn(1.dp, 4.dp),
        termsTextStartPadding = (8.dp * widthScale).coerceIn(6.dp, 12.dp),
        signUpButtonTopSpacing = (20.dp * heightScale).coerceIn(14.dp, 24.dp),
        signUpButtonHeight = (55.dp * compactness).coerceIn(50.dp, 62.dp),
        dividerTopSpacing = (20.dp * heightScale).coerceIn(14.dp, 24.dp),
        socialTopSpacing = (20.dp * heightScale).coerceIn(14.dp, 24.dp),
        socialButtonGap = (12.dp * heightScale).coerceIn(8.dp, 16.dp),
        footerTopSpacing = (20.dp * heightScale).coerceIn(14.dp, 24.dp),
        socialButtonMetrics = SocialButtonMetrics(
            height = (56.dp * compactness).coerceIn(50.dp, 62.dp),
            corner = (20.dp * compactness).coerceIn(16.dp, 24.dp),
            borderWidth = (1.5.dp * compactness).coerceIn(1.dp, 2.dp),
            horizontalPadding = (16.dp * widthScale).coerceIn(12.dp, 22.dp),
            iconGap = (12.dp * widthScale).coerceIn(10.dp, 16.dp)
        )
    )
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    widthDp = 375,
    heightDp = 820
)
@Composable
fun SignUpScreenPreview() {
    DZTheme {
        SignUpScreen()
    }
}
