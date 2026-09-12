package com.example.dz.presentation.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import androidx.compose.ui.graphics.graphicsLayer
import com.example.dz.designsystem.components.organic.OrganicPaginationDots
import com.example.dz.designsystem.components.organic.rememberEntranceProgress
import com.example.dz.designsystem.components.organic.rememberSettleProgress
import com.example.dz.designsystem.components.organic.OrganicPrimaryButton
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicHeadingFontFamily
import com.example.dz.presentation.common.SystemBackHandler
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.onboarding_next
import dz.shared.generated.resources.onboarding_page1_desc
import dz.shared.generated.resources.onboarding_page1_page_number
import dz.shared.generated.resources.onboarding_page1_synced
import dz.shared.generated.resources.onboarding_page1_title
import dz.shared.generated.resources.onboarding_page2_desc
import dz.shared.generated.resources.onboarding_page2_minutes_label
import dz.shared.generated.resources.onboarding_page2_minutes_value
import dz.shared.generated.resources.onboarding_page2_title
import dz.shared.generated.resources.onboarding_page3_book_title
import dz.shared.generated.resources.onboarding_page3_desc
import dz.shared.generated.resources.onboarding_page3_friend_status
import dz.shared.generated.resources.onboarding_page3_note
import dz.shared.generated.resources.onboarding_page3_title
import dz.shared.generated.resources.onboarding_start
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

private const val PAGE_COUNT = 3

/** Held back so the ring sweeps after its dial has landed rather than while it is arriving. */
private const val GOAL_RING_DELAY_MILLIS = 240L
private const val WEEK_FIRST_DAY_DELAY_MILLIS = 360
private const val WEEK_DAY_STAGGER_MILLIS = 55
private const val CIRCLE_FIRST_DELAY_MILLIS = 440
private const val CIRCLE_STAGGER_MILLIS = 70

/**
 * The three onboarding beats as a single screen with a horizontal pager.
 * `pagerState.currentPage` is the one source of truth: it drives the
 * illustration/title/description shown, which pagination dot is active, and
 * whether the bottom button reads "Next" or "Start" — there are no parallel
 * `isPageOne`/`isPageTwo` booleans.
 */
