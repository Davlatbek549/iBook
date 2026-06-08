package com.example.dz.screens.purchase_confirmation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

data class PurchaseConfirmationUiState(
    val title: String = "Murder Board",
    val author: String = "Brian Shea"
)

private data class PurchaseConfirmationMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val coverTopOffset: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val coverCorner: Dp,
    val coverShadow: Dp,
    val titleTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val authorTopSpacing: Dp,
    val authorFontSize: TextUnit
)

@Composable
fun PurchaseConfirmationScreen(
    modifier: Modifier = Modifier,
    uiState: PurchaseConfirmationUiState = PurchaseConfirmationUiState(),
    onBackClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberPurchaseConfirmationMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            PurchaseConfirmationBackground()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(
                        start = metrics.horizontalPadding,
                        top = metrics.topSpacing
                    )
            ) {
                CircleBackButton(
                    size = metrics.backButtonSize,
                    iconSize = metrics.backIconSize,
                    onClick = onBackClick
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = metrics.horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(metrics.coverTopOffset))

                Image(
                    painter = painterResource(Res.drawable.book_cover),
                    contentDescription = "${uiState.title} book cover",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(width = metrics.coverWidth, height = metrics.coverHeight)
                        .shadow(metrics.coverShadow, RoundedCornerShape(metrics.coverCorner))
                        .clip(RoundedCornerShape(metrics.coverCorner))
                )

                Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

                Text(
                    text = uiState.title,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.titleFontSize,
                        lineHeight = metrics.titleFontSize * 1.18f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.authorTopSpacing))

                Text(
                    text = "By ${uiState.author}",
                    color = Color.White.copy(alpha = 0.82f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.authorFontSize,
                        lineHeight = metrics.authorFontSize * 1.2f,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )
                )
            }
        }
    }
}

@Composable
fun PurchaseConfirmation(
    modifier: Modifier = Modifier,
    uiState: PurchaseConfirmationUiState = PurchaseConfirmationUiState(),
    onBackClick: () -> Unit = {}
) {
    PurchaseConfirmationScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@Composable
private fun PurchaseConfirmationBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14272C))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.18f
                    scaleY = 1.18f
                }
                .blur(50.dp)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF442125),
                        Color(0xFF173D3D),
                        Color(0xFF273034),
                        Color(0xFF8B2F20)
                    )
                )
            )
            drawCircle(
                color = Color(0xFF7D2525).copy(alpha = 0.68f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.6f, -size.height * 0.02f)
            )
            drawCircle(
                color = Color(0xFF0D5555).copy(alpha = 0.62f),
                radius = size.width * 0.52f,
                center = Offset(size.width * 0.82f, size.height * 0.2f)
            )
            drawCircle(
                color = Color(0xFF9E3A25).copy(alpha = 0.58f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.7f, size.height * 0.98f)
            )
            drawRect(
                color = Color(0xFF9B9B9B).copy(alpha = 0.44f),
                topLeft = Offset(0f, size.height * 0.5f),
                size = Size(size.width, size.height * 0.42f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.12f))
        )
    }
}

@Composable
private fun CircleBackButton(
    size: Dp,
    iconSize: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.16f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_back),
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
private fun rememberPurchaseConfirmationMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PurchaseConfirmationMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 390.dp).coerceIn(0.86f, 1.25f)
    val heightScale = (maxHeight / 844.dp).coerceIn(0.86f, 1.2f)
    val compactness = minOf(widthScale, heightScale)

    PurchaseConfirmationMetrics(
        horizontalPadding = (28.dp * widthScale).coerceIn(22.dp, 42.dp),
        topSpacing = (22.dp * heightScale).coerceIn(14.dp, 34.dp),
        backButtonSize = (46.dp * compactness).coerceIn(40.dp, 54.dp),
        backIconSize = (27.dp * compactness).coerceIn(22.dp, 30.dp),
        coverTopOffset = (50.dp * heightScale).coerceIn(40.dp, 68.dp),
        coverWidth = (162.dp * widthScale).coerceIn(146.dp, 192.dp),
        coverHeight = (244.dp * compactness).coerceIn(220.dp, 286.dp),
        coverCorner = (22.dp * compactness).coerceIn(18.dp, 26.dp),
        coverShadow = (18.dp * compactness).coerceIn(14.dp, 22.dp),
        titleTopSpacing = (18.dp * heightScale).coerceIn(14.dp, 24.dp),
        titleFontSize = (20f * compactness).coerceIn(18f, 24f).sp,
        authorTopSpacing = (12.dp * heightScale).coerceIn(8.dp, 16.dp),
        authorFontSize = (16f * compactness).coerceIn(14f, 19f).sp
    )
}

@Preview(showBackground = false, widthDp = 390, heightDp = 844)
@Composable
private fun PurchaseConfirmationScreenPreview() {
    DZTheme {
        PurchaseConfirmationScreen()
    }
}
