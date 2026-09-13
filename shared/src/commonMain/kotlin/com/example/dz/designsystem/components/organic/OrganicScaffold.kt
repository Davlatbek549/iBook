package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily

/**
 * The frame every Organic screen sits in, and the headers that top it.
 *
 * Values from `dz-all-screens.html`. The handoff draws each screen as a fixed 390 × 844 frame with
 * a mock status bar; on a real device the system status bar takes that role, so what carries over
 * is the ground colour, the 24dp gutter and the clearance the floating tab bar needs.
 */

/** Page gutter — 24dp everywhere except the auth screens, which use the wider `OrganicSize.authGutter`. */
val ORGANIC_GUTTER: Dp = 24.dp

/**
 * Bottom padding a scroll region needs so its last row clears the floating tab bar. The handoff
 * asks for ≥ 92dp, which leaves ~40dp of visible clearance under the content.
 */
val ORGANIC_TAB_BAR_CLEARANCE: Dp = 92.dp

/**
 * Screen ground: the warm cream fill plus status-bar inset. Content lays itself out — this
 * deliberately does not impose a column or a scroll, because the screens differ (Home is a lazy
 * column, the reader is a fixed page).
 */
@Composable
fun OrganicScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
            .statusBarsPadding(),
        content = content
    )
}

/**
 * Top-of-screen title in the display face — "Your shelf", "Your goal". 30sp, with an optional
 * trailing action on the same baseline row.
 */
@Composable
fun OrganicScreenHeader(
    title: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 30.sp,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leading?.invoke()
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = organicHeadingFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            lineHeight = fontSize,
            color = OrganicColors.text
        )
        trailing?.invoke()
    }
}

/**
 * Section heading inside a screen — "Picked for you", "New this week". 21sp display face, with an
 * optional "See all" on the right. Aligned on baselines, not centres, so the small link sits with
 * the heading's baseline rather than its middle.
 */
@Composable
fun OrganicSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    /** An affordance beside the action, for a section that both leads somewhere and does something. */
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            fontFamily = organicHeadingFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 21.sp,
            color = OrganicColors.text
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (actionLabel != null) {
                Text(
                    text = actionLabel,
                    modifier = if (onActionClick != null) {
                        Modifier.clickable(role = Role.Button, onClick = onActionClick)
                    } else {
                        Modifier
                    },
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.accent700
                )
            }
            trailing?.invoke()
        }
    }
}

/**
 * Circular icon button on a neutral-200 ground — the search affordance on Home and Library, the
 * back chevron on pushed screens. 42dp by default; the goal screen draws it at 38dp.
 *
 * [OrganicBackButton] stays separate: it carries the busy state that leaving an auth screen needs.
 */
@Composable
fun OrganicCircleIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 42.dp,
    iconSize: Dp = 19.dp,
    /** The accent fill marks the one button on a screen that makes something new. */
    background: Color = OrganicColors.neutral200,
    tint: Color = OrganicColors.neutral800,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

/**
 * Uppercase kicker — the small label above a card title ("KEEP GOING"). 10–11sp at weight 800 with
 * wide tracking; the colour is the caller's because it changes with the card it sits on.
 */
@Composable
fun OrganicKicker(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 11.sp,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        fontFamily = organicBodyFontFamily(),
        fontWeight = FontWeight.ExtraBold,
        fontSize = fontSize,
        letterSpacing = fontSize * 0.08f,
        color = color
    )
}
