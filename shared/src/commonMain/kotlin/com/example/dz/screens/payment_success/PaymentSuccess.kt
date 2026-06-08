package com.example.dz.screens.payment_success

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlin.math.min

data class PurchaseSuccessUiState(
    val bookPrice: String = "\$10.00",
    val taxAndFees: String = "0.00",
    val total: String = "\$10.00",
    val cardLastDigits: String = "**035"
)

private data class PurchaseSuccessMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val targetTopSpacing: Dp,
    val targetWidth: Dp,
    val targetHeight: Dp,
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
    val readButtonHeight: Dp,
    val readButtonCorner: Dp,
    val bottomSpacing: Dp
)

@Composable
fun PurchaseSuccessScreen(
    modifier: Modifier = Modifier,
    uiState: PurchaseSuccessUiState = PurchaseSuccessUiState(),
    onBackClick: () -> Unit = {},
    onDiscountCodeClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onReadNowClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberPurchaseSuccessMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            PaymentSuccessBackground()

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

                Spacer(modifier = Modifier.height(metrics.targetTopSpacing))

                TargetSuccessArt(
                    modifier = Modifier.size(
                        width = metrics.targetWidth,
                        height = metrics.targetHeight
                    )
                )

                Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

                Text(
                    text = "Payment Success!",
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

                PurchaseSuccessSummaryCard(
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

                ReadNowButton(
                    metrics = metrics,
                    onClick = onReadNowClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(metrics.bottomSpacing))
            }
        }
    }
}

@Composable
fun PaymentSuccessScreen(
    modifier: Modifier = Modifier,
    uiState: PurchaseSuccessUiState = PurchaseSuccessUiState(),
    onBackClick: () -> Unit = {},
    onDiscountCodeClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onReadNowClick: () -> Unit = {}
) {
    PurchaseSuccessScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onDiscountCodeClick = onDiscountCodeClick,
        onChangePaymentClick = onChangePaymentClick,
        onReadNowClick = onReadNowClick
    )
}

@Composable
private fun PaymentSuccessBackground() {
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
            painter = painterResource(Res.drawable.ic_back),
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
private fun TargetSuccessArt(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawTargetShadow()
        drawTargetBoard()
        drawArrow()
    }
}

private fun DrawScope.drawTargetShadow() {
    drawOval(
        color = Color.Black.copy(alpha = 0.2f),
        topLeft = Offset(size.width * 0.2f, size.height * 0.86f),
        size = Size(size.width * 0.5f, size.height * 0.08f)
    )
}

