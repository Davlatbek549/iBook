package com.example.dz.designsystem.components.organic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.OrganicIcons
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily

/*
 * Controls Edit Profile introduced, each built to the "Edit profile · new components" spec in the
 * design project. Generic enough to reuse: none of them knows it is on a profile.
 */

/**
 * Two or more choices in one pill track — Work · Study. A 48dp neutral-200 track with a 4dp inset;
 * the chosen segment is a raised neutral-100 thumb.
 */
@Composable
fun OrganicSegmentedControl(
    options: List<OrganicSegment>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val body = organicBodyFontFamily()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral200)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            val shape = RoundedCornerShape(OrganicShape.pill)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .then(if (selected) Modifier.shadow(1.dp, shape, ambientColor = OrganicColors.shadow, spotColor = OrganicColors.shadow) else Modifier)
                    .clip(shape)
                    .background(if (selected) OrganicColors.neutral100 else Color.Transparent)
                    .selectable(selected = selected, enabled = enabled, role = Role.Tab) { onSelect(index) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                option.icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (selected) OrganicColors.accent700 else OrganicColors.neutral700,
                    )
                    Box(modifier = Modifier.size(7.dp))
                }
                Text(
                    text = option.label,
                    fontFamily = body,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (selected) OrganicColors.neutral900 else OrganicColors.neutral700,
                )
            }
        }
    }
}

data class OrganicSegment(val label: String, val icon: ImageVector? = null)

/**
 * One choice of a set, drawn as a card: Open or Private. Never colour alone — the chosen card
 * also carries a filled check, the other an empty ring.
 */
@Composable
fun OrganicOptionCard(
    title: String,
    body: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(OrganicShape.radiusLg)
    val font = organicBodyFontFamily()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f)
            .clip(shape)
            .background(if (selected) OrganicColors.accent100 else OrganicColors.neutral100)
            .then(if (selected) Modifier.border(2.dp, OrganicColors.accent, shape) else Modifier)
            .selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick)
            .padding(start = 14.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (selected) OrganicColors.accent200 else OrganicColors.neutral200),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(19.dp),
                tint = if (selected) OrganicColors.accent800 else OrganicColors.neutral800,
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, fontFamily = font, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrganicColors.text)
            Text(text = body, fontFamily = font, fontSize = 13.sp, lineHeight = 18.sp, color = OrganicColors.neutral700)
        }
        OrganicSelectionMark(selected = selected)
    }
}

/**
 * A pill row in a picker sheet — Female, Male, Prefer not to say. The same selected treatment as
 * [OrganicOptionCard], without the icon and the second line.
 */
@Composable
fun OrganicChoiceRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(OrganicShape.pill)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(shape)
            .background(if (selected) OrganicColors.accent100 else OrganicColors.neutral100)
            .then(if (selected) Modifier.border(2.dp, OrganicColors.accent, shape) else Modifier)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
            .padding(start = 20.dp, end = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            fontFamily = organicBodyFontFamily(),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 15.sp,
            color = OrganicColors.text,
        )
        OrganicSelectionMark(selected = selected)
    }
}

