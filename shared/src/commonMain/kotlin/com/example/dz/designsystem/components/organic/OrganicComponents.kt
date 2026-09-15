package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicDisplayFontFamily

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
    height: Dp = 58.dp,
    leadingIcon: ImageVector? = null,
) {
    Row(
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
            .height(height)
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
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
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

// ---------------------------------------------------------------------------
// Shared shell — screen frame, tab bar, icon buttons, cards, rows, tokens
// used across the whole "Reading & library" area (Home, Library, Collections,
// Book detail, Reading). See design handoff `README.md` § Global layout spec.
// ---------------------------------------------------------------------------

/** One of the five persistent destinations on [OrganicBottomBar]. */
enum class OrganicTab { Home, Library, Store, Search, Profile }

/**
 * The floating pill tab bar — dark (neutral-900) capsule anchored 24dp from
 * each side and 22dp off the bottom, 64dp tall. The active item gets a 44dp
 * accent-filled circle with a white icon; inactive items are bare
 * neutral-300 icons. This replaces the legacy full-width [com.example.dz.presentation.navigation.CustomBottomBar].
 */
@Composable
fun OrganicBottomBar(
    selected: OrganicTab,
    onTabClick: (OrganicTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(OrganicShape.pill),
                ambientColor = OrganicColors.shadow.copy(alpha = 0.35f),
                spotColor = OrganicColors.shadow.copy(alpha = 0.35f)
            )
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral900),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val items = listOf(
            Triple(OrganicTab.Home, OrganicIcons.Home, "Home"),
            Triple(OrganicTab.Library, OrganicIcons.Library, "Library"),
            Triple(OrganicTab.Store, OrganicIcons.Store, "Store"),
            Triple(OrganicTab.Search, OrganicIcons.Search, "Search"),
            Triple(OrganicTab.Profile, OrganicIcons.Profile, "Profile"),
        )
        items.forEach { (tab, icon, label) ->
            val isActive = tab == selected
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .then(
                        if (isActive) Modifier.background(OrganicColors.accent) else Modifier
                    )
                    .clickable(role = Role.Button, onClick = { onTabClick(tab) }),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isActive) Color.White else OrganicColors.neutral300,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/** Circular icon button — the workhorse for back buttons, search glyphs, and trailing actions. */
@Composable
fun OrganicIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 42.dp,
    background: Color = OrganicColors.neutral200,
    tint: Color = OrganicColors.neutral800,
    iconSize: Dp = 18.dp,
    contentDescription: String? = null,
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

/** Back-chevron circle, the standard top-left affordance on pushed screens. */
@Composable
fun OrganicBackButton(onClick: () -> Unit, modifier: Modifier = Modifier, size: Dp = 42.dp) {
    OrganicIconButton(
        icon = OrganicIcons.ChevronLeft,
        onClick = onClick,
        modifier = modifier,
        size = size,
        background = OrganicColors.neutral200,
        tint = OrganicColors.neutral800,
    )
}

/** "Title" + "See all" header row used above carousels and lists (Home, Library, etc). */
@Composable
fun OrganicSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            fontFamily = organicDisplayFontFamily(),
            fontSize = 21.sp,
            color = OrganicColors.text
        )
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                fontFamily = organicBodyFontFamily(),
                fontSize = 12.sp,
                color = OrganicColors.accent700,
                modifier = Modifier
                    .then(if (onActionClick != null) Modifier.clickable(onClick = onActionClick) else Modifier)
            )
        }
    }
}

/** A book-cover placeholder: a diagonal gradient rounded rect, standing in for real artwork. */
@Composable
fun OrganicBookCover(
    modifier: Modifier = Modifier,
    gradient: Pair<Color, Color> = Color(0xFF8D5F45) to Color(0xFF5C3D31),
    radius: Dp = 12.dp,
    elevation: Dp = 0.dp,
) {
    Box(
        modifier = modifier
            .then(
                if (elevation > 0.dp) Modifier.shadow(elevation, RoundedCornerShape(radius), clip = false)
                else Modifier
            )
            .clip(RoundedCornerShape(radius))
            .background(
                Brush.linearGradient(
                    colors = listOf(gradient.first, gradient.second),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1400f),
                )
            )
    )
}

/** Named placeholder gradients pulled straight from the handoff's cover swatches. */
object OrganicCoverGradients {
    val list = listOf(
        Color(0xFF8D5F45) to Color(0xFF5C3D31),
        Color(0xFF9AA87E) to Color(0xFF5F6C4B),
        Color(0xFFD0B09A) to Color(0xFF9C7358),
        Color(0xFFC9A37C) to Color(0xFF8A6A4F),
        Color(0xFFA8845F) to Color(0xFF6F5238),
        Color(0xFF8F9AA8) to Color(0xFF4F5865),
        Color(0xFFB8837F) to Color(0xFF7D4F52),
        Color(0xFFA58B9C) to Color(0xFF6B4F5F),
        Color(0xFFC08A6A) to Color(0xFF8A5A42),
        OrganicColors.accent2_700 to OrganicColors.accent2_900,
    )

    fun forIndex(index: Int): Pair<Color, Color> = list[index.mod(list.size)]
}

