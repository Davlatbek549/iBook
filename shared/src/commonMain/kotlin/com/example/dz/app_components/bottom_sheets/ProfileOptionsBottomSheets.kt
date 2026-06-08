package com.example.dz.app_components.bottom_sheets

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ic_gift
import dz.shared.generated.resources.ic_help_centre
import dz.shared.generated.resources.ic_settings
import dz.shared.generated.resources.ic_stats
import dz.shared.generated.resources.ic_user
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private data class ProfileOptionsBottomSheetMetrics(
    val sheetHeight: Dp,
    val sheetCorner: Dp,
    val sheetHorizontalPadding: Dp,
    val handleTopSpacing: Dp,
    val handleWidth: Dp,
    val handleHeight: Dp,
    val contentTopSpacing: Dp,
    val rowHeight: Dp,
    val rowIconSize: Dp,
    val rowIconColumnWidth: Dp,
    val rowTextSize: TextUnit,
    val rowTextLineHeight: TextUnit,
    val dividerStartPadding: Dp,
    val dividerHeight: Dp
)

data class ProfileOptionsBottomSheetItem(
    val title: String,
    val iconRes: DrawableResource,
    val onClick: () -> Unit
)

@Composable
fun ProfileOptionsBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onInsightsClick: () -> Unit = {},
    onGiftCodeClick: () -> Unit = {},
    onHelpCenterClick: () -> Unit = {}
) {
    val items = listOf(
        ProfileOptionsBottomSheetItem(
            title = "Settings",
            iconRes = Res.drawable.ic_settings,
            onClick = onSettingsClick
        ),
        ProfileOptionsBottomSheetItem(
            title = "Edit Profile",
            iconRes = Res.drawable.ic_user,
            onClick = onEditProfileClick
        ),
        ProfileOptionsBottomSheetItem(
            title = "Insights",
            iconRes = Res.drawable.ic_stats,
            onClick = onInsightsClick
        ),
        ProfileOptionsBottomSheetItem(
            title = "Gift Code",
            iconRes = Res.drawable.ic_gift,
            onClick = onGiftCodeClick
        ),
        ProfileOptionsBottomSheetItem(
            title = "Help Center",
            iconRes = Res.drawable.ic_help_centre,
            onClick = onHelpCenterClick
        )
    )

    ProfileOptionsBottomSheet(
        items = items,
        modifier = modifier,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun ProfileOptionsBottomSheet(
    items: List<ProfileOptionsBottomSheetItem>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberProfileOptionsBottomSheetMetrics(
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            itemCount = items.size
        )
        val colorScheme = MaterialTheme.colorScheme

        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(26.dp)
                .background(colorScheme.scrim.copy(alpha = 0.38f))
                .clickable(onClick = onDismissRequest)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(metrics.sheetHeight)
                .clip(
                    RoundedCornerShape(
                        topStart = metrics.sheetCorner,
                        topEnd = metrics.sheetCorner
                    )
                )
                .background(colorScheme.surface)
                .navigationBarsPadding()
                .padding(horizontal = metrics.sheetHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.handleTopSpacing))

            Box(
                modifier = Modifier
                    .width(metrics.handleWidth)
                    .height(metrics.handleHeight)
                    .clip(CircleShape)
                    .background(colorScheme.outline.copy(alpha = 0.36f))
            )

            Spacer(modifier = Modifier.height(metrics.contentTopSpacing))

            items.forEachIndexed { index, item ->
                ProfileOptionsBottomSheetRow(
                    item = item,
                    metrics = metrics,
                    iconTint = colorScheme.primary,
                    textColor = colorScheme.onSurface.copy(alpha = 0.72f)
                )

                if (index != items.lastIndex) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = metrics.dividerStartPadding)
                            .height(metrics.dividerHeight)
                            .background(colorScheme.outline.copy(alpha = 0.26f))
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileOptionsBottomSheetRow(
    item: ProfileOptionsBottomSheetItem,
    metrics: ProfileOptionsBottomSheetMetrics,
    iconTint: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.rowHeight)
            .clickable(onClick = item.onClick),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(metrics.rowIconColumnWidth),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = item.title,
                tint = iconTint,
                modifier = Modifier.size(metrics.rowIconSize)
            )
        }

        Text(
            text = item.title,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.rowTextSize,
                lineHeight = metrics.rowTextLineHeight,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberProfileOptionsBottomSheetMetrics(
    maxWidth: Dp,
    maxHeight: Dp,
    itemCount: Int
): ProfileOptionsBottomSheetMetrics {
    return remember(maxWidth, maxHeight, itemCount) {
        val rowHeight = (maxHeight * 0.078f).coerceIn(62.dp, 78.dp)
        val contentHeight = rowHeight * itemCount + (maxHeight * 0.075f)
        val sheetHeight = contentHeight.coerceIn(maxHeight * 0.44f, maxHeight * 0.52f)
        val rowTextSize = (maxWidth.value * 0.048f).coerceIn(18f, 22f).sp

        ProfileOptionsBottomSheetMetrics(
            sheetHeight = sheetHeight,
            sheetCorner = (maxWidth * 0.105f).coerceIn(38.dp, 56.dp),
            sheetHorizontalPadding = (maxWidth * 0.074f).coerceIn(28.dp, 48.dp),
            handleTopSpacing = (maxHeight * 0.012f).coerceIn(8.dp, 12.dp),
            handleWidth = (maxWidth * 0.102f).coerceIn(34.dp, 48.dp),
            handleHeight = 6.dp,
            contentTopSpacing = (maxHeight * 0.037f).coerceIn(24.dp, 34.dp),
            rowHeight = rowHeight,
            rowIconSize = (maxWidth * 0.052f).coerceIn(22.dp, 32.dp),
            rowIconColumnWidth = (maxWidth * 0.135f).coerceIn(54.dp, 74.dp),
            rowTextSize = rowTextSize,
            rowTextLineHeight = rowTextSize * 1.18f,
            dividerStartPadding = (maxWidth * 0.135f).coerceIn(54.dp, 74.dp),
            dividerHeight = 1.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileOptionsBottomSheetPreview() {
    DZTheme {
        ProfileOptionsBottomSheet()
    }
}
