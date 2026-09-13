package com.example.dz.designsystem.components.organic

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily

/**
 * The controls: the toggle, the filter pills, and the trailing chevron.
 *
 * Geometry from `dz-all-screens.html`.
 */

/**
 * 52 × 30 pill toggle with a 24dp white knob. Off is neutral-300, on is terracotta.
 *
 * The knob slides rather than jumping — the design specifies only the two end states, but a switch
 * that teleports reads as a redraw instead of a response.
 */
@Composable
fun OrganicToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val track by animateColorAsState(
        targetValue = if (checked) OrganicColors.accent else OrganicColors.neutral300,
        label = "toggleTrack"
    )
    // 52 wide, 3 padding either side, 24 knob → 22dp of travel.
    val knobOffset by animateDpAsState(
        targetValue = if (checked) 22.dp else 0.dp,
        label = "toggleKnob"
    )

    Box(
        modifier = modifier
            .width(52.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(track)
            .clickable(
                enabled = enabled,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            )
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = knobOffset)
                .size(24.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    ambientColor = OrganicColors.shadow.copy(alpha = 0.14f),
                    spotColor = OrganicColors.shadow.copy(alpha = 0.14f)
                )
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

/**
 * One filter pill — Library's shelf states, the store's moods, the notification filters. Selected
 * is an accent fill with white text; the rest are neutral-200 with neutral-800.
 */
@Composable
fun OrganicFilterPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(
        targetValue = if (selected) OrganicColors.accent else OrganicColors.neutral200,
        label = "pillBackground"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else OrganicColors.neutral800,
        label = "pillText"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(background)
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 9.dp)
    ) {
        Text(
            text = label,
            fontFamily = organicBodyFontFamily(),
            fontSize = 13.sp,
            color = textColor
        )
    }
}

/** A row of [OrganicFilterPill]s, 8dp apart. */
@Composable
fun OrganicFilterPills(
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        labels.forEachIndexed { index, label ->
            OrganicFilterPill(
                label = label,
                selected = index == selectedIndex,
                onClick = { onSelect(index) }
            )
        }
    }
}

/**
 * The trailing chevron every tappable row ends with. 15dp, neutral-600 by default — the sage cards
 * tint it accent-2-800 instead.
 *
 * Never given its own click handler: the row that contains it is the tap target.
 */
@Composable
fun OrganicRowChevron(
    modifier: Modifier = Modifier,
    tint: Color = OrganicColors.neutral600,
) {
    Icon(
        imageVector = OrganicIcons.ChevronRight,
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(15.dp)
    )
}
