package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.OrganicSize
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicColors
import com.example.dz.designsystem.theme.organicHeadingFontFamily

/**
 * The parts every Organic auth screen is assembled from. The handoff's own note applies: almost
 * every screen is these pieces rearranged, so they are built once here rather than per screen.
 *
 * Values come from `dz-all-screens.html`, which the bundle names as where the exact numbers live.
 */

/** 42dp circular back button on a neutral-200 ground. */
@Composable
fun OrganicBackButton(
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    colors: OrganicColors = organicColors(),
) {
    Box(
        modifier = modifier
            .size(OrganicSize.backButton)
            .clip(CircleShape)
            .background(colors.neutral200)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = InkIcons.Back,
            contentDescription = contentDescription,
            tint = colors.neutral800,
            modifier = Modifier.size(18.dp)
        )
    }
}

/** Screen heading in the display face. 32sp on the two-line auth titles, 30sp elsewhere. */
@Composable
fun OrganicTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 32.sp,
    colors: OrganicColors = organicColors(),
) {
    Text(
        text = text,
        modifier = modifier,
        fontFamily = organicHeadingFontFamily(),
        fontWeight = FontWeight.Normal,
        fontSize = fontSize,
        lineHeight = fontSize * 1.15f,
        color = colors.text
    )
}

/** Supporting line under a title. */
@Composable
fun OrganicSubtitle(
    text: String,
    modifier: Modifier = Modifier,
    colors: OrganicColors = organicColors(),
) {
    Text(
        text = text,
        modifier = modifier,
        fontFamily = organicBodyFontFamily(),
        fontSize = 15.sp,
        lineHeight = 24.sp,
        color = colors.neutral700
    )
}

/**
 * Uppercase field kicker. Sign-in labels its inputs this way; sign-up and the recovery screens
 * rely on placeholders instead, so this is opt-in per field.
 */
@Composable
fun OrganicFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
    colors: OrganicColors = organicColors(),
) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        fontFamily = organicBodyFontFamily(),
        fontSize = 12.sp,
        letterSpacing = 0.6.sp,
        color = colors.neutral700
    )
}

/**
 * Filled 56dp input with a 28dp radius. The border is neutral until the field is focused or
 * wrong, matching the handoff's "no visible border until focus" rule — the resting hairline is
 * neutral-300, which reads as part of the fill rather than an outline.
 *
 * [trailing] carries the "Show" affordance, which the design writes as a word rather than an eye.
 */
@Composable
fun OrganicField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    showLabel: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: OrganicColors = organicColors(),
) {
    var focused by remember { mutableStateOf(false) }
    var revealed by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(OrganicShape.lg)
    val body = organicBodyFontFamily()

    val borderColor = when {
        isError -> colors.danger
        focused -> colors.accent
        else -> colors.neutral300
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(7.dp)) {
        label?.let { OrganicFieldLabel(text = it, colors = colors) }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(OrganicSize.fieldHeight)
                .clip(shape)
                .background(colors.neutral100)
                .border(1.dp, borderColor, shape)
                .onFocusChanged { focused = it.isFocused },
            textStyle = TextStyle(
                fontFamily = body,
                fontSize = if (isPassword && !revealed) 17.sp else 15.sp,
                // The masked dots are tracked out in the design; unmasked text is not.
                letterSpacing = if (isPassword && !revealed) 3.sp else 0.sp,
                color = colors.text
            ),
            singleLine = true,
            cursorBrush = SolidColor(colors.accent),
            visualTransformation =
                if (isPassword && !revealed) PasswordVisualTransformation('•')
                else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(horizontal = 22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontFamily = body,
                                fontSize = 15.sp,
                                color = colors.neutral500,
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    }
                    if (isPassword && showLabel != null) {
                        Text(
                            text = showLabel,
                            modifier = Modifier
                                .clickable { revealed = !revealed }
                                .padding(start = 10.dp, top = 8.dp, bottom = 8.dp),
                            fontFamily = body,
                            fontSize = 12.sp,
                            color = if (focused) colors.accent700 else colors.neutral600
                        )
                    }
                }
            }
        )

        errorMessage?.let { message ->
            Text(
                text = message,
                modifier = Modifier.padding(start = 6.dp),
                fontFamily = body,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = colors.danger
            )
        }
    }
}

