package com.example.ibook.screens.payment_failed

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.ui.theme.IBookTheme
import kotlin.math.min

data class PaymentFailedUiState(
    val bookPrice: String = "\$10.00",
    val taxAndFees: String = "0.00",
    val total: String = "\$10.00",
    val cardLastDigits: String = "**035"
)

private data class PaymentFailedMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val crossTopSpacing: Dp,
    val crossWidth: Dp,
    val crossHeight: Dp,
    val titleTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val subtitleTopSpacing: Dp,
    val subtitleFontSize: TextUnit,
    val summaryTopSpacing: Dp,
    val summaryCorner: Dp,
    val summaryPadding: Dp,
    val rowSpacing: Dp,
    val labelFontSize: TextUnit,
    val valueFontSize: TextUnit,
    val discountButtonWidth: Dp,
    val smallButtonHeight: Dp,
    val smallButtonFontSize: TextUnit,
    val paymentTopSpacing: Dp,
    val paymentHeight: Dp,
    val paymentCorner: Dp,
    val cardCircleSize: Dp,
    val changeButtonWidth: Dp,
    val getBackButtonHeight: Dp,
    val getBackButtonCorner: Dp,
    val bottomSpacing: Dp
)

@Composable
fun PaymentFailedScreen(
    modifier: Modifier = Modifier,
    uiState: PaymentFailedUiState = PaymentFailedUiState(),
    onBackClick: () -> Unit = {},
    onDiscountCodeClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onGetBackClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberPaymentFailedMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            PaymentFailedBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = metrics.horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(metrics.topSpacing))

                Row(modifier = Modifier.fillMaxWidth()) {
                    CircleBackButton(
                        size = metrics.backButtonSize,
                        iconSize = metrics.backIconSize,
                        onClick = onBackClick
                    )
                }

                Spacer(modifier = Modifier.height(metrics.crossTopSpacing))

                FailedPaymentArt(
                    modifier = Modifier.size(
                        width = metrics.crossWidth,
                        height = metrics.crossHeight
                    )
                )

                Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

                Text(
                    text = "Payment failed!",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.titleFontSize,
                        lineHeight = metrics.titleFontSize * 1.18f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.subtitleTopSpacing))

                Text(
                    text = "You can start reading your book\nwhenever you want",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.subtitleFontSize,
                        lineHeight = metrics.subtitleFontSize * 1.24f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.summaryTopSpacing))

                PaymentFailedSummaryCard(
                    uiState = uiState,
                    metrics = metrics,
                    onDiscountCodeClick = onDiscountCodeClick
                )

                Spacer(modifier = Modifier.height(metrics.paymentTopSpacing))

                PaymentMethodCard(
                    uiState = uiState,
                    metrics = metrics,
                    onChangePaymentClick = onChangePaymentClick
                )

                Spacer(modifier = Modifier.weight(1f))

                GetBackButton(
                    metrics = metrics,
                    onClick = onGetBackClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(metrics.bottomSpacing))
            }
        }
    }
}

@Composable
fun PurchaseFailedScreen(
    modifier: Modifier = Modifier,
    uiState: PaymentFailedUiState = PaymentFailedUiState(),
    onBackClick: () -> Unit = {},
    onDiscountCodeClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onGetBackClick: () -> Unit = {}
) {
    PaymentFailedScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onDiscountCodeClick = onDiscountCodeClick,
        onChangePaymentClick = onChangePaymentClick,
        onGetBackClick = onGetBackClick
    )
}

