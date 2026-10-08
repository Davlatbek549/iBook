package com.example.dz.presentation.reading

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicSectionLabel
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicLight
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.util.lerp
import com.example.dz.domain.model.PageTurn
import com.example.dz.domain.model.PageTheme
import com.example.dz.domain.model.ReaderPreferences
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.reader_display
import dz.shared.generated.resources.reader_font_sans
import dz.shared.generated.resources.reader_font_serif
import dz.shared.generated.resources.reader_page_colour
import dz.shared.generated.resources.reader_page_turn
import dz.shared.generated.resources.reader_turn_curl
import dz.shared.generated.resources.reader_turn_scroll
import dz.shared.generated.resources.reader_turn_slide
import dz.shared.generated.resources.reader_text_size
import dz.shared.generated.resources.reader_theme_cream
import dz.shared.generated.resources.reader_theme_night
import dz.shared.generated.resources.reader_theme_paper
import dz.shared.generated.resources.reader_theme_sage
import org.jetbrains.compose.resources.stringResource

/**
 * The display sheet — text size, page colour, face.
 *
 * A sheet over the page rather than a screen of its own, as the handoff draws it: the point of
 * changing type size is watching the page change behind the control, which a pushed screen cannot
 * show. Tapping outside dismisses it and keeps your place.
 *
 * Every change is applied and written the moment it is made; there is no Save, because there is
 * nothing here a reader would want to cancel.
 */
@Composable
internal fun DisplaySheet(
    uiState: ReadingUiState,
    onEvent: (ReadingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrimInteraction = remember { MutableInteractionSource() }

    Box(modifier = Modifier.fillMaxSize()) {
        // 0.38 is the handoff's own value: dark enough that the sheet plainly has focus, light
        // enough that the page is still readable behind it — which is the reason the sheet is
        // over the page rather than on a screen of its own.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OrganicColors.scrim.copy(alpha = 0.38f))
                .clickable(
                    interactionSource = scrimInteraction,
                    indication = null,
                ) { onEvent(ReadingEvent.DisplaySheetDismissed) }
        )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    ambientColor = OrganicColors.shadow.copy(alpha = 0.22f),
                    spotColor = OrganicColors.shadow.copy(alpha = 0.22f)
                )
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(OrganicColors.bg)
                .navigationBarsPadding()
                .padding(start = 28.dp, end = 28.dp, top = 20.dp, bottom = 34.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(46.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(OrganicShape.pill))
                    .background(OrganicColors.neutral400)
            )

            Text(
                text = stringResource(Res.string.reader_display),
                fontFamily = organicHeadingFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                color = OrganicColors.text
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OrganicSectionLabel(text = stringResource(Res.string.reader_text_size))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // The two A's are the scale itself: small at one end, large at the other.
                    Text(
                        text = "A",
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 14.sp,
                        color = OrganicColors.neutral700
                    )
                    // Thumb and track are drawn here rather than taken from Material: the
                    // default draws a bar-shaped handle and a stop dot at the end of the track,
                    // and the design asks for a plain round knob on a plain pill.
                    Slider(
                        value = uiState.preferences.fontScale,
                        onValueChange = { onEvent(ReadingEvent.FontScaleChanged(it)) },
                        onValueChangeFinished = { onEvent(ReadingEvent.FontScaleCommitted) },
                        valueRange = ReaderPreferences.MIN_FONT_SCALE..ReaderPreferences.MAX_FONT_SCALE,
                        modifier = Modifier.weight(1f),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = CircleShape,
                                        ambientColor = OrganicColors.shadow.copy(alpha = 0.22f),
                                        spotColor = OrganicColors.shadow.copy(alpha = 0.22f),
                                    )
                                    .background(OrganicColors.accent, CircleShape)
                            )
                        },
                        track = { state ->
                            val span = ReaderPreferences.MAX_FONT_SCALE - ReaderPreferences.MIN_FONT_SCALE
                            val filled = ((state.value - ReaderPreferences.MIN_FONT_SCALE) / span)
                                .coerceIn(0f, 1f)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(OrganicShape.pill))
                                    .background(OrganicColors.neutral200)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(filled)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(OrganicShape.pill))
                                        .background(OrganicColors.accent)
                                )
                            }
                        },
                    )
                    Text(
                        text = "A",
                        fontFamily = organicBodyFontFamily(),
                        fontSize = 22.sp,
                        color = OrganicColors.neutral800
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OrganicSectionLabel(text = stringResource(Res.string.reader_page_colour))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    pageThemeSwatches().forEach { (theme, swatch) ->
                        PageThemeSwatch(
                            colour = swatch,
                            label = theme.label(),
                            selected = theme == uiState.preferences.pageTheme,
                            onClick = { onEvent(ReadingEvent.PageThemeChanged(theme)) },
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OrganicSectionLabel(text = stringResource(Res.string.reader_page_turn))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PageTurn.entries.forEach { turn ->
                        PageTurnOption(
                            turn = turn,
                            selected = turn == uiState.preferences.pageTurn,
                            modifier = Modifier.weight(1f),
                            onClick = { onEvent(ReadingEvent.PageTurnChanged(turn)) },
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FaceOption(
                    label = stringResource(Res.string.reader_font_sans),
                    selected = !uiState.preferences.useSerif,
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(ReadingEvent.SerifChanged(false)) },
                )
                FaceOption(
                    label = stringResource(Res.string.reader_font_serif),
                    selected = uiState.preferences.useSerif,
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(ReadingEvent.SerifChanged(true)) },
                )
            }
        }
    }
}

