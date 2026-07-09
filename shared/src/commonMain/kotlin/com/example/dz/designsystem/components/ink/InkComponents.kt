package com.example.dz.designsystem.components.ink

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import com.example.dz.designsystem.theme.inkDisplayFontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.remote.RemoteBookCover
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/** Primary action button — accent ground, 52dp tall. */
@Composable
fun InkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    colors: InkColors = inkColors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(InkShape.radiusSm + 2.dp))
            .background(colors.accent)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally)
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = colors.onAccent,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = text,
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.5.sp,
            color = colors.onAccent
        )
    }
}

/** Outlined secondary button — transparent ground, hairline border, ink label. */
@Composable
fun InkSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    colors: InkColors = inkColors(),
) {
    val shape = RoundedCornerShape(InkShape.radiusSm + 2.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .border(1.dp, colors.line, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = colors.ink
        )
    }
}

/** Text-only ghost button — accent label, no ground. */
@Composable
fun InkGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 44.dp,
    colors: InkColors = inkColors(),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = colors.accent
        )
    }
}

/** Pill chip — solid uses the soft accent ground, otherwise hairline outline. */
@Composable
fun InkChip(
    text: String,
    solid: Boolean = false,
    colors: InkColors = inkColors(),
) {
    val shape = CircleShape
    Box(
        modifier = Modifier
            .height(26.dp)
            .clip(shape)
            .let {
                if (solid) it.background(colors.accentSoft)
                else it.border(1.dp, colors.line, shape)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp),
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = if (solid) colors.accent else colors.inkSoft
        )
    }
}

/** Small-caps eyebrow label. */
@Composable
fun InkLabel(
    text: String,
    colors: InkColors = inkColors(),
) {
    Text(
        text = text.uppercase(),
        fontFamily = inkBodyFontFamily(),
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.5.sp,
        letterSpacing = 1.5.sp,
        color = colors.muted
    )
}

/** Page indicator — active dot stretches into a short accent bar. */
@Composable
fun InkDots(
    count: Int,
    activeIndex: Int,
    colors: InkColors = inkColors(),
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { i ->
            val active = i == activeIndex
            Box(
                modifier = Modifier
                    .width(if (active) 18.dp else 5.dp)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(if (active) colors.accent else colors.line)
            )
        }
    }
}

/** Flat "ink" card ground: surface fill with a hairline border. */
@Composable
fun Modifier.inkCard(
    colors: InkColors = inkColors(),
    shape: RoundedCornerShape = RoundedCornerShape(InkShape.radius),
): Modifier = this
    .clip(shape)
    .background(colors.surface)
    .border(1.dp, colors.line, shape)

/** Single-line input — hairline border that turns accent on focus, optional password reveal. */
@Composable
fun InkField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    colors: InkColors = inkColors(),
) {
    var focused by remember { mutableStateOf(false) }
    var revealed by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(InkShape.radiusSm + 2.dp)
    val bodyFont = inkBodyFontFamily()

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(shape)
            .background(colors.surface)
            .border(1.dp, if (focused) colors.accent else colors.line, shape)
            .onFocusChanged { focused = it.isFocused },
        textStyle = TextStyle(
            fontFamily = bodyFont,
            fontSize = 13.5.sp,
            color = colors.ink
        ),
        singleLine = true,
        cursorBrush = SolidColor(colors.accent),
        visualTransformation = if (isPassword && !revealed) PasswordVisualTransformation('•') else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                leadingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = if (focused) colors.accent else colors.muted,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontFamily = bodyFont,
                            fontSize = 13.5.sp,
                            color = colors.muted,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
                if (isPassword) {
                    Icon(
                        imageVector = if (revealed) InkIcons.Eye else InkIcons.EyeOff,
                        contentDescription = null,
                        tint = colors.muted,
                        modifier = Modifier
                            .size(17.dp)
                            .clickable { revealed = !revealed }
                    )
                }
            }
        }
    )
}

/** 40dp square hairline icon button. */
@Composable
fun InkIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: InkColors = inkColors(),
) {
    val shape = RoundedCornerShape(InkShape.radiusSm)
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(shape)
            .border(1.dp, colors.line, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.ink,
            modifier = Modifier.size(18.dp)
        )
    }
}