@Composable
fun OnboardingScreen(
    onEvent: (OnboardingEvent) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })
    val scope = rememberCoroutineScope()

    // Page 3 -> Page 2 -> Page 1 -> normal root back behavior (we simply let
    // the event fall through to the system/NavController when on page 0).
    SystemBackHandler(enabled = pagerState.currentPage > 0) {
        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
    }

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
                .padding(start = 26.dp, end = 26.dp, bottom = 26.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) { page ->
                // Each page plays itself in when it is composed, which the pager does as the
                // page scrolls into view. Keying on *being the current page* instead would look
                // right on the button and wrong on a swipe: currentPage does not flip until the
                // drag passes the snap threshold, so the incoming half-page would slide in empty
                // and only fill once it had all but arrived. The pager also disposes what it
                // scrolls away from, so coming back plays the entrance again.
                when (page) {
                    0 -> OnboardingPageOne()
                    1 -> OnboardingPageTwo()
                    else -> OnboardingPageThree()
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OrganicPaginationDots(
                    pageCount = PAGE_COUNT,
                    activeIndex = pagerState.currentPage,
                    onDotClick = { index ->
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    modifier = Modifier.weight(1f)
                )

                val isLastPage = pagerState.currentPage == PAGE_COUNT - 1
                OrganicPrimaryButton(
                    text = stringResource(if (isLastPage) Res.string.onboarding_start else Res.string.onboarding_next),
                    fullWidth = false,
                    trailingArrow = true,
                    onClick = {
                        if (isLastPage) {
                            onEvent(OnboardingEvent.StartClicked)
                        } else {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageOne() {
    OnboardingPageLayout(
        title = stringResource(Res.string.onboarding_page1_title),
        description = stringResource(Res.string.onboarding_page1_desc),
    ) {
        SyncIllustration()
    }
}

@Composable
private fun OnboardingPageTwo() {
    OnboardingPageLayout(
        title = stringResource(Res.string.onboarding_page2_title),
        description = stringResource(Res.string.onboarding_page2_desc),
    ) {
        GoalIllustration()
    }
}

@Composable
private fun OnboardingPageThree() {
    OnboardingPageLayout(
        title = stringResource(Res.string.onboarding_page3_title),
        description = stringResource(Res.string.onboarding_page3_desc),
    ) {
        SocialIllustration()
    }
}

/**
 * Shared vertical rhythm for a page: centered illustration, then title + copy.
 *
 * The copy follows the illustration in rather than arriving with it, so the eye is led down the
 * page in the order it should be read.
 */
@Composable
private fun OnboardingPageLayout(
    title: String,
    description: String,
    illustration: @Composable () -> Unit,
) {
    val titleEntrance by rememberEntranceProgress(delayMillis = 260)
    val copyEntrance by rememberEntranceProgress(delayMillis = 340)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            illustration()
        }

        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = titleEntrance
                    translationY = (1f - titleEntrance) * 18.dp.toPx()
                },
            fontFamily = organicHeadingFontFamily(),
            fontSize = 30.sp,
            lineHeight = 34.sp,
            color = OrganicColors.text
        )

        Text(
            text = description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .graphicsLayer {
                    alpha = copyEntrance
                    translationY = (1f - copyEntrance) * 14.dp.toPx()
                },
            fontFamily = organicBodyFontFamily(),
            fontSize = 16.sp,
            lineHeight = 25.sp,
            color = OrganicColors.neutral700
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

/**
 * Page 1 — two "device" cards showing the same page number, plus a synced-status chip. Preview
 * only, not clickable.
 *
 * The devices are *placed* on a spring, one then the other, and the chip only pops once both are
 * down — the illustration is about two things coming into agreement, so they have to arrive in
 * that order for the chip to mean anything.
 */
@Composable
private fun SyncIllustration() {
    val halo by rememberEntranceProgress(durationMillis = 380)
    val firstCard by rememberSettleProgress(delayMillis = 120)
    val secondCard by rememberSettleProgress(delayMillis = 210)
    val chip by rememberEntranceProgress(delayMillis = 430, durationMillis = 320)

    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .graphicsLayer {
                    alpha = halo
                    scaleX = 0.82f + 0.18f * halo
                    scaleY = 0.82f + 0.18f * halo
                }
                .clip(CircleShape)
                .background(OrganicColors.accent200)
        )

        DeviceCard(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-58).dp, y = (-14).dp)
                .graphicsLayer {
                    alpha = firstCard.coerceIn(0f, 1f)
                    translationY = (1f - firstCard) * 46.dp.toPx()
                }
                .rotate(-7f * firstCard.coerceIn(0f, 1f)),
            width = 96.dp,
            height = 132.dp,
            background = OrganicColors.neutral100,
            lineColor = OrganicColors.neutral300,
            accentLineColor = OrganicColors.accent400,
            labelColor = OrganicColors.neutral600
        )

        DeviceCard(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 56.dp, y = (-22).dp)
                .graphicsLayer {
                    alpha = secondCard.coerceIn(0f, 1f)
                    translationY = (1f - secondCard) * 46.dp.toPx()
                }
                .rotate(8f * secondCard.coerceIn(0f, 1f)),
            width = 74.dp,
            height = 150.dp,
            background = OrganicColors.accent2_900,
            lineColor = OrganicColors.accent2_700,
            accentLineColor = OrganicColors.accent400,
            labelColor = OrganicColors.accent2_300
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-6).dp)
                .graphicsLayer {
                    alpha = chip
                    scaleX = 0.7f + 0.3f * chip
                    scaleY = 0.7f + 0.3f * chip
                }
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(OrganicShape.pill))
                .clip(RoundedCornerShape(OrganicShape.pill))
                .background(OrganicColors.bg)
                .padding(horizontal = 15.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(
                imageVector = InkIcons.Sync,
                contentDescription = null,
                tint = OrganicColors.accent,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = stringResource(Res.string.onboarding_page1_synced),
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = OrganicColors.neutral800
            )
        }
    }
}