private fun DrawScope.drawTargetBoard() {
    val center = Offset(size.width * 0.42f, size.height * 0.5f)
    val radius = min(size.width * 0.31f, size.height * 0.46f)
    val sideOffset = Offset(-radius * 0.18f, radius * 0.1f)

    drawCircle(
        color = Color(0xFFE6273A),
        radius = radius,
        center = center + sideOffset
    )
    drawCircle(
        color = Color(0xFFFF3548),
        radius = radius,
        center = center
    )
    drawCircle(
        color = Color(0xFFFF4B5A).copy(alpha = 0.75f),
        radius = radius * 0.86f,
        center = center
    )

    val ringColors = listOf(
        Color(0xFFFF3548),
        Color(0xFFF5F8FB),
        Color(0xFFFF4554),
        Color(0xFFF5F8FB),
        Color(0xFFFF4554)
    )
    val ringRadii = listOf(0.72f, 0.55f, 0.38f, 0.22f, 0.08f)

    ringColors.zip(ringRadii).forEach { (color, scale) ->
        drawCircle(
            color = color,
            radius = radius * scale,
            center = center
        )
    }

    drawCircle(
        color = Color.White.copy(alpha = 0.18f),
        radius = radius * 0.72f,
        center = center,
        style = Stroke(width = radius * 0.018f)
    )
    drawArc(
        color = Color.White.copy(alpha = 0.14f),
        startAngle = 112f,
        sweepAngle = 96f,
        useCenter = false,
        topLeft = Offset(center.x - radius * 0.92f, center.y - radius * 0.92f),
        size = Size(radius * 1.84f, radius * 1.84f),
        style = Stroke(width = radius * 0.08f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawArrow() {
    val tip = Offset(size.width * 0.93f, size.height * 0.18f)
    val hit = Offset(size.width * 0.42f, size.height * 0.51f)
    val shaftStart = Offset(size.width * 0.74f, size.height * 0.3f)

    drawLine(
        color = Color(0xFF283495).copy(alpha = 0.35f),
        start = hit + Offset(-1f, 4f),
        end = shaftStart + Offset(3f, 5f),
        strokeWidth = size.minDimension * 0.04f,
        cap = StrokeCap.Round
    )
    drawLine(
        color = Color(0xFF514BD5),
        start = hit,
        end = shaftStart,
        strokeWidth = size.minDimension * 0.036f,
        cap = StrokeCap.Round
    )

    val headPath = Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(size.width * 0.71f, size.height * 0.21f)
        lineTo(size.width * 0.8f, size.height * 0.29f)
        close()
    }
    drawPath(
        path = headPath,
        color = Color(0xFFFFC247)
    )

    val notchPath = Path().apply {
        moveTo(size.width * 0.72f, size.height * 0.22f)
        lineTo(size.width * 0.8f, size.height * 0.2f)
        lineTo(size.width * 0.76f, size.height * 0.26f)
        close()
    }
    drawPath(
        path = notchPath,
        color = Color(0xFFD78B25)
    )

    drawCircle(
        color = Color(0xFF635CFF),
        radius = size.minDimension * 0.022f,
        center = Offset(size.width * 0.78f, size.height * 0.2f)
    )

    drawCircle(
        color = Color(0xFF635CFF),
        radius = size.minDimension * 0.022f,
        center = hit
    )
}

@Composable
private fun PurchaseSuccessSummaryCard(
    uiState: PurchaseSuccessUiState,
    metrics: PurchaseSuccessMetrics,
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
                label = "",
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
    metrics: PurchaseSuccessMetrics,
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
    uiState: PurchaseSuccessUiState,
    metrics: PurchaseSuccessMetrics,
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
private fun ReadNowButton(
    metrics: PurchaseSuccessMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.readButtonHeight)
            .clip(RoundedCornerShape(metrics.readButtonCorner))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Read Now",
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
private fun rememberPurchaseSuccessMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PurchaseSuccessMetrics {
    return remember(maxWidth, maxHeight) {
        val compactHeight = maxHeight < 760.dp
        val widthScale = min(1f, maxWidth.value / 390f)
        val heightScale = min(1f, maxHeight.value / 844f)
        val scale = min(widthScale, heightScale)

        PurchaseSuccessMetrics(
            horizontalPadding = (26 * widthScale).dp.coerceAtLeast(20.dp),
            topSpacing = if (compactHeight) 15.dp else 20.dp,
            backButtonSize = (40 * scale).dp.coerceAtLeast(42.dp),
            backIconSize = (24 * scale).dp.coerceAtLeast(24.dp),
            targetTopSpacing = 0.dp,
            targetWidth = (270 * scale).dp.coerceIn(232.dp, 286.dp),
            targetHeight = (182 * scale).dp.coerceIn(158.dp, 194.dp),
            titleTopSpacing = if (compactHeight) 8.dp else 10.dp,
            titleFontSize = (28 * scale).sp,
            subtitleTopSpacing = (12 * heightScale).dp.coerceAtLeast(10.dp),
            subtitleFontSize = (15 * scale).sp,
            summaryTopSpacing = if (compactHeight) 25.dp else 35.dp,
            summaryCorner = (28 * scale).dp.coerceAtLeast(24.dp),
            summaryPadding = (22 * widthScale).dp.coerceAtLeast(18.dp),
            rowSpacing = (22 * heightScale).dp.coerceAtLeast(14.dp),
            labelFontSize = (15 * scale).sp,
            valueFontSize = (15 * scale).sp,
            discountButtonWidth = (135 * widthScale).dp.coerceAtLeast(128.dp),
            smallButtonHeight = (23 * scale).dp.coerceAtLeast(26.dp),
            smallButtonFontSize = (10f * scale).sp,
            paymentTopSpacing = (15 * heightScale).dp.coerceAtLeast(14.dp),
            paymentHeight = (68 * heightScale).dp.coerceAtLeast(64.dp),
            paymentCorner = (28 * scale).dp.coerceAtLeast(24.dp),
            cardCircleSize = (30 * scale).dp.coerceAtLeast(30.dp),
            changeButtonWidth = (76 * widthScale).dp.coerceAtLeast(68.dp),
            readButtonHeight = (64 * heightScale).dp.coerceAtLeast(60.dp),
            readButtonCorner = (20 * scale).dp.coerceAtLeast(18.dp),
            bottomSpacing = (38 * heightScale).dp.coerceAtLeast(22.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PurchaseSuccessScreenPreview() {
    DZTheme(darkTheme = false) {
        PurchaseSuccessScreen()
    }
}
