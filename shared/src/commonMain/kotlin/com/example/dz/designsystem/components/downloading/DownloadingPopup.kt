package com.example.dz.designsystem.components.downloading

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.book_cover
import dz.shared.generated.resources.book_download_cover_cd
import dz.shared.generated.resources.book_downloading_progress
import dz.shared.generated.resources.reading_book_author
import dz.shared.generated.resources.reading_book_title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

private data class DownloadingPopupMetrics(
    val horizontalPadding: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val coverCorner: Dp,
    val titleTopSpacing: Dp,
    val authorTopSpacing: Dp,
    val progressTopSpacing: Dp,
    val progressWidth: Dp,
    val progressHeight: Dp,
    val progressCorner: Dp,
    val progressTextTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val authorFontSize: TextUnit,
    val progressTextFontSize: TextUnit
)

@Composable
fun DownloadingPopup(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.reading_book_title),
    author: String = stringResource(Res.string.reading_book_author),
    progress: Float = 0.25f,
    coverRes: DrawableResource = Res.drawable.book_cover
) {
    DownloadingPopup(
        modifier = modifier,
        title = title,
        author = author,
        progress = progress,
        cover = { coverModifier ->
            Image(
                painter = painterResource(coverRes),
                contentDescription = stringResource(Res.string.book_download_cover_cd),
                contentScale = ContentScale.Crop,
                modifier = coverModifier
            )
        }
    )
}

@Composable
fun DownloadingPopup(
    modifier: Modifier = Modifier,
    title: String,
    author: String,
    progress: Float,
    cover: @Composable BoxScope.(Modifier) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val metrics = rememberDownloadingPopupMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val safeProgress = progress.coerceIn(0f, 1f)
        val progressPercent = (safeProgress * 100).roundToInt()
        val colorScheme = MaterialTheme.colorScheme

        DownloadingPopupBackground()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = metrics.horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(metrics.coverWidth, metrics.coverHeight)
                    .shadow(
                        elevation = 28.dp,
                        shape = RoundedCornerShape(metrics.coverCorner),
                        clip = false
                    )
                    .clip(RoundedCornerShape(metrics.coverCorner))
            ) {
                cover(Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

            Text(
                text = title,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.titleFontSize,
                    lineHeight = metrics.titleFontSize * 1.24f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            if (author.isNotBlank()) {
                Spacer(modifier = Modifier.height(metrics.authorTopSpacing))

                Text(
                    text = author,
                    color = colorScheme.onBackground.copy(alpha = 0.78f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.authorFontSize,
                        lineHeight = metrics.authorFontSize * 1.25f,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(metrics.progressTopSpacing))

            DownloadingProgressBar(
                progress = safeProgress,
                metrics = metrics
            )

            Spacer(modifier = Modifier.height(metrics.progressTextTopSpacing))

            Text(
                text = stringResource(Res.string.book_downloading_progress, progressPercent),
                color = colorScheme.onBackground.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = metrics.progressTextFontSize,
                    lineHeight = metrics.progressTextFontSize * 1.25f,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
private fun DownloadingPopupBackground() {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.18f
                    scaleY = 1.18f
                }
                .blur(42.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorScheme.tertiary.copy(alpha = 0.22f),
                            colorScheme.primary.copy(alpha = 0.18f),
                            colorScheme.background
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.scrim.copy(alpha = 0.16f))
        )
    }
}

@Composable
private fun DownloadingProgressBar(
    progress: Float,
    metrics: DownloadingPopupMetrics
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.progressHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = metrics.progressWidth, height = metrics.progressHeight)
                .clip(RoundedCornerShape(metrics.progressCorner))
                .background(colorScheme.onBackground.copy(alpha = 0.42f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(metrics.progressHeight)
                    .clip(RoundedCornerShape(metrics.progressCorner))
                    .background(colorScheme.onBackground)
            )
        }
    }
}

@Composable
private fun rememberDownloadingPopupMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): DownloadingPopupMetrics {
    val widthRatio = dimensionRatio(maxWidth, 390.dp, 0.84f, 1.16f)
    val heightRatio = dimensionRatio(maxHeight, 844.dp, 0.78f, 1.1f)
    val compactness = kotlin.math.min(widthRatio, heightRatio)

    return remember(maxWidth, maxHeight) {
        val coverWidth = (292.dp * compactness).coerceIn(236.dp, 320.dp)

        DownloadingPopupMetrics(
            horizontalPadding = (40.dp * widthRatio).coerceIn(28.dp, 48.dp),
            coverWidth = coverWidth,
            coverHeight = coverWidth * 1.36f,
            coverCorner = (18.dp * compactness).coerceIn(14.dp, 22.dp),
            titleTopSpacing = (34.dp * compactness).coerceIn(24.dp, 38.dp),
            authorTopSpacing = (10.dp * compactness).coerceIn(8.dp, 14.dp),
            progressTopSpacing = (34.dp * compactness).coerceIn(26.dp, 40.dp),
            progressWidth = (maxWidth - 80.dp).coerceIn(250.dp, 330.dp),
            progressHeight = (4.dp * compactness).coerceIn(3.dp, 5.dp),
            progressCorner = 50.dp,
            progressTextTopSpacing = (18.dp * compactness).coerceIn(14.dp, 22.dp),
            titleFontSize = (22f * compactness).coerceIn(19f, 25f).sp,
            authorFontSize = (16f * compactness).coerceIn(14f, 18f).sp,
            progressTextFontSize = (16f * compactness).coerceIn(14f, 18f).sp
        )
    }
}

private fun dimensionRatio(
    value: Dp,
    base: Dp,
    min: Float,
    max: Float
): Float {
    if (!value.value.isFinite() || !base.value.isFinite() || base.value == 0f) {
        return 1f
    }

    return (value / base).coerceIn(min, max)
}

@Preview(
    name = "Downloading Popup - 25%",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun DownloadingPopupPreview() {
    DZTheme(darkTheme = false) {
        DownloadingPopup(progress = 0.45f)
    }
}