@Composable
private fun DeviceCard(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    background: Color,
    lineColor: Color,
    accentLineColor: Color,
    labelColor: Color,
) {
    Column(
        modifier = modifier
            .size(width, height)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .padding(11.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(Modifier.fillMaxWidth(0.7f).height(5.dp).clip(CircleShape).background(lineColor))
        Box(Modifier.fillMaxWidth(0.9f).height(5.dp).clip(CircleShape).background(lineColor))
        Box(Modifier.fillMaxWidth(0.84f).height(5.dp).clip(CircleShape).background(lineColor))
        Box(Modifier.fillMaxWidth(0.46f).height(5.dp).clip(CircleShape).background(accentLineColor))
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(Res.string.onboarding_page1_page_number),
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            color = labelColor
        )
    }
}

/**
 * Page 2 — circular daily-goal progress preview. The ring fills from 0 to ~70% once this page
 * becomes the pager's current one, after the dial has landed: a ring that sweeps while its dial
 * is still arriving reads as two things happening rather than one.
 */
@Composable
private fun GoalIllustration() {
    val progress = remember { Animatable(0f) }
    val halo by rememberEntranceProgress(durationMillis = 380)
    val dial by rememberSettleProgress(delayMillis = 110)

    LaunchedEffect(Unit) {
        delay(GOAL_RING_DELAY_MILLIS)
        progress.animateTo(0.7f, animationSpec = tween(durationMillis = 350))
    }

    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .graphicsLayer {
                    alpha = halo
                    scaleX = 0.82f + 0.18f * halo
                    scaleY = 0.82f + 0.18f * halo
                }
                .clip(CircleShape)
                .background(OrganicColors.accent2_200)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(158.dp)
                    .graphicsLayer {
                        alpha = dial.coerceIn(0f, 1f)
                        scaleX = 0.7f + 0.3f * dial.coerceIn(0f, 1.15f)
                        scaleY = 0.7f + 0.3f * dial.coerceIn(0f, 1.15f)
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    val inset = stroke.width / 2f
                    val arcSize = Size(size.width - inset * 2, size.height - inset * 2)
                    drawArc(
                        color = OrganicColors.accent2_300,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke
                    )
                    drawArc(
                        color = OrganicColors.accent,
                        startAngle = -90f,
                        sweepAngle = 360f * progress.value,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.onboarding_page2_minutes_value),
                        fontFamily = organicHeadingFontFamily(),
                        fontSize = 44.sp,
                        color = OrganicColors.accent900
                    )
                    Text(
                        text = stringResource(Res.string.onboarding_page2_minutes_label),
                        fontFamily = organicBodyFontFamily(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = OrganicColors.neutral700
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // The week fills in left to right, the way it was lived.
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { index ->
                    val day by rememberEntranceProgress(
                        delayMillis = WEEK_FIRST_DAY_DELAY_MILLIS + index * WEEK_DAY_STAGGER_MILLIS,
                        durationMillis = 240,
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer {
                                alpha = day
                                scaleX = 0.5f + 0.5f * day
                                scaleY = 0.5f + 0.5f * day
                            }
                            .clip(CircleShape)
                            .background(OrganicColors.accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = InkIcons.Done,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
                // "Today" marker — a dashed ring, distinct from both the
                // completed (filled + check) and upcoming (flat sage) days.
                val today by rememberEntranceProgress(
                    delayMillis = WEEK_FIRST_DAY_DELAY_MILLIS + 3 * WEEK_DAY_STAGGER_MILLIS,
                    durationMillis = 240,
                )
                Canvas(
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer {
                            alpha = today
                            scaleX = 0.5f + 0.5f * today
                            scaleY = 0.5f + 0.5f * today
                        }
                ) {
                    val strokeWidth = 3.dp.toPx()
                    drawCircle(
                        color = OrganicColors.accent400,
                        radius = (size.minDimension - strokeWidth) / 2f,
                        style = Stroke(
                            width = strokeWidth,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 3f))
                        )
                    )
                }
                repeat(3) { index ->
                    val day by rememberEntranceProgress(
                        delayMillis = WEEK_FIRST_DAY_DELAY_MILLIS + (4 + index) * WEEK_DAY_STAGGER_MILLIS,
                        durationMillis = 240,
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer {
                                alpha = day
                                scaleX = 0.5f + 0.5f * day
                                scaleY = 0.5f + 0.5f * day
                            }
                            .clip(CircleShape)
                            .background(OrganicColors.accent2_300)
                    )
                }
            }
        }
    }
}

