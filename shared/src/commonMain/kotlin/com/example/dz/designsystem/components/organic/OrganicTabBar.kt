package com.example.dz.designsystem.components.organic

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.indication
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import dev.chrisbanes.haze.HazeState

/** One tab bar destination. */
data class OrganicTab(
    val route: String,
    val icon: ImageVector,
    /** Spoken label — the route string is an identifier, not something to read aloud. */
    val label: String,
)

/**
 * The floating tab bar: a glass pill inset from both edges and lifted off the bottom, with the
 * active destination marked by a terracotta circle.
 *
 * It floats *over* the scroll region rather than reserving space below it, which is why it carries
 * its own insets instead of sitting in a `Scaffold` bottom-bar slot. Screens underneath pay for the
 * overlap with bottom padding — see [ORGANIC_TAB_BAR_CLEARANCE].
 *
 * Geometry from `dz-all-screens.html`: inset 24, bottom 22, height 64, pill radius, `--shadow-md`;
 * each item a 44dp circle with a 20dp icon.
 *
 * Two deliberate departures from the handoff, both requested:
 * - The pill is [organicGlass] rather than an opaque `--color-neutral-900` fill. Pass a null
 *   [backdrop] to get the flat fill the handoff draws.
 * - Each item owns a full fifth of the bar's width as its tap target, with the 44dp circle centred
 *   inside it as the visual mark. The handoff's `space-around` left the gaps between circles inert,
 *   so a tap that landed a few dp wide of an icon did nothing. Equal slices with centred content
 *   put the circles in exactly the same places `space-around` did — the geometry is unchanged, only
 *   the dead space is gone.
 */
@Composable
fun OrganicTabBar(
    tabs: List<OrganicTab>,
    currentRoute: String?,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    backdrop: HazeState? = null,
) {
    val shape = RoundedCornerShape(OrganicShape.pill)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 22.dp)
    ) {
        // The design's 44dp circle assumes a 390dp screen. Narrower phones do not leave room for
        // five of them, so the mark gives way rather than colliding with the pill's edge — the tap
        // target stays a full slice either way.
        val markSize = (maxWidth / tabs.size - 6.dp).coerceAtMost(44.dp)

        // The handoff's neutral-300 is drawn against a solid neutral-900 pill. Glass is lighter
        // and, worse, varies with whatever scrolls behind it, so on glass the inactive icons go
        // brighter to hold their contrast over a pale shelf of book covers.
        val inactiveTint = if (backdrop != null) {
            Color.White.copy(alpha = 0.78f)
        } else {
            OrganicColors.neutral300
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = shape,
                    ambientColor = OrganicColors.shadow.copy(alpha = 0.16f),
                    spotColor = OrganicColors.shadow.copy(alpha = 0.16f)
                )
                .then(
                    if (backdrop != null) {
                        Modifier.organicGlass(backdrop = backdrop, shape = shape)
                    } else {
                        Modifier.clip(shape).background(OrganicColors.neutral900)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                OrganicTabItem(
                    tab = tab,
                    selected = currentRoute == tab.route,
                    markSize = markSize,
                    inactiveTint = inactiveTint,
                    onClick = { onTabClick(tab.route) }
                )
            }
        }
    }
}

/**
 * One destination. The slice is the tap target; the circle is only the mark.
 *
 * The ripple is bound to the circle rather than the slice — a tap anywhere in the slice should
 * register, but the feedback belongs on the thing that looks like a button.
 */
@Composable
private fun RowScope.OrganicTabItem(
    tab: OrganicTab,
    selected: Boolean,
    markSize: Dp,
    inactiveTint: Color,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (selected) OrganicColors.accent else Color.Transparent,
        label = "tabBackground"
    )
    val tint by animateColorAsState(
        targetValue = if (selected) Color.White else inactiveTint,
        label = "tabTint"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(markSize)
                .clip(CircleShape)
                .background(background)
                .indication(interactionSource, ripple()),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = tint,
                modifier = Modifier.size(if (markSize < 44.dp) markSize * 0.45f else 20.dp)
            )
        }
    }
}