@Composable
private fun PaymentFailedBackground() {
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
                .blur(48.dp)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF5B2021),
                        Color(0xFF183B3D),
                        Color(0xFF101A1F),
                        Color(0xFF882B19)
                    )
                )
            )
            drawCircle(
                color = Color(0xFFA3443C).copy(alpha = 0.46f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.9f, size.height * 0.02f)
            )
            drawCircle(
                color = Color(0xFFC8D3CE).copy(alpha = 0.36f),
                radius = size.width * 0.56f,
                center = Offset(size.width * 0.26f, size.height * 0.42f)
            )
            drawCircle(
                color = Color(0xFFC33F25).copy(alpha = 0.58f),
                radius = size.width * 0.56f,
                center = Offset(size.width * 0.62f, size.height * 1.0f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.24f))
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
            .background(Color.Black.copy(alpha = 0.18f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
private fun FailedPaymentArt(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawFailedCrossShadow()
        drawFailedCrossBody()
    }
}

private fun DrawScope.drawFailedCrossShadow() {
    drawOval(
        color = Color.Black.copy(alpha = 0.22f),
        topLeft = Offset(size.width * 0.25f, size.height * 0.82f),
        size = Size(size.width * 0.5f, size.height * 0.09f)
    )
}

private fun DrawScope.drawFailedCrossBody() {
    val center = Offset(size.width * 0.5f, size.height * 0.48f)
    val longSide = min(size.width * 0.82f, size.height * 0.84f)
    val barWidth = longSide * 0.34f
    val corner = CornerRadius(barWidth * 0.18f, barWidth * 0.18f)
    val sideShift = Offset(longSide * 0.045f, longSide * 0.065f)

    rotate(degrees = -35f, pivot = center) {
        drawCrossBar(
            center = center + sideShift,
            longSide = longSide,
            barWidth = barWidth,
            color = Color(0xFFB42535),
            corner = corner
        )
        drawCrossBar(
            center = center,
            longSide = longSide,
            barWidth = barWidth,
            color = Color(0xFFFF3348),
            corner = corner
        )
        drawCrossBarHighlights(
            center = center,
            longSide = longSide,
            barWidth = barWidth
        )
    }

    drawCenterFailedMark(center = center, radius = barWidth * 0.35f)
}

private fun DrawScope.drawCrossBar(
    center: Offset,
    longSide: Float,
    barWidth: Float,
    color: Color,
    corner: CornerRadius
) {
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - barWidth / 2f, center.y - longSide / 2f),
        size = Size(barWidth, longSide),
        cornerRadius = corner
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - longSide / 2f, center.y - barWidth / 2f),
        size = Size(longSide, barWidth),
        cornerRadius = corner
    )
}

private fun DrawScope.drawCrossBarHighlights(
    center: Offset,
    longSide: Float,
    barWidth: Float
) {
    val lineColor = Color(0xFFD82036).copy(alpha = 0.75f)
    val lightColor = Color.White.copy(alpha = 0.12f)

    drawLine(
        color = lightColor,
        start = Offset(center.x - barWidth * 0.24f, center.y - longSide * 0.42f),
        end = Offset(center.x - barWidth * 0.24f, center.y - barWidth * 0.72f),
        strokeWidth = barWidth * 0.045f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = lineColor,
        start = Offset(center.x + barWidth * 0.22f, center.y + barWidth * 0.72f),
        end = Offset(center.x + barWidth * 0.22f, center.y + longSide * 0.42f),
        strokeWidth = barWidth * 0.04f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = lineColor,
        start = Offset(center.x - longSide * 0.42f, center.y + barWidth * 0.22f),
        end = Offset(center.x - barWidth * 0.72f, center.y + barWidth * 0.22f),
        strokeWidth = barWidth * 0.04f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = Color(0xFF9F1F31).copy(alpha = 0.6f),
        start = Offset(center.x + barWidth * 0.7f, center.y - barWidth * 0.22f),
        end = Offset(center.x + longSide * 0.42f, center.y - barWidth * 0.22f),
        strokeWidth = barWidth * 0.04f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawCenterFailedMark(center: Offset, radius: Float) {
    drawCircle(
        color = Color(0xFFCA2438),
        radius = radius,
        center = center + Offset(radius * 0.18f, radius * 0.18f)
    )
    drawCircle(
        color = Color(0xFFFF5A66),
        radius = radius * 0.92f,
        center = center
    )

    val strokeWidth = radius * 0.34f
    drawLine(
        color = Color(0xFFC9293C),
        start = center + Offset(-radius * 0.48f, -radius * 0.48f),
        end = center + Offset(radius * 0.48f, radius * 0.48f),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
    drawLine(
        color = Color(0xFFC9293C),
        start = center + Offset(radius * 0.48f, -radius * 0.48f),
        end = center + Offset(-radius * 0.48f, radius * 0.48f),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

@Composable
private fun PaymentFailedSummaryCard(
    uiState: PaymentFailedUiState,
    metrics: PaymentFailedMetrics,
    onDiscountCodeClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(metrics.summaryCorner)
    ) {
        Column(
            modifier = Modifier.padding(metrics.summaryPadding),
            verticalArrangement = Arrangement.spacedBy(metrics.rowSpacing)
        ) {
            SummaryRow(
                label = "Book",
                value = uiState.bookPrice,
                metrics = metrics
            )

            SummaryRow(
                label = "Tax & Fees",
                value = uiState.taxAndFees,
                metrics = metrics
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Discount code",
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF707070),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.labelFontSize,
                        lineHeight = metrics.labelFontSize * 1.2f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                SmallPurpleButton(
                    text = "ADD DISCOUNT CODE",
                    width = metrics.discountButtonWidth,
                    height = metrics.smallButtonHeight,
                    fontSize = metrics.smallButtonFontSize,
                    onClick = onDiscountCodeClick
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE5E5E5))
            )

            SummaryRow(
                label = "Total",
                value = uiState.total,
                metrics = metrics,
                highlight = true
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    metrics: PaymentFailedMetrics,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Color(0xFF707070),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.labelFontSize,
                lineHeight = metrics.labelFontSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Text(
            text = value,
            color = if (highlight) MaterialTheme.colorScheme.primary else Color(0xFF707070),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = metrics.valueFontSize,
                lineHeight = metrics.valueFontSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun PaymentMethodCard(
    uiState: PaymentFailedUiState,
    metrics: PaymentFailedMetrics,
    onChangePaymentClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.paymentHeight),
        color = Color.White,
        shape = RoundedCornerShape(metrics.paymentCorner)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = metrics.summaryPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MastercardMark(size = metrics.cardCircleSize)

            Spacer(modifier = Modifier.width(metrics.summaryPadding * 0.75f))

            Text(
                text = uiState.cardLastDigits,
                modifier = Modifier.weight(1f),
                color = Color(0xFF707070),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = metrics.labelFontSize,
                    lineHeight = metrics.labelFontSize * 1.2f,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp
                )
            )

            SmallPurpleButton(
                text = "CHANGE",
                width = metrics.changeButtonWidth,
                height = metrics.smallButtonHeight,
                fontSize = metrics.smallButtonFontSize,
                onClick = onChangePaymentClick
            )
        }
    }
}

@Composable
private fun MastercardMark(size: Dp) {
    Box(
        modifier = Modifier
            .width(size * 1.58f)
            .height(size),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFFFF0018))
        )
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = size * 0.64f)
                .clip(CircleShape)
                .background(Color(0xFFFFA114).copy(alpha = 0.96f))
        )
    }
}

