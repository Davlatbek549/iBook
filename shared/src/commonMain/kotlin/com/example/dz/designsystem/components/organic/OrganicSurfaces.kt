package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily

/**
 * The content surfaces almost every Organic screen is assembled from: the filled card, the book
 * cover, the list row, and the avatar.
 *
 * Geometry from `dz-all-screens.html`. The Organic rule that shapes all of these: cards are
 * *filled*, radius 28, with `--shadow-sm` or nothing — never a hairline border.
 */

/**
 * A filled card. Radius 28 by default, no border ever, shadow optional.
 *
 * [onClick] is taken here rather than left to the caller's modifier so the ripple is clipped to the
 * card's own corners.
 */
@Composable
fun OrganicCard(
    modifier: Modifier = Modifier,
    background: Color = OrganicColors.neutral100,
    cornerRadius: Dp = OrganicShape.radiusLg,
    elevated: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(14.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .then(
                if (elevated) {
                    Modifier.organicCardShadow(shape)
                } else {
                    Modifier
                }
            )
            .clip(shape)
            .background(background)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(contentPadding)
    ) {
        content()
    }
}

/** `--shadow-sm` — the only elevation a card ever gets in this system. */
private fun Modifier.organicCardShadow(shape: RoundedCornerShape): Modifier =
    shadow(
        elevation = 2.dp,
        shape = shape,
        ambientColor = OrganicColors.shadow.copy(alpha = 0.14f),
        spotColor = OrganicColors.shadow.copy(alpha = 0.14f)
    )

/**
 * The cover placeholder palette, lifted from the handoff's own gradients. Book artwork is not part
 * of the design — every cover in the frames is one of these — so a book with no image still looks
 * like the design rather than like a failure.
 */
private val OrganicCoverGradients: List<Pair<Color, Color>> = listOf(
    Color(0xFF9AA87E) to Color(0xFF5F6C4B),
    Color(0xFF8D5F45) to Color(0xFF5C3D31),
    Color(0xFFD0B09A) to Color(0xFF9C7358),
    Color(0xFFC9A37C) to Color(0xFF8A6A4F),
    Color(0xFF8F9AA8) to Color(0xFF4F5865),
    Color(0xFFB8837F) to Color(0xFF7D4F52),
    Color(0xFFA58B9C) to Color(0xFF6B4F5F),
    Color(0xFFC08A6A) to Color(0xFF8A5A42),
    Color(0xFFA8845F) to Color(0xFF6F5238),
)

/**
 * Picks a placeholder gradient from a stable key, so one book keeps the same colour across every
 * screen it appears on instead of changing shelf to shelf.
 */
private fun coverGradientFor(key: String): Pair<Color, Color> {
    if (key.isEmpty()) return OrganicCoverGradients.first()
    val hash = key.fold(0) { acc, c -> (acc * 31 + c.code) and 0x7FFFFFFF }
    return OrganicCoverGradients[hash % OrganicCoverGradients.size]
}

/**
 * A book cover at the sizes the design draws it: 112 × 118 r18 in carousels, 52 × 76 r10 in library
 * cards, 44 × 60 r10 in list rows, 66 × 96 r12 on the Keep going card.
 *
 * Artwork loads over the gradient rather than replacing it, so there is never a blank rectangle
 * mid-load and never a bundled stand-in cover to look at. That is the difference from
 * [com.example.dz.designsystem.components.remote.RemoteBookCover], which falls back to a drawable.
 */