/** Full-width pill action, 58dp, terracotta fill with a medium shadow. */
@Composable
fun OrganicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isBusy: Boolean = false,
    colors: OrganicColors = organicColors(),
) {
    val interactive = enabled && !isBusy
    val shape = RoundedCornerShape(OrganicShape.pill)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(OrganicSize.buttonHeight)
            .shadow(if (interactive) 6.dp else 0.dp, shape, clip = false)
            .clip(shape)
            .background(if (interactive) colors.accent else colors.neutral400)
            .clickable(enabled = interactive, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        if (isBusy) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        }
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

/** Outlined 54dp pill for federated sign-in. */
@Composable
fun OrganicSocialButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    colors: OrganicColors = organicColors(),
) {
    val shape = RoundedCornerShape(OrganicShape.pill)
    Row(
        modifier = modifier
            .height(OrganicSize.socialButtonHeight)
            .clip(shape)
            .background(if (enabled) colors.neutral100 else colors.neutral200)
            .border(1.dp, colors.neutral300, shape)
            .clickable(enabled = enabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = if (enabled) colors.text else colors.neutral500,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = label,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = if (enabled) colors.text else colors.neutral500
        )
    }
}

/** Hairline rule with a word in the middle. */
@Composable
fun OrganicDivider(
    text: String,
    modifier: Modifier = Modifier,
    colors: OrganicColors = organicColors(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f).height(1.dp).background(colors.neutral300))
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontSize = 13.sp,
            color = colors.neutral600
        )
        Box(modifier = Modifier.weight(1f).height(1.dp).background(colors.neutral300))
    }
}

/** 24dp square checkbox, 8dp radius, terracotta when ticked. */
@Composable
fun OrganicCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    colors: OrganicColors = organicColors(),
) {
    val shape = RoundedCornerShape(OrganicShape.sm)
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(shape)
            .background(if (checked) colors.accent else colors.neutral200)
            .then(
                if (checked) Modifier else Modifier.border(1.dp, colors.neutral400, shape)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = InkIcons.Done,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

/**
 * Three-segment password strength bar. Sage — not terracotta — is the "good" colour here: the
 * handoff reserves the accent for actions, and gives the second voice to feedback.
 */
@Composable
fun OrganicStrengthMeter(
    filled: Int,
    label: String,
    modifier: Modifier = Modifier,
    colors: OrganicColors = organicColors(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(if (index < filled) colors.accent2 else colors.neutral300)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontFamily = organicBodyFontFamily(),
            fontSize = 12.sp,
            color = colors.neutral700
        )
    }
}

/**
 * Fixed-length code entry: [length] boxes driven by one hidden field, so the platform keyboard,
 * paste and SMS autofill behave normally instead of fighting per-box focus.
 *
 * [onComplete] fires on the last digit — the only action available at that point is "submit".
 */
@Composable
fun OrganicCodeField(
    code: String,
    onCodeChange: (String) -> Unit,
    length: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onComplete: () -> Unit = {},
    colors: OrganicColors = organicColors(),
) {
    var focused by remember { mutableStateOf(false) }
    val heading = organicHeadingFontFamily()
    val shape = RoundedCornerShape(20.dp)

    BasicTextField(
        value = code,
        onValueChange = { entered ->
            val digits = entered.filter { it.isDigit() }.take(length)
            if (digits != code) {
                onCodeChange(digits)
                if (digits.length == length) onComplete()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.isFocused },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box {
                Box(modifier = Modifier.size(0.dp)) { innerTextField() }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(length) { index ->
                        val filled = index < code.length
                        val active = focused && index == code.length
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(OrganicSize.codeBoxHeight)
                                .clip(shape)
                                .background(colors.neutral100)
                                .border(
                                    width = 1.dp,
                                    color = when {
                                        isError -> colors.danger
                                        filled || active -> colors.accent
                                        else -> colors.neutral300
                                    },
                                    shape = shape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            val digit = code.getOrNull(index)?.toString()
                            if (digit != null) {
                                Text(
                                    text = digit,
                                    fontFamily = heading,
                                    fontSize = 24.sp,
                                    color = colors.text
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