@Composable
private fun SmallPurpleButton(
    text: String,
    width: Dp,
    height: Dp,
    fontSize: TextUnit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .widthIn(min = width)
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = fontSize,
                lineHeight = fontSize * 1.2f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun GetBackButton(
    metrics: PaymentFailedMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.getBackButtonHeight)
            .clip(RoundedCornerShape(metrics.getBackButtonCorner))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Get Back",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun rememberPaymentFailedMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PaymentFailedMetrics {
    return remember(maxWidth, maxHeight) {
        val compactHeight = maxHeight < 760.dp
        val widthScale = min(1f, maxWidth.value / 390f)
        val heightScale = min(1f, maxHeight.value / 844f)
        val scale = min(widthScale, heightScale)

        PaymentFailedMetrics(
            horizontalPadding = (26 * widthScale).dp.coerceAtLeast(20.dp),
            topSpacing = if (compactHeight) 15.dp else 20.dp,
            backButtonSize = (40 * scale).dp.coerceAtLeast(42.dp),
            backIconSize = (24 * scale).dp.coerceAtLeast(24.dp),
            crossTopSpacing = 0.dp,
            crossWidth = (220 * scale).dp.coerceIn(212.dp, 250.dp),
            crossHeight = (230 * scale).dp.coerceIn(208.dp, 258.dp),
            titleTopSpacing = if (compactHeight) 6.dp else 8.dp,
            titleFontSize = (28 * scale).sp,
            subtitleTopSpacing = (12 * heightScale).dp.coerceAtLeast(10.dp),
            subtitleFontSize = (15 * scale).sp,
            summaryTopSpacing = if (compactHeight) 20.dp else 26.dp,
            summaryCorner = (28 * scale).dp.coerceAtLeast(24.dp),
            summaryPadding = (22 * widthScale).dp.coerceAtLeast(18.dp),
            rowSpacing = (20 * heightScale).dp.coerceAtLeast(14.dp),
            labelFontSize = (15 * scale).sp,
            valueFontSize = (15 * scale).sp,
            discountButtonWidth = (135 * widthScale).dp.coerceAtLeast(128.dp),
            smallButtonHeight = (23 * scale).dp.coerceAtLeast(26.dp),
            smallButtonFontSize = (10f * scale).sp,
            paymentTopSpacing = (15 * heightScale).dp.coerceAtLeast(14.dp),
            paymentHeight = (65 * heightScale).dp.coerceAtLeast(64.dp),
            paymentCorner = (28 * scale).dp.coerceAtLeast(24.dp),
            cardCircleSize = (30 * scale).dp.coerceAtLeast(30.dp),
            changeButtonWidth = (76 * widthScale).dp.coerceAtLeast(68.dp),
            getBackButtonHeight = (60 * heightScale).dp.coerceAtLeast(60.dp),
            getBackButtonCorner = (20 * scale).dp.coerceAtLeast(18.dp),
            bottomSpacing = (30 * heightScale).dp.coerceAtLeast(22.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PaymentFailedScreenPreview() {
    IBookTheme(darkTheme = false) {
        PaymentFailedScreen()
    }
}
