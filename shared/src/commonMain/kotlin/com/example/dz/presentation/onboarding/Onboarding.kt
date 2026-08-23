package com.example.dz.presentation.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.dz.designsystem.components.organic.OrganicPaginationDots
import com.example.dz.designsystem.components.organic.OrganicPrimaryButton
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.OrganicShape
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicDisplayFontFamily
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
import dz.shared.generated.resources.skip
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

private const val PAGE_COUNT = 3

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
    OnboardingBackHandler(enabled = pagerState.currentPage > 0) {
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(Res.string.skip),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onEvent(OnboardingEvent.SkipClicked) }
                        .padding(8.dp),
                    fontFamily = organicBodyFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = OrganicColors.neutral700
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> OnboardingPageOne()
                    1 -> OnboardingPageTwo(isVisible = pagerState.currentPage == 1)
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
        description = stringResource(Res.string.onboarding_page1_desc)
    ) {
        SyncIllustration()
    }
}

@Composable
private fun OnboardingPageTwo(isVisible: Boolean) {
    OnboardingPageLayout(
        title = stringResource(Res.string.onboarding_page2_title),
        description = stringResource(Res.string.onboarding_page2_desc)
    ) {
        GoalIllustration(isVisible = isVisible)
    }
}

@Composable
private fun OnboardingPageThree() {
    OnboardingPageLayout(
        title = stringResource(Res.string.onboarding_page3_title),
        description = stringResource(Res.string.onboarding_page3_desc)
    ) {
        SocialIllustration()
    }
}

/** Shared vertical rhythm for a page: centered illustration, then title + copy. */
@Composable
private fun OnboardingPageLayout(
    title: String,
    description: String,
    illustration: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            illustration()
        }

        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            fontFamily = organicDisplayFontFamily(),
            fontSize = 30.sp,
            lineHeight = 34.sp,
            color = OrganicColors.text
        )

        Text(
            text = description,
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            fontFamily = organicBodyFontFamily(),
            fontSize = 16.sp,
            lineHeight = 25.sp,
            color = OrganicColors.neutral700
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

/** Page 1 — two "device" cards showing the same page number, plus a synced-status chip. Preview only, not clickable. */
@Composable
private fun SyncIllustration() {
    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .clip(CircleShape)
                .background(OrganicColors.accent200)
        )

        DeviceCard(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-58).dp, y = (-14).dp)
                .rotate(-7f),
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
                .rotate(8f),
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
 * Page 2 — circular daily-goal progress preview. Animates from 0 to ~70%
 * over ~350ms the moment this page becomes the pager's current page.
 */
@Composable
private fun GoalIllustration(isVisible: Boolean) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            progress.snapTo(0f)
            progress.animateTo(0.7f, animationSpec = tween(durationMillis = 350))
        }
    }

    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .clip(CircleShape)
                .background(OrganicColors.accent2_200)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(158.dp), contentAlignment = Alignment.Center) {
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
                        fontFamily = organicDisplayFontFamily(),
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier.size(24.dp).clip(CircleShape).background(OrganicColors.accent),
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
                Canvas(modifier = Modifier.size(24.dp)) {
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
                repeat(3) {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(OrganicColors.accent2_300))
                }
            }
        }
    }
}

/** Page 3 — a friend's book, their note, and their circle. All preview-only. */
@Composable
private fun SocialIllustration() {
    Box(
        modifier = Modifier.size(270.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .clip(CircleShape)
                .background(OrganicColors.neutral200)
        )

        Box(
            modifier = Modifier
                .size(104.dp, 150.dp)
                .shadow(elevation = 14.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF8D5F45), Color(0xFF5C3D31))))
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.onboarding_page3_book_title),
                fontFamily = organicDisplayFontFamily(),
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = OrganicColors.bg
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-4).dp, y = 26.dp)
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

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 30.dp, y = (-30).dp)
        ) {
            AvatarRing(initials = "J", background = OrganicColors.accent2_600, offsetStart = 0.dp)
            AvatarRing(initials = "R", background = OrganicColors.accent600, offsetStart = (-10).dp)
            AvatarRing(initials = "+5", background = OrganicColors.neutral400, textColor = OrganicColors.neutral900, offsetStart = (-10).dp)
        }
    }
}

@Composable
private fun AvatarRing(
    initials: String,
    background: Color,
    offsetStart: Dp,
    textColor: Color = Color.White,
) {
    Box(
        modifier = Modifier
            .offset(x = offsetStart)
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