/** Hairline divider with a small word in the middle ("or"). */
@Composable
fun InkOrDivider(
    text: String,
    colors: InkColors = inkColors(),
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = colors.line)
        Text(
            text = text,
            fontFamily = inkBodyFontFamily(),
            fontSize = 11.5.sp,
            color = colors.muted
        )
        HorizontalDivider(modifier = Modifier.weight(1f), thickness = 1.dp, color = colors.line)
    }
}

/** Outlined social sign-in button with an official brand glyph. */
@Composable
fun InkSocialButton(
    icon: DrawableResource,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: InkColors = inkColors(),
) {
    val shape = RoundedCornerShape(InkShape.radiusSm + 2.dp)
    Row(
        modifier = modifier
            .height(50.dp)
            .clip(shape)
            .background(colors.surface)
            .border(1.dp, colors.line, shape)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(17.dp)
        )
        Text(
            text = label,
            fontFamily = inkBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = colors.ink
        )
    }
}

/** Serif section heading with an optional accent action ("See all"). */
@Composable
fun InkSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onActionClick: () -> Unit = {},
    colors: InkColors = inkColors(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontFamily = inkDisplayFontFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = colors.ink
        )
        if (action != null) {
            Text(
                text = action,
                modifier = Modifier.clickable(onClick = onActionClick),
                fontFamily = inkBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = colors.accent
            )
        }
    }
}

/** Thin reading-progress track: alt ground with an accent fill. */
@Composable
fun InkProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    colors: InkColors = inkColors(),
) {
    Box(
        modifier = modifier
            .height(4.dp)
            .clip(CircleShape)
            .background(colors.alt)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(4.dp)
                .clip(CircleShape)
                .background(colors.accent)
        )
    }
}

/** List row: small cover, serif title, muted author, optional meta and trailing slots. */
@Composable
fun InkBookRow(
    cover: DrawableResource,
    title: String,
    author: String,
    modifier: Modifier = Modifier,
    coverUrl: String? = null,
    showDivider: Boolean = false,
    meta: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    colors: InkColors = inkColors(),
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (showDivider) {
            HorizontalDivider(thickness = 1.dp, color = colors.line)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            RemoteBookCover(
                coverUrl = coverUrl,
                fallback = cover,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 46.dp, height = 66.dp)
                    .shadow(5.dp, RoundedCornerShape(InkShape.cover), clip = true)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = inkDisplayFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colors.ink
                )
                Text(
                    text = author,
                    modifier = Modifier.padding(top = 3.dp),
                    fontFamily = inkBodyFontFamily(),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colors.muted
                )
                if (meta != null) {
                    Box(modifier = Modifier.padding(top = 6.dp)) { meta() }
                }
            }
            trailing?.invoke()
        }
    }
}

/** Detail-screen top bar: back button, serif title with optional subtitle, optional right slot. */
@Composable
fun InkTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    right: (@Composable () -> Unit)? = null,
    colors: InkColors = inkColors(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        InkIconButton(icon = InkIcons.Back, onClick = onBackClick, colors = colors)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = inkDisplayFontFamily(),
                fontWeight = FontWeight.Medium,
                fontSize = 19.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colors.ink
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(top = 3.dp),
                    fontFamily = inkBodyFontFamily(),
                    fontSize = 11.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colors.muted
                )
            }
        }
        right?.invoke()
    }
}

/** Pill toggle switch in the ink style. */
@Composable
fun InkToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colors: InkColors = inkColors(),
) {
    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 24.dp)
            .clip(CircleShape)
            .background(if (checked) colors.accent else colors.alt)
            .border(1.dp, if (checked) colors.accent else colors.line, CircleShape)
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked) colors.onAccent else colors.surface)
        )
    }
}

/** Muted footer line with an accent action ("New here? Create account"). */
@Composable
fun InkFooterLink(
    prefix: String,
    action: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: InkColors = inkColors(),
) {
    Text(
        text = buildAnnotatedString {
            append(prefix)
            append(" ")
            withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.SemiBold)) {
                append(action)
            }
        },
        modifier = modifier.clickable(onClick = onClick),
        fontFamily = inkBodyFontFamily(),
        fontSize = 13.sp,
        color = colors.muted
    )
}