/**
 * Page 3 — a friend's book, their note, and their circle. All preview-only.
 *
 * Read as a small scene arriving in the order it happened: the book is put down, someone says
 * what they are reading, the note follows, and the rest of the circle gathers behind it. The chip
 * and the note come in from their own sides, so neither looks like it slid out of the other.
 */
@Composable
private fun SocialIllustration() {
    val halo by rememberEntranceProgress(durationMillis = 380)
    val book by rememberSettleProgress(delayMillis = 120)
    val status by rememberEntranceProgress(delayMillis = 300, durationMillis = 320)
    val note by rememberEntranceProgress(delayMillis = 390, durationMillis = 320)

    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .graphicsLayer {
                    alpha = halo
                    scaleX = 0.82f + 0.18f * halo
                    scaleY = 0.82f + 0.18f * halo
                }
                .clip(CircleShape)
                .background(OrganicColors.neutral200)
        )

        Box(
            modifier = Modifier
                .size(104.dp, 150.dp)
                .graphicsLayer {
                    alpha = book.coerceIn(0f, 1f)
                    translationY = (1f - book) * 40.dp.toPx()
                }
                .shadow(elevation = 14.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF8D5F45), Color(0xFF5C3D31))))
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.onboarding_page3_book_title),
                fontFamily = organicHeadingFontFamily(),
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = OrganicColors.bg
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-4).dp, y = 26.dp)
                .graphicsLayer {
                    alpha = status
                    translationX = (1f - status) * (-26).dp.toPx()
                }
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(OrganicShape.pill))
                .clip(RoundedCornerShape(OrganicShape.pill))
                .background(OrganicColors.bg)
                .padding(start = 3.dp, end = 13.dp, top = 3.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.size(26.dp).clip(CircleShape).background(OrganicColors.accent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
            Text(
                text = stringResource(Res.string.onboarding_page3_friend_status),
                fontFamily = organicBodyFontFamily(),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = OrganicColors.neutral800
            )
        }

        Text(
            text = stringResource(Res.string.onboarding_page3_note),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 10.dp, y = (-38).dp)
                .graphicsLayer {
                    alpha = note
                    translationX = (1f - note) * 26.dp.toPx()
                }
                .widthIn(max = 138.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 6.dp))
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 6.dp))
                .background(OrganicColors.accent2_800)
                .padding(horizontal = 13.dp, vertical = 10.dp),
            fontFamily = organicBodyFontFamily(),
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = OrganicColors.bg
        )

        // The circle gathers one at a time, so "+5" reads as the last to join rather than as
        // part of a block that appeared at once.
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 30.dp, y = (-30).dp)
        ) {
            AvatarRing(
                initials = "J",
                background = OrganicColors.accent2_600,
                offsetStart = 0.dp,
                delayMillis = CIRCLE_FIRST_DELAY_MILLIS,
            )
            AvatarRing(
                initials = "R",
                background = OrganicColors.accent600,
                offsetStart = (-10).dp,
                delayMillis = CIRCLE_FIRST_DELAY_MILLIS + CIRCLE_STAGGER_MILLIS,
            )
            AvatarRing(
                initials = "+5",
                background = OrganicColors.neutral400,
                textColor = OrganicColors.neutral900,
                offsetStart = (-10).dp,
                delayMillis = CIRCLE_FIRST_DELAY_MILLIS + 2 * CIRCLE_STAGGER_MILLIS,
            )
        }
    }
}

@Composable
private fun AvatarRing(
    initials: String,
    background: Color,
    offsetStart: Dp,
    delayMillis: Int,
    textColor: Color = Color.White,
) {
    val arrival by rememberEntranceProgress(delayMillis = delayMillis, durationMillis = 260)

    Box(
        modifier = Modifier
            .offset(x = offsetStart)
            .graphicsLayer {
                alpha = arrival
                scaleX = 0.6f + 0.4f * arrival
                scaleY = 0.6f + 0.4f * arrival
            }
            .size(30.dp)
            .clip(CircleShape)
            .background(OrganicColors.bg)
            .padding(3.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            fontFamily = organicBodyFontFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = textColor
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen()
}
