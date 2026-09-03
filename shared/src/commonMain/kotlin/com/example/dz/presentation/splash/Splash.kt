package com.example.dz.presentation.splash

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.organic.OrganicPrimaryButton
import com.example.dz.designsystem.theme.OrganicColors
import com.example.dz.designsystem.theme.organicBodyFontFamily
import com.example.dz.designsystem.theme.organicDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.get_started
import dz.shared.generated.resources.splash_signin_action
import dz.shared.generated.resources.splash_signin_prefix
import dz.shared.generated.resources.splash_subtitle
import dz.shared.generated.resources.splash_wordmark
import org.jetbrains.compose.resources.stringResource

/**
 * First screen of the app. Brand moment while [SplashViewModel] checks for a restored session in
 * the background (silently forwarding straight to Home if one is found); otherwise the reader
 * chooses "Get started" (→ onboarding, or straight past it if already seen) or "Sign in" (→
 * existing sign-in screen). The shelf illustration is decorative only.
 */
@Composable
fun SplashScreen(
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {},
) {
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

            BookshelfIllustration(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OrganicPrimaryButton(
                    text = stringResource(Res.string.get_started),
                    onClick = onGetStarted
                )

                SignInPrompt(
                    onSignInClick = onSignIn,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun BrandBlock(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // The "d" mark — a filled accent circle with the wordmark's first letter.
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(OrganicColors.accent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "d",
                fontFamily = organicDisplayFontFamily(),
                fontSize = 26.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(Res.string.splash_wordmark),
            fontFamily = organicDisplayFontFamily(),
            fontSize = 76.sp,
            lineHeight = 68.sp,
            color = OrganicColors.accent900
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(Res.string.splash_subtitle),
            modifier = Modifier.widthIn(max = 250.dp),
            fontFamily = organicBodyFontFamily(),
            fontSize = 17.sp,
            lineHeight = 26.sp,
            color = OrganicColors.neutral800
        )
    }
}

@Composable
private fun SignInPrompt(
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bodyFont = organicBodyFontFamily()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.splash_signin_prefix),
            fontFamily = bodyFont,
            fontSize = 14.sp,
            color = OrganicColors.neutral700
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(Res.string.splash_signin_action),
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable(onClick = onSignInClick)
                // Widen the tap target a little beyond the visible glyphs.
                .padding(horizontal = 4.dp, vertical = 4.dp),
            fontFamily = bodyFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = OrganicColors.accent700
        )
    }
}

/**
 * Decorative bookshelf: five spines of varying height/tilt resting on a
 * shelf line. Not interactive — no click handling anywhere in this subtree.
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

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            spines.forEach { spine ->
                Box(
                    modifier = Modifier
                        .width(spine.width)
                        .height(spine.height)
                        .rotate(spine.rotation)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                        .background(spine.brush)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
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