/**
 * A filled disc that reveals a ring by drawing an inner circle on top —
 * exactly mirrors the handoff's `conic-gradient(...)` progress rings (Home's
 * Keep going card, Library's per-book rings).
 */
@Composable
fun OrganicProgressRing(
    percent: Int,
    modifier: Modifier = Modifier,
    size: Dp = 58.dp,
    innerSize: Dp = 44.dp,
    ringColor: Color = OrganicColors.accent,
    trackColor: Color = Color.White.copy(alpha = 0.55f),
    innerBackground: Color = OrganicColors.accent200,
    content: (@Composable () -> Unit)? = null,
) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(size)) {
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = true,
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * (percent.coerceIn(0, 100) / 100f),
                useCenter = true,
            )
        }
        Box(
            modifier = Modifier
                .size(innerSize)
                .clip(CircleShape)
                .background(innerBackground),
            contentAlignment = Alignment.Center
        ) {
            content?.invoke()
        }
    }
}

/** Small rounded label — badges, tags, chapter kickers, filter-tab pills. */
@Composable
fun OrganicPill(
    text: String,
    modifier: Modifier = Modifier,
    background: Color = OrganicColors.neutral200,
    textColor: Color = OrganicColors.neutral800,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    horizontalPadding: Dp = 14.dp,
    verticalPadding: Dp = 7.dp,
    uppercase: Boolean = false,
    letterSpacing: androidx.compose.ui.unit.TextUnit = 0.sp,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(background)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        Text(
            text = if (uppercase) text.uppercase() else text,
            fontFamily = organicBodyFontFamily(),
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor,
            letterSpacing = letterSpacing,
        )
    }
}

/** Segmented pill filter (e.g. Reading / To read / Finished). */
@Composable
fun OrganicFilterTabs(
    items: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            OrganicPill(
                text = label,
                background = if (isSelected) OrganicColors.accent else OrganicColors.neutral200,
                textColor = if (isSelected) Color.White else OrganicColors.neutral800,
                fontSize = 13.sp,
                horizontalPadding = 18.dp,
                verticalPadding = 9.dp,
                onClick = { onSelect(index) },
            )
        }
    }
}

/** 52×30 pill toggle — off = neutral-300, on = accent, 24dp white knob. */
@Composable
fun OrganicToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(52.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(if (checked) OrganicColors.accent else OrganicColors.neutral300)
            .clickable(role = Role.Switch, onClick = { onCheckedChange(!checked) })
            .padding(3.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .shadow(2.dp, CircleShape, clip = false)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

/** Circular initial avatar — "A" in a tinted circle, heading font. */
@Composable
fun OrganicAvatarInitial(
    initial: String,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    background: Color = OrganicColors.accent200,
    textColor: Color = OrganicColors.accent800,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            fontFamily = organicDisplayFontFamily(),
            fontSize = (size.value * 0.39f).sp,
            color = textColor,
        )
    }
}

/** Overlapping avatar stack with a background-colour ring, e.g. "Reading now" presence card. */
@Composable
fun OrganicAvatarStack(
    colors: List<Color>,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    overlap: Dp = 10.dp,
    ringColor: Color = OrganicColors.bg,
) {
    Row(modifier = modifier) {
        colors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .offset(x = if (index == 0) 0.dp else -overlap * index)
                    .size(size)
                    .border(2.dp, ringColor, CircleShape)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

/** Pill/rounded text field, filled neutral-100, 2px accent border while focused. */
@Composable
fun OrganicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    height: Dp = 54.dp,
) {
    var focused by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral100)
            .then(
                if (focused) Modifier.border(2.dp, OrganicColors.accent, RoundedCornerShape(OrganicShape.pill))
                else Modifier
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontFamily = organicBodyFontFamily(),
                fontSize = 16.sp,
                color = OrganicColors.neutral500,
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focused = it.isFocused },
            textStyle = TextStyle(
                fontFamily = organicBodyFontFamily(),
                fontSize = 16.sp,
                color = OrganicColors.text,
            ),
            singleLine = true,
            cursorBrush = Brush.verticalGradient(listOf(OrganicColors.accent, OrganicColors.accent)),
        )
    }
}

/** Multiline field for descriptions/notes — filled neutral-100, radius-lg, no border. */
@Composable
fun OrganicMultilineField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    minHeight: Dp = 78.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(OrganicColors.neutral100, RoundedCornerShape(OrganicShape.radiusLg))
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = OrganicColors.neutral500,
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight),
            textStyle = TextStyle(
                fontFamily = organicBodyFontFamily(),
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = OrganicColors.neutral800,
            ),
            cursorBrush = Brush.verticalGradient(listOf(OrganicColors.accent, OrganicColors.accent)),
        )
    }
}

/** A generic 28dp-radius filled surface — the base for every card in the Organic system. */
@Composable
fun OrganicCard(
    modifier: Modifier = Modifier,
    background: Color = OrganicColors.neutral100,
    radius: Dp = OrganicShape.radiusLg,
    elevated: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .then(
                if (elevated) Modifier.shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(radius),
                    ambientColor = OrganicColors.shadow.copy(alpha = 0.14f),
                    spotColor = OrganicColors.shadow.copy(alpha = 0.14f),
                ) else Modifier
            )
            .clip(RoundedCornerShape(radius))
            .background(background)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        content()
    }
}
