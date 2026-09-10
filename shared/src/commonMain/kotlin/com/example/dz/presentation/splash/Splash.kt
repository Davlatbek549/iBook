package com.example.dz.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.rememberEntranceProgress
import com.example.dz.designsystem.components.organic.rememberSettleProgress
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.splash_subtitle
import dz.shared.generated.resources.splash_wordmark
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

/**
 * First screen of the app: a brand moment, and nothing else. [SplashViewModel] decides where to go
 * while it plays and moves on by itself — there is nothing here to tap.
 *
 * The entrance is staggered so the shelf appears to fill itself: the mark, wordmark and line
 * settle first, then the spines rise into place one after another. Timings are tuned to finish
 * inside `SPLASH_MINIMUM_MILLIS`, so the screen is never cut off mid-animation.
 */
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrganicColors.bg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            BrandBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, start = 34.dp, end = 34.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            BookshelfIllustration(modifier = Modifier.align(Alignment.CenterHorizontally))

            // Holds the shelf in the lower third now that nothing sits under it.
            Spacer(modifier = Modifier.weight(0.6f))
        }
    }
}

@Composable
private fun BrandBlock(modifier: Modifier = Modifier) {
    val mark by rememberEntranceProgress(delayMillis = 0, durationMillis = 380)
    val wordmark by rememberEntranceProgress(delayMillis = 100)
    val subtitle by rememberEntranceProgress(delayMillis = 220)

    Column(modifier = modifier) {
        // The "d" mark — a filled accent circle with the wordmark's first letter.
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = mark
                    scaleX = 0.82f + 0.18f * mark
                    scaleY = 0.82f + 0.18f * mark
                }
                .size(54.dp)
                .clip(CircleShape)
                .background(OrganicColors.accent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "d",
                fontFamily = organicHeadingFontFamily(),
                fontSize = 26.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(Res.string.splash_wordmark),
            modifier = Modifier.graphicsLayer {
                alpha = wordmark
                translationY = (1f - wordmark) * 18.dp.toPx()
            },
            fontFamily = organicHeadingFontFamily(),
            fontSize = 76.sp,
            lineHeight = 68.sp,
            color = OrganicColors.accent900
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(Res.string.splash_subtitle),
            modifier = Modifier
                .widthIn(max = 250.dp)
                .graphicsLayer {
                    alpha = subtitle
                    translationY = (1f - subtitle) * 14.dp.toPx()
                },
            fontFamily = organicBodyFontFamily(),
            fontSize = 17.sp,
            lineHeight = 26.sp,
            color = OrganicColors.neutral800
        )
    }
}

/**
 * How far a tilted spine reaches past the row's own edges, and so how much room the clip frame
 * has to leave it. Comfortably over the worst case: the tallest tilted spine is 158dp at 5°,
 * which swings its corner about 7dp wide and 2dp low.
 */
private val SPINE_TILT_MARGIN = 16.dp
private val SPINE_TILT_DROP = 6.dp

/** How far apart the spines start, so they land one after another rather than together. */
private const val SPINE_STAGGER_MILLIS = 80
private const val FIRST_SPINE_DELAY_MILLIS = 320

/**
 * Decorative bookshelf: five spines of varying height/tilt resting on a shelf line. The row is
 * clipped, so a spine still below its resting place is hidden behind the shelf and appears to be
 * slotted in. Not interactive — no click handling anywhere in this subtree.
 *
 * The clip sits on a frame around the row rather than on the row itself. A tilt rotates a spine
 * inside its own layout bounds, which pushes its outer corners past them — clipping at the row's
 * edge sheared those corners flat and left the end books looking trimmed down their sides. The
 * margin below is only what a tilt reaches past the baseline; a rising spine is a whole height
 * down, so it is still hidden.
 */
@Composable
private fun BookshelfIllustration(modifier: Modifier = Modifier) {
    data class Spine(
        val width: Dp,
        val height: Dp,
        val rotation: Float,
        val brush: Brush,
    )

    val spines = listOf(
        Spine(44.dp, 130.dp, -4f, Brush.linearGradient(listOf(Color(0xFF9AA87E), Color(0xFF5F6C4B)))),
        Spine(52.dp, 172.dp, 0f, Brush.linearGradient(listOf(Color(0xFF8D5F45), Color(0xFF5C3D31)))),
        Spine(40.dp, 146.dp, 3f, Brush.linearGradient(listOf(Color(0xFFD0B09A), Color(0xFF9C7358)))),
        Spine(48.dp, 118.dp, -2f, Brush.linearGradient(listOf(Color(0xFFC9A37C), Color(0xFF8A6A4F)))),
        Spine(36.dp, 158.dp, 5f, Brush.linearGradient(listOf(OrganicColors.accent2_700, OrganicColors.accent2_700))),
    )

    val shelf by rememberEntranceProgress(delayMillis = 160, durationMillis = 460)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.clipToBounds()) {
            Row(
                modifier = Modifier.padding(
                    start = SPINE_TILT_MARGIN,
                    end = SPINE_TILT_MARGIN,
                    bottom = SPINE_TILT_DROP,
                ),
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Bottom
            ) {
                spines.forEachIndexed { index, spine ->
                    val rise by rememberSettleProgress(
                        delayMillis = FIRST_SPINE_DELAY_MILLIS + index * SPINE_STAGGER_MILLIS
                    )

                    Box(
                        modifier = Modifier
                            .width(spine.width)
                            .height(spine.height)
                            .graphicsLayer {
                                alpha = rise.coerceIn(0f, 1f)
                                translationY = (1f - rise) * spine.height.toPx()
                                rotationZ = spine.rotation * rise.coerceIn(0f, 1f)
                            }
                            .clip(
                                RoundedCornerShape(
                                    topStart = 8.dp,
                                    topEnd = 8.dp,
                                    bottomStart = 4.dp,
                                    bottomEnd = 4.dp,
                                )
                            )
                            .background(spine.brush)
                    )
                }
            }
        }
        // The row already carries SPINE_TILT_DROP under the books, so this is the rest of the gap.
        Spacer(modifier = Modifier.height(10.dp - SPINE_TILT_DROP))
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = shelf
                    scaleX = 0.35f + 0.65f * shelf
                }
                .width(280.dp)
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(OrganicColors.accent2_300)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}
