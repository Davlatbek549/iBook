package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily

/**
 * Large pill action button in the "Organic" voice — accent fill, 58dp tall,
 * with standard Android pressed/ripple feedback baked in via [Modifier.clickable].
 * When [trailingArrow] is set the chevron is drawn *inside* this same clickable
 * row (never a separate tappable element).
 */
@Composable
fun OrganicPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    trailingArrow: Boolean = false,
) {
    Row(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
            .height(58.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(OrganicShape.pill),
                ambientColor = OrganicColors.shadow.copy(alpha = 0.22f),
                spotColor = OrganicColors.shadow.copy(alpha = 0.22f)
            )
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.accent)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.White
            )
            if (trailingArrow) {
                Icon(
                    imageVector = InkIcons.ArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * One pagination dot. The visible mark is small (8dp) but the tappable area
 * is expanded to a comfortable 44dp touch target, per Android accessibility
 * guidance.
 */
@Composable
private fun RowScope.OrganicDot(
    active: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(if (active) 26.dp else 8.dp)
                .height(8.dp)
                .clip(CircleShape)
                .background(if (active) OrganicColors.accent else OrganicColors.neutral300)
        )
    }
}

/** Row of pagination dots — tap any dot to jump to that page. */
@Composable
fun OrganicPaginationDots(
    pageCount: Int,
    activeIndex: Int,
    onDotClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            OrganicDot(active = index == activeIndex, onClick = { onDotClick(index) })
        }
    }
}