/** The 26dp ring or filled check at the end of an option. */
@Composable
private fun OrganicSelectionMark(selected: Boolean) {
    if (selected) {
        Box(
            modifier = Modifier.size(26.dp).clip(CircleShape).background(OrganicColors.accent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(OrganicIcons.Check, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color.White)
        }
    } else {
        Box(modifier = Modifier.size(26.dp).border(2.dp, OrganicColors.neutral400, CircleShape))
    }
}

/** How a tappable value row reads: an empty choice, a choice not yet saved, or a locked one. */
enum class OrganicPickerRowState { Editable, Locked }

/**
 * A field-shaped row that opens a picker instead of a keyboard: date of birth, gender, country.
 * Locked rows are quieter (neutral-200), show a padlock instead of the chevron, and cannot be
 * tapped — the one-time fields once they are set.
 */
@Composable
fun OrganicPickerRow(
    value: String?,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    state: OrganicPickerRowState = OrganicPickerRowState.Editable,
    enabled: Boolean = true,
) {
    val locked = state == OrganicPickerRowState.Locked
    val font = organicBodyFontFamily()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .alpha(if (enabled) 1f else 0.5f)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(if (locked) OrganicColors.neutral200 else OrganicColors.neutral100)
            .clickable(enabled = enabled && !locked, role = Role.Button, onClick = onClick)
            .semantics { if (locked) stateDescription = "Locked" }
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(18.dp), tint = OrganicColors.neutral700)
        }
        Text(
            text = value ?: placeholder,
            modifier = Modifier.weight(1f),
            fontFamily = font,
            fontSize = 15.sp,
            maxLines = 1,
            color = when {
                value == null -> OrganicColors.neutral700
                locked -> OrganicColors.neutral800
                else -> OrganicColors.text
            },
        )
        Icon(
            imageVector = if (locked) OrganicIcons.Lock else OrganicIcons.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(if (locked) 16.dp else 15.dp),
            tint = OrganicColors.neutral600,
        )
    }
}

/**
 * The dial-code chip inside a phone field: flag, code and a chevron on a 40dp neutral-200 pill.
 * Opens the country picker.
 */
@Composable
fun OrganicCountryCodeChip(
    flag: String,
    dialCode: String,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.neutral200)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription }
            .padding(start = 12.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = flag, fontSize = 16.sp)
        Text(
            text = dialCode,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            color = OrganicColors.text,
        )
        Icon(OrganicIcons.ChevronDown, contentDescription = null, modifier = Modifier.size(14.dp), tint = OrganicColors.neutral700)
    }
}

/** A sage pill that fills a field in one tap — a free username the server suggested. */
@Composable
fun OrganicSuggestionPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(OrganicColors.accent2_200)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = OrganicColors.accent2_800,
        )
    }
}

/** Which ramp a section tag is cut from: terracotta for public, sage for private. */
enum class OrganicTagTone { Accent, Sage }

/**
 * A small uppercase tag with an icon, heading a group of fields — "On your profile",
 * "Private · only you see these".
 */
@Composable
fun OrganicSectionTag(
    text: String,
    icon: ImageVector,
    tone: OrganicTagTone,
    modifier: Modifier = Modifier,
) {
    val (fill, ink) = when (tone) {
        OrganicTagTone.Accent -> OrganicColors.accent100 to OrganicColors.accent800
        OrganicTagTone.Sage -> OrganicColors.accent2_100 to OrganicColors.accent2_800
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(fill)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(13.dp), tint = ink)
        Text(
            text = text.uppercase(),
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 0.9.sp,
            color = ink,
        )
    }
}

/** How a helper line under a field reads. */
enum class OrganicHelperTone { Neutral, Warning, Success }

/**
 * The one line under a field: its rule, a one-time warning, or a status. Warning text uses
 * accent-700, never the accent itself, so it passes contrast at this size.
 */
@Composable
fun OrganicHelperText(
    text: String,
    modifier: Modifier = Modifier,
    tone: OrganicHelperTone = OrganicHelperTone.Neutral,
    icon: ImageVector? = null,
) {
    val color = when (tone) {
        OrganicHelperTone.Neutral -> OrganicColors.neutral700
        OrganicHelperTone.Warning -> OrganicColors.accent700
        OrganicHelperTone.Success -> OrganicColors.accent2_700
    }
    Row(
        modifier = modifier.padding(start = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let { Icon(it, contentDescription = null, modifier = Modifier.size(13.dp), tint = color) }
        Text(
            text = text,
            fontFamily = organicBodyFontFamily(),
            fontWeight = if (tone == OrganicHelperTone.Neutral) FontWeight.Normal else FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = color,
        )
    }
}

/** The spinner a field shows at its end while something about it is being checked. */
@Composable
fun OrganicFieldSpinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(17.dp),
        color = OrganicColors.neutral700,
        strokeWidth = 2.dp,
    )
}
