package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily

/**
 * The larger composed cards — the ones that are a small layout rather than a single shape.
 *
 * Geometry from `dz-all-screens.html`. Each is lifted from the screen that defines it (the hero
 * from Store, the tile from Categories, the row from Friends) so a section reused on Home carries
 * its own design rather than a new one invented for the occasion.
 */

/**
 * The editor's pick hero: a deep terracotta slab with a cover, the title in near-white, and the
 * price as a pill. The darkest surface in the system, and the only one that inverts the type.
 *
 * A 190dp accent-800 circle bleeds off the bottom-right, clipped by the card.
 */
@Composable
fun OrganicHeroCard(
    title: String,
    kicker: String,
    modifier: Modifier = Modifier,
    author: String? = null,
    price: String? = null,
    coverUrl: String? = null,
    onClick: (() -> Unit)? = null,
) {
    OrganicCard(
        modifier = modifier.fillMaxWidth(),
        background = OrganicColors.accent900,
        contentPadding = PaddingValues(22.dp),
        decoration = {
            // `right:-70; bottom:-80; 190×190` — the circle hangs off the bottom-right corner and
            // the card's own clip trims it.
            drawCircle(
                color = OrganicColors.accent800,
                radius = 95.dp.toPx(),
                center = Offset(size.width - 25.dp.toPx(), size.height - 15.dp.toPx())
            )
        },
        onClick = onClick,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OrganicBookCover(
                    title = title,
                    coverUrl = coverUrl,
                    width = 88.dp,
                    height = 128.dp,
                    cornerRadius = 14.dp,
                    elevated = true,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OrganicKicker(text = kicker, color = OrganicColors.accent300)
                    Text(
                        text = title,
                        fontFamily = organicHeadingFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        lineHeight = 24.2.sp,
                        color = OrganicColors.accent100,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (author != null) {
                        Text(
                            text = author,
                            fontFamily = organicBodyFontFamily(),
                            fontSize = 13.sp,
                            color = OrganicColors.accent200,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (price != null) {
                        Text(
                            text = price,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clip(RoundedCornerShape(OrganicShape.pill))
                                .background(OrganicColors.accent)
                                .padding(horizontal = 18.dp, vertical = 9.dp),
                            fontFamily = organicBodyFontFamily(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * A genre tile: 104dp tall, the name sitting on the bottom edge, with a circle and a tilted book
 * spine as decoration in the top-right.
 *
 * [background] alternates down the grid — the design runs accent and sage tints against each other
 * rather than repeating one tint.
 */
@Composable
fun OrganicGenreTile(
    name: String,
    modifier: Modifier = Modifier,
    background: Color = OrganicColors.accent200,
    decorationColor: Color = OrganicColors.accent300,
    spineColor: Color = OrganicColors.accent700,
    textColor: Color = OrganicColors.accent900,
    subtitle: String? = null,
    subtitleColor: Color = OrganicColors.accent800,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .height(104.dp)
            .clip(RoundedCornerShape(OrganicShape.radiusLg))
            .background(background)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                }
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 16.dp, y = (-18).dp)
                .size(74.dp)
                .clip(CircleShape)
                .background(decorationColor)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-16).dp, y = 14.dp)
                .rotate(9f)
                .width(26.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(spineColor)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = name,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 19.sp,
                lineHeight = 20.9.sp,
                color = textColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = subtitleColor,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * A person and what they are doing: 46dp initial circle, name, a line about their reading, and a
 * timestamp trailing.
 */
@Composable
fun OrganicPersonRow(
    name: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingLabel: String? = null,
    avatarBackground: Color = OrganicColors.accent2_600,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(vertical = 11.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicAvatar(
            name = name,
            size = 46.dp,
            background = avatarBackground,
            textColor = Color.White,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = name,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = OrganicColors.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp,
                    color = OrganicColors.neutral700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (trailingLabel != null) {
            Text(
                text = trailingLabel,
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = OrganicColors.accent700,
                maxLines = 1
            )
        }
    }
}

/**
 * The uppercase label that heads a minor section — "READING NOW", "ALL CATEGORIES". Heavier and
 * wider-tracked than [OrganicKicker], which sits inside a card rather than over a list.
 */
@Composable
fun OrganicSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = OrganicColors.neutral600,
) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        fontFamily = organicBodyFontFamily(),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        letterSpacing = 1.2.sp,
        color = color
    )
}

/**
 * The tints the genre grid alternates through, paired so each tile's decoration sits a step or two
 * above its ground.
 */
val OrganicGenreTints: List<GenreTint> = listOf(
    GenreTint(OrganicColors.accent200, OrganicColors.accent300, OrganicColors.accent700, OrganicColors.accent900, OrganicColors.accent800),
    GenreTint(OrganicColors.accent2_200, OrganicColors.accent2_300, OrganicColors.accent2_700, OrganicColors.accent2_900, OrganicColors.accent2_800),
    GenreTint(OrganicColors.neutral200, OrganicColors.neutral300, OrganicColors.neutral700, OrganicColors.neutral900, OrganicColors.neutral800),
)

/** One tile's colour set: ground, circle, spine, title, subtitle. */
data class GenreTint(
    val background: Color,
    val decoration: Color,
    val spine: Color,
    val text: Color,
    val subtitle: Color,
)
