package com.example.dz.designsystem.components.popups

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.theme.ColorPrimary
import com.example.dz.designsystem.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.cd_delete
import dz.shared.generated.resources.popup_delete_purchased_message
import dz.shared.generated.resources.popup_remove_everywhere
import dz.shared.generated.resources.popup_remove_from_collection
import dz.shared.generated.resources.trash_bin
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class DeleteBooksPopupMetrics(
    val buttonHorizontalPadding: Dp,
    val cardHorizontalPadding: Dp,
    val cardTopSpacing: Dp,
    val cardHeight: Dp,
    val cardCorner: Dp,
    val illustrationSize: Dp,
    val trashBinSize: Dp,
    val titleTopSpacing: Dp,
    val titleSize: TextUnit,
    val buttonHeight: Dp,
    val buttonCorner: Dp,
    val buttonSpacing: Dp,
    val bottomSpacing: Dp,
    val buttonTextSize: TextUnit
)

@Composable
fun DeleteBooksPopup(
    onRemoveFromCollectionClick: () -> Unit,
    onRemoveEverywhereClick: () -> Unit,
    modifier: Modifier = Modifier,
    trashBinRes: DrawableResource = Res.drawable.trash_bin
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberDeleteBooksPopupMetrics(maxWidth = maxWidth, maxHeight = maxHeight)
        val colorScheme = MaterialTheme.colorScheme

        DeleteBooksPopupBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(metrics.cardTopSpacing))

            DeleteBooksCard(
                metrics = metrics,
                trashBinRes = trashBinRes,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = metrics.cardHorizontalPadding)
            )

            Spacer(modifier = Modifier.weight(1f))

            DeleteBooksButton(
                text = stringResource(Res.string.popup_remove_from_collection),
                backgroundColor = colorScheme.surface,
                contentColor = ColorPrimary,
                metrics = metrics,
                onClick = onRemoveFromCollectionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = metrics.buttonHorizontalPadding)
            )

            Spacer(modifier = Modifier.height(metrics.buttonSpacing))

            DeleteBooksButton(
                text = stringResource(Res.string.popup_remove_everywhere),
                backgroundColor = ColorPrimary,
                contentColor = colorScheme.onPrimary,
                metrics = metrics,
                onClick = onRemoveEverywhereClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = metrics.buttonHorizontalPadding)
            )

            Spacer(modifier = Modifier.height(metrics.bottomSpacing))
        }
    }
}

@Composable
private fun DeleteBooksPopupBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF828282))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(34.dp)
                .background(Color.Black.copy(alpha = 0.22f))
        )
    }
}

@Composable
private fun DeleteBooksCard(
    metrics: DeleteBooksPopupMetrics,
    trashBinRes: DrawableResource,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .height(metrics.cardHeight)
            .clip(RoundedCornerShape(metrics.cardCorner))
            .background(colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(29.dp))

        Box(
            modifier = Modifier.size(metrics.illustrationSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawDeleteBooksIllustrationBackground()
            }

            Image(
                painter = painterResource(trashBinRes),
                contentDescription = stringResource(Res.string.cd_delete),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(metrics.trashBinSize)
            )
        }

        Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

        Text(
            text = stringResource(Res.string.popup_delete_purchased_message),
            color = colorScheme.onSurface.copy(alpha = 0.76f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.titleSize,
                lineHeight = metrics.titleSize * 1.24f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

private fun DrawScope.drawDeleteBooksIllustrationBackground() {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension * 0.43f

    drawCircle(
        color = Color(0xFFF5F5F5),
        radius = radius,
        center = center
    )

    val purple = ColorPrimary
    val mint = Color(0xFF5ECFC6)
    val orange = Color(0xFFFFB533)
    val blue = Color(0xFF64A4F2)
    val magenta = Color(0xFFD95DE8)

    drawDeleteRing(Offset(size.width * 0.16f, size.height * 0.2f), purple, size.minDimension * 0.027f)
    drawDeleteRing(Offset(size.width * 0.83f, size.height * 0.23f), purple, size.minDimension * 0.041f)
    drawDeleteRing(Offset(size.width * 0.12f, size.height * 0.76f), orange, size.minDimension * 0.032f)
    drawDeleteRing(Offset(size.width * 0.81f, size.height * 0.76f), purple, size.minDimension * 0.015f)

    drawDeletePlus(Offset(size.width * 0.88f, size.height * 0.49f), blue, size.minDimension * 0.015f)
    drawDeletePlus(Offset(size.width * 0.28f, size.height * 0.86f), Color(0xFFA65EEA), size.minDimension * 0.018f)

    drawDeleteSquiggle(Offset(size.width * 0.13f, size.height * 0.52f), mint, size.minDimension * 0.019f)
    drawDeleteSquiggle(Offset(size.width * 0.73f, size.height * 0.53f), magenta, size.minDimension * 0.009f)

    drawCircle(color = purple, radius = size.minDimension * 0.015f, center = Offset(size.width * 0.55f, size.height * 0.91f))
}

private fun DrawScope.drawDeleteRing(center: Offset, color: Color, radius: Float) {
    drawCircle(
        color = color,
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.42f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawDeletePlus(center: Offset, color: Color, length: Float) {
    drawLine(
        color = color,
        start = Offset(center.x - length, center.y),
        end = Offset(center.x + length, center.y),
        strokeWidth = length * 0.58f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(center.x, center.y - length),
        end = Offset(center.x, center.y + length),
        strokeWidth = length * 0.58f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawDeleteSquiggle(start: Offset, color: Color, length: Float) {
    drawLine(
        color = color,
        start = start,
        end = Offset(start.x + length * 0.72f, start.y),
        strokeWidth = length * 0.45f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(start.x + length * 0.72f, start.y),
        end = Offset(start.x + length * 0.72f, start.y + length * 0.46f),
        strokeWidth = length * 0.45f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = color,
        start = Offset(start.x + length * 0.72f, start.y + length * 0.46f),
        end = Offset(start.x + length * 1.25f, start.y + length * 0.46f),
        strokeWidth = length * 0.45f,
        cap = StrokeCap.Round
    )
}

@Composable
private fun DeleteBooksButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    metrics: DeleteBooksPopupMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.buttonHeight)
            .clip(RoundedCornerShape(metrics.buttonCorner))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.buttonTextSize,
                lineHeight = metrics.buttonTextSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberDeleteBooksPopupMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): DeleteBooksPopupMetrics {
    val compact = maxWidth < 370.dp
    val short = maxHeight < 760.dp

    return remember(maxWidth, maxHeight) {
        DeleteBooksPopupMetrics(
            buttonHorizontalPadding = if (compact) 24.dp else 29.dp,
            cardHorizontalPadding = if (compact) 45.dp else 49.dp,
            cardTopSpacing = if (short) 156.dp else 206.dp,
            cardHeight = if (compact) 318.dp else 344.dp,
            cardCorner = if (compact) 44.dp else 52.dp,
            illustrationSize = if (compact) 232.dp else 254.dp,
            trashBinSize = if (compact) 170.dp else 188.dp,
            titleTopSpacing = if (compact) 7.dp else 9.dp,
            titleSize = if (compact) 17.sp else 18.sp,
            buttonHeight = if (compact) 70.dp else 73.dp,
            buttonCorner = 22.dp,
            buttonSpacing = 12.dp,
            bottomSpacing = if (short) 24.dp else 31.dp,
            buttonTextSize = if (compact) 15.sp else 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteBooksPopupPreview() {
    DZTheme {
        DeleteBooksPopup(
            onRemoveFromCollectionClick = {},
            onRemoveEverywhereClick = {}
        )
    }
}