/**
 * One way of turning a page, played rather than described.
 *
 * The words alone — slide, paper, scroll — do not say much until you have tried each, and neither
 * does a still picture of a sheet stopped half way across. So each glyph turns its own page on a
 * loop: one sheet arriving from the side, one peeling up by its corner, one rising from below.
 * Whichever motion you recognise is the one you want.
 */
@Composable
private fun PageTurnOption(
    turn: PageTurn,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val ink = if (selected) Color.White else OrganicColors.neutral800

    // Rest, turn, rest. The pause at each end is what makes the motion legible as a page turn
    // rather than a shape that never stops moving.
    val motion by rememberInfiniteTransition(label = "page turn").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = TURN_LOOP_MILLIS
                0f at 0 using FastOutSlowInEasing
                0f at TURN_REST_MILLIS using FastOutSlowInEasing
                1f at TURN_REST_MILLIS + TURN_TRAVEL_MILLIS
                1f at TURN_LOOP_MILLIS
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "page turn",
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(OrganicShape.radiusMd))
            .background(if (selected) OrganicColors.accent else OrganicColors.neutral200)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Canvas(modifier = Modifier.size(width = 30.dp, height = 22.dp)) {
            val stroke = 1.6.dp.toPx()
            val corner = CornerRadius(2.dp.toPx(), 2.dp.toPx())

            // Sheets come in from off the glyph, so the part still outside it has to be cut.
            clipRect {
                when (turn) {
                    // A sheet arriving from the right over the one already there.
                    PageTurn.SLIDE -> {
                        val sheet = Size(size.width * 0.62f, size.height)
                        drawRoundRect(
                            color = ink.copy(alpha = 0.35f),
                            topLeft = Offset.Zero,
                            size = sheet,
                            cornerRadius = corner,
                            style = Stroke(width = stroke),
                        )
                        drawRoundRect(
                            color = ink,
                            topLeft = Offset(
                                lerp(size.width, size.width - sheet.width, motion),
                                0f,
                            ),
                            size = sheet,
                            cornerRadius = corner,
                            style = Stroke(width = stroke),
                        )
                    }

                    // A sheet lifting off the one beneath, corner first.
                    PageTurn.CURL -> {
                        drawRoundRect(
                            color = ink.copy(alpha = 0.35f),
                            topLeft = Offset.Zero,
                            size = size,
                            cornerRadius = corner,
                            style = Stroke(width = stroke),
                        )
                        val fold = size.width * 0.52f * motion
                        if (fold > stroke) {
                            drawPath(
                                path = Path().apply {
                                    moveTo(size.width, size.height - fold)
                                    lineTo(size.width - fold, size.height)
                                    quadraticBezierTo(
                                        size.width - fold * 0.25f, size.height - fold * 0.25f,
                                        size.width, size.height - fold,
                                    )
                                    close()
                                },
                                color = ink,
                            )
                        }
                    }

                    // A sheet rising from below the one being read.
                    PageTurn.SCROLL -> {
                        drawRoundRect(
                            color = ink.copy(alpha = 0.35f),
                            topLeft = Offset.Zero,
                            size = size,
                            cornerRadius = corner,
                            style = Stroke(width = stroke),
                        )
                        drawRoundRect(
                            color = ink,
                            topLeft = Offset(
                                0f,
                                lerp(size.height, size.height * 0.16f, motion),
                            ),
                            size = Size(size.width, size.height * 0.46f),
                            cornerRadius = corner,
                            style = Stroke(width = stroke),
                        )
                    }
                }
            }
        }
        Text(
            text = stringResource(
                when (turn) {
                    PageTurn.SLIDE -> Res.string.reader_turn_slide
                    PageTurn.CURL -> Res.string.reader_turn_curl
                    PageTurn.SCROLL -> Res.string.reader_turn_scroll
                }
            ),
            fontFamily = organicBodyFontFamily(),
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 13.sp,
            color = ink
        )
    }
}