@Composable
fun OrganicBookCover(
    title: String,
    modifier: Modifier = Modifier,
    coverUrl: String? = null,
    width: Dp = 112.dp,
    height: Dp = 118.dp,
    cornerRadius: Dp = 18.dp,
    elevated: Boolean = false,
) {
    val (top, bottom) = coverGradientFor(title)
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .then(if (elevated) Modifier.organicCardShadow(shape) else Modifier)
            .clip(shape)
            .drawBehind {
                // CSS `linear-gradient(160deg, …)`: mostly top-to-bottom, leaning ~20° right.
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(top, bottom),
                        start = Offset.Zero,
                        end = Offset(size.width * 0.36f, size.height)
                    )
                )
            }
    ) {
        if (!coverUrl.isNullOrBlank()) {
            AsyncImage(
                model = coverUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

/**
 * The list row the whole app is built out of: cover, then title over author over meta, then
 * whatever trails it — a chevron, a progress ring, a price.
 *
 * The row is one tap target, never several: the handoff puts no separately tappable element inside
 * a row, so [trailing] is drawn inside this clickable rather than beside it.
 */
@Composable
fun OrganicListRow(
    title: String,
    modifier: Modifier = Modifier,
    author: String? = null,
    meta: String? = null,
    metaColor: Color = OrganicColors.accent700,
    coverUrl: String? = null,
    coverWidth: Dp = 44.dp,
    coverHeight: Dp = 60.dp,
    coverRadius: Dp = 10.dp,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
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
            ),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrganicBookCover(
            title = title,
            coverUrl = coverUrl,
            width = coverWidth,
            height = coverHeight,
            cornerRadius = coverRadius
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                lineHeight = 19.sp,
                color = OrganicColors.text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (author != null) {
                Text(
                    text = author,
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = OrganicColors.neutral700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (meta != null) {
                Text(
                    text = meta,
                    modifier = Modifier.padding(top = 2.dp),
                    fontFamily = organicBodyFontFamily(),
                    fontSize = 12.sp,
                    color = metaColor,
                    maxLines = 1
                )
            }
        }
        trailing?.invoke()
    }
}

/**
 * The same row inside a filled card — how Library draws its shelves (neutral-100 fill, 14dp
 * padding, `--shadow-sm`), as opposed to Home's bare rows.
 */
@Composable
fun OrganicListRowCard(
    title: String,
    modifier: Modifier = Modifier,
    author: String? = null,
    meta: String? = null,
    metaColor: Color = OrganicColors.accent700,
    coverUrl: String? = null,
    coverWidth: Dp = 52.dp,
    coverHeight: Dp = 76.dp,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    OrganicCard(
        modifier = modifier.fillMaxWidth(),
        elevated = true,
        onClick = onClick
    ) {
        OrganicListRow(
            title = title,
            author = author,
            meta = meta,
            metaColor = metaColor,
            coverUrl = coverUrl,
            coverWidth = coverWidth,
            coverHeight = coverHeight,
            trailing = trailing
        )
    }
}

/**
 * A carousel card — cover above title above author, 112dp wide. Home's "Picked for you" and the
 * store's shelves are rows of these.
 */
@Composable
fun OrganicCoverCard(
    title: String,
    author: String,
    modifier: Modifier = Modifier,
    coverUrl: String? = null,
    width: Dp = 112.dp,
    coverHeight: Dp = 118.dp,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .width(width)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                }
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OrganicBookCover(
            title = title,
            coverUrl = coverUrl,
            width = width,
            height = coverHeight,
            cornerRadius = 18.dp,
            elevated = true
        )
        Text(
            text = title,
            fontFamily = organicBodyFontFamily(),
            fontSize = 13.sp,
            lineHeight = 15.6.sp,
            color = OrganicColors.text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = author,
            modifier = Modifier.offset(y = (-6).dp),
            fontFamily = organicBodyFontFamily(),
            fontSize = 11.sp,
            color = OrganicColors.neutral700,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * An initial circle. No photography anywhere in the design — avatars are the first letter of a name
 * in the display face on an accent-200 ground.
 */
@Composable
fun OrganicAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    background: Color = OrganicColors.accent200,
    textColor: Color = OrganicColors.accent800,
    fontSize: TextUnit = 18.sp,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(background)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClick = onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.trim().take(1).uppercase(),
            fontFamily = organicHeadingFontFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            color = textColor
        )
    }
}

/**
 * Overlapping avatars — the presence stack on Home's "reading right now" card. Each circle after
 * the first steps 10dp left and wears a 2dp ring in the page colour so the overlap reads as depth.
 */
@Composable
fun OrganicAvatarStack(
    names: List<String>,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    ringColor: Color = OrganicColors.bg,
    backgrounds: List<Color> = listOf(
        OrganicColors.accent2_400,
        OrganicColors.accent300,
        OrganicColors.accent2_600
    ),
) {
    Row(modifier = modifier) {
        names.forEachIndexed { index, name ->
            OrganicAvatar(
                name = name,
                modifier = Modifier
                    .offset(x = if (index == 0) 0.dp else (-10 * index).dp)
                    .border(2.dp, ringColor, CircleShape),
                size = size,
                background = backgrounds[index % backgrounds.size],
                textColor = OrganicColors.neutral900,
                fontSize = 13.sp
            )
        }
    }
}
