package com.example.dz.features.onboarding.splash

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.app_components.icons.InkIcons
import com.example.dz.getPlatform
import com.example.dz.theme.InkColors
import com.example.dz.theme.inkBodyFontFamily
import com.example.dz.theme.inkColors
import com.example.dz.theme.inkDisplayFontFamily
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.app_name
import dz.shared.generated.resources.splash_tagline
import dz.shared.generated.resources.splash_version
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

private const val RING_TEXT = "EST. ON GOOD BOOKS · DZ · EST. ON GOOD BOOKS · DZ · "

@Composable
fun SplashScreen(onSplashFinished: () -> Unit = {}) {
    LaunchedEffect(Unit) {
        delay(1500)
        onSplashFinished()
    }

    val colors = inkColors()
    val displayFont = inkDisplayFontFamily()
    val bodyFont = inkBodyFontFamily()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.paper)
    ) {
        val scale = remember(maxWidth, maxHeight) {
            minOf(maxWidth / 390.dp, maxHeight / 844.dp).coerceIn(0.7f, 1.25f)
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 50.dp * scale),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PressStampEmblem(
                size = 216.dp * scale,
                scale = scale,
                colors = colors,
                displayFont = displayFont,
                bodyFont = bodyFont
            )

            Spacer(modifier = Modifier.height(34.dp * scale))

            OrnamentRule(colors = colors)

            Spacer(modifier = Modifier.height(18.dp * scale))

            Text(
                text = stringResource(Res.string.splash_tagline),
                fontFamily = displayFont,
                fontStyle = FontStyle.Italic,
                fontSize = (15f * scale).sp,
                color = colors.inkSoft
            )
        }

        Text(
            text = "${stringResource(Res.string.app_name)} for ${getPlatform().name.substringBefore(" ")} · ${stringResource(Res.string.splash_version)}".uppercase(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp),
            fontFamily = bodyFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            letterSpacing = 1.6.sp,
            color = colors.muted
        )
    }
}

/** Press-stamp emblem: double hairline ring, ring of type, mark + monogram. */
@Composable
private fun PressStampEmblem(
    size: Dp,
    scale: Float,
    colors: InkColors,
    displayFont: FontFamily,
    bodyFont: FontFamily
) {
    val textMeasurer = rememberTextMeasurer()
    val ringStyle = TextStyle(
        fontFamily = bodyFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = (9f * scale).sp,
        color = colors.muted
    )
    val ringLayouts = remember(ringStyle, textMeasurer) {
        RING_TEXT.map { textMeasurer.measure(AnnotatedString(it.toString()), ringStyle) }
    }

    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // emblem is designed on a 216-unit grid
            val unit = this.size.minDimension / 216f
            val hairline = Stroke(width = 1.dp.toPx())

            drawCircle(color = colors.line, radius = 104f * unit, center = center, style = hairline)
            drawCircle(color = colors.line, radius = 99.5f * unit, center = center, style = hairline)
            drawCircle(color = colors.line, radius = 64f * unit, center = center, style = hairline)

            // ring of type at radius 82, characters evenly spaced around the full circle
            val ringRadius = 82f * unit
            val step = 360f / ringLayouts.size
            ringLayouts.forEachIndexed { i, layout ->
                rotate(degrees = i * step, pivot = center) {
                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(
                            center.x - layout.size.width / 2f,
                            center.y - ringRadius - layout.size.height / 2f
                        )
                    )
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = InkIcons.BookOpen,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(34.dp * scale)
            )
            Spacer(modifier = Modifier.height(8.dp * scale))
            Text(
                text = stringResource(Res.string.app_name),
                fontFamily = displayFont,
                fontWeight = FontWeight.Medium,
                fontSize = (38f * scale).sp,
                letterSpacing = (-0.4f).sp,
                color = colors.ink
            )
        }
    }
}

/** Ornament rule: hairline — diamond — hairline. */
@Composable
private fun OrnamentRule(colors: InkColors) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.size(width = 36.dp, height = 1.dp).background(colors.line))
        Box(modifier = Modifier.size(6.dp).rotate(45f).background(colors.accent))
        Box(modifier = Modifier.size(width = 36.dp, height = 1.dp).background(colors.line))
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