/** How long a glyph sits still before it turns, and again after it has. */
private const val TURN_REST_MILLIS = 520

/** How long the turn itself takes — near enough the real one to stand for it. */
private const val TURN_TRAVEL_MILLIS = 900

private const val TURN_LOOP_MILLIS = TURN_REST_MILLIS * 2 + TURN_TRAVEL_MILLIS

@Composable
private fun PageThemeSwatch(
    colour: Color,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(colour)
            .border(
                // The chosen ground is ringed in the accent; the rest get a hairline so a pale
                // swatch still has an edge against the sheet it sits on.
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) OrganicColors.accent else OrganicColors.neutral300,
                shape = CircleShape,
            )
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semanticsLabel(label)
    )
}

@Composable
private fun FaceOption(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        text = label,
        modifier = modifier
            .clip(RoundedCornerShape(OrganicShape.pill))
            .background(if (selected) OrganicColors.accent else OrganicColors.neutral200)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .padding(vertical = 14.dp),
        fontFamily = organicBodyFontFamily(),
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        fontSize = 14.sp,
        color = if (selected) Color.White else OrganicColors.neutral800,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

/**
 * The swatch shown for each ground — the page colour itself, not a sample of its text. Taken from
 * the light palette in either appearance, like the page it stands for.
 */
private fun pageThemeSwatches(): List<Pair<PageTheme, Color>> = listOf(
    PageTheme.CREAM to OrganicLight.bg,
    PageTheme.PAPER to OrganicLight.neutral100,
    PageTheme.SAGE to OrganicLight.accent2_200,
    PageTheme.NIGHT to OrganicLight.neutral900,
)

@Composable
private fun PageTheme.label(): String = when (this) {
    PageTheme.CREAM -> stringResource(Res.string.reader_theme_cream)
    PageTheme.PAPER -> stringResource(Res.string.reader_theme_paper)
    PageTheme.SAGE -> stringResource(Res.string.reader_theme_sage)
    PageTheme.NIGHT -> stringResource(Res.string.reader_theme_night)
}

/** A swatch is a colour with no text in it, so its name has to be spoken rather than read. */
private fun Modifier.semanticsLabel(label: String): Modifier =
    semantics { contentDescription = label }
