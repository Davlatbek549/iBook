package com.example.dz.designsystem.components.organic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.modifiers.hideKeyboardOnBackgroundTap
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily

/**
 * The Organic bottom sheet: cream ground, 28dp top corners, a short grab handle, a Caprasimo
 * title and an optional line under it. Opens fully expanded — every sheet here is short.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganicBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberBottomSheetState(SheetValue.Hidden, setOf(SheetValue.Hidden, SheetValue.Expanded))
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = OrganicShape.radiusLg, topEnd = OrganicShape.radiusLg),
        containerColor = OrganicColors.bg,
        scrimColor = OrganicColors.neutral900.copy(alpha = 0.42f),
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(OrganicColors.neutral300),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // The sheet is its own window, out of reach of the one on App's root.
                .hideKeyboardOnBackgroundTap()
                .navigationBarsPadding()
                .padding(start = ORGANIC_GUTTER, end = ORGANIC_GUTTER, top = 6.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    fontFamily = organicHeadingFontFamily(),
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    color = OrganicColors.text,
                )
                subtitle?.let {
                    Text(
                        text = it,
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = OrganicColors.neutral700,
                    )
                }
            }
            content()
        }
    }
}

/**
 * A row in an action sheet — "Take photo", "Choose from library". [destructive] rows are drawn in
 * accent-700 on an accent-100 icon disc.
 */
@Composable
fun OrganicSheetAction(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    destructive: Boolean = false,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.45f)
            .clip(RoundedCornerShape(OrganicShape.radiusMd))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (destructive) OrganicColors.accent100 else OrganicColors.neutral200),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(17.dp),
                tint = if (destructive) OrganicColors.accent700 else OrganicColors.neutral800,
            )
        }
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontWeight = if (destructive) FontWeight.Bold else FontWeight.Normal,
            fontSize = 15.sp,
            color = if (destructive) OrganicColors.accent700 else OrganicColors.text,
        )
    }
}

/** A full-width secondary pill — a sheet's Cancel. */
@Composable
fun OrganicSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral200)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = OrganicColors.neutral900,
        )
    }
}

/**
 * A two-choice question over the page: an icon disc, a Caprasimo title, a body that may bold the
 * value it is about, then a secondary and a primary pill side by side.
 */
@Composable
fun OrganicConfirmDialog(
    icon: ImageVector,
    title: String,
    body: AnnotatedString,
    dismissText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmColor: Color = OrganicColors.accent,
    isBusy: Boolean = false,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = !isBusy, dismissOnClickOutside = !isBusy),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(OrganicShape.radiusLg))
                .background(OrganicColors.bg)
                .padding(22.dp),
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(OrganicColors.accent100),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = OrganicColors.accent700)
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = title,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 21.sp,
                lineHeight = 25.sp,
                color = OrganicColors.text,
            )
            Text(
                text = body,
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = OrganicColors.neutral800,
            )
            Spacer(Modifier.height(22.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OrganicSecondaryButton(
                    text = dismissText,
                    onClick = onDismiss,
                    enabled = !isBusy,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(OrganicShape.pill))
                        .background(confirmColor)
                        .clickable(enabled = !isBusy, role = Role.Button, onClick = onConfirm),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isBusy) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = confirmText,
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

/**
 * A short confirmation that floats above the bottom of the screen — "Profile updated". A 60dp
 * neutral-900 pill with a sage check. The caller decides how long it stays; it announces itself to
 * screen readers when it appears.
 */
@Composable
fun OrganicToast(
    visible: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    body: String? = null,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(OrganicShape.pill))
                .background(OrganicColors.neutral900)
                .semantics { liveRegion = LiveRegionMode.Polite }
                .padding(start = 9.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(OrganicColors.accent2_500),
                contentAlignment = Alignment.Center,
            ) {
                Icon(OrganicIcons.Check, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
            }
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = title,
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = OrganicColors.neutral100,
                )
                body?.let {
                    Text(text = it, fontFamily = organicBodyFontFamily(), fontSize = 12.sp, color = OrganicColors.neutral300)
                }
            }
        }
    }
}
