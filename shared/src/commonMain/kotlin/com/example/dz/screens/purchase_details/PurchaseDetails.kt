package com.example.dz.screens.purchase_details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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
import kotlin.math.min

data class PurchaseDetailsUiState(
    val title: String = "Murder Board",
    val author: String = "Brian Shea",
    val bookPrice: String = "\$10.00",
    val taxAndFees: String = "0.00",
    val total: String = "\$10.00",
    val cardLastDigits: String = "**035"
)

private data class PurchaseDetailsMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val coverTopSpacing: Dp,
    val coverWidth: Dp,
    val coverHeight: Dp,
    val coverCorner: Dp,
    val titleTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val authorTopSpacing: Dp,
    val authorFontSize: TextUnit,
    val summaryTopSpacing: Dp,
    val summaryCorner: Dp,
    val summaryPadding: Dp,
    val rowSpacing: Dp,
    val labelFontSize: TextUnit,
    val valueFontSize: TextUnit,
    val discountButtonWidth: Dp,
    val discountButtonHeight: Dp,
    val discountFontSize: TextUnit,
    val paymentTopSpacing: Dp,
    val paymentHeight: Dp,
    val paymentCorner: Dp,
    val cardCircleSize: Dp,
    val changeButtonWidth: Dp,
    val changeButtonHeight: Dp,
    val buttonTopSpacing: Dp,
    val payButtonHeight: Dp,
    val payButtonCorner: Dp,
    val bottomSpacing: Dp
)

@Composable
fun PurchaseDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: PurchaseDetailsUiState = PurchaseDetailsUiState(),
    onBackClick: () -> Unit = {},
    onDiscountCodeClick: () -> Unit = {},
    onChangePaymentClick: () -> Unit = {},
    onPayNowClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberPurchaseDetailsMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            PurchaseDetailsBackground()

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

                Spacer(modifier = Modifier.height(metrics.coverTopSpacing))

                Image(
                    painter = painterResource(Res.drawable.book_cover),
                    contentDescription = "Murder Board book cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = metrics.coverWidth, height = metrics.coverHeight)
                        .shadow(18.dp, RoundedCornerShape(metrics.coverCorner))
                        .clip(RoundedCornerShape(metrics.coverCorner))
                )

                Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

                Text(
                    text = uiState.title,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = metrics.titleFontSize,
                        lineHeight = metrics.titleFontSize * 1.2f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.authorTopSpacing))

                Text(
                    text = "By ${uiState.author}",
                    color = Color.White.copy(alpha = 0.76f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = metrics.authorFontSize,
                        lineHeight = metrics.authorFontSize * 1.25f,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.summaryTopSpacing))

                PurchaseSummaryCard(
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

                PayNowButton(
                    metrics = metrics,
                    onClick = onPayNowClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(metrics.bottomSpacing))
            }
        }
    }
}

@Composable
private fun PurchaseDetailsBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF173238))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.18f
                    scaleY = 1.18f
                }
                .blur(46.dp)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF542021),
                        Color(0xFF0B3F42),
                        Color(0xFF0B171C),
                        Color(0xFF8A3023)
                    )
                )
            )
            drawCircle(
                color = Color(0xFFA6473F).copy(alpha = 0.5f),
                radius = size.width * 0.48f,
                center = Offset(size.width * 0.92f, size.height * 0.02f)
            )
            drawCircle(
                color = Color(0xFF95C8C4).copy(alpha = 0.4f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.3f, size.height * 0.34f)
            )
            drawCircle(
                color = Color(0xFFC54325).copy(alpha = 0.58f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.56f, size.height * 0.96f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.28f))
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
private fun PurchaseSummaryCard(
    uiState: PurchaseDetailsUiState,
    metrics: PurchaseDetailsMetrics,
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
                    height = metrics.discountButtonHeight,
                    fontSize = metrics.discountFontSize,
                    onClick = onDiscountCodeClick
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE7E7E7))
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
    metrics: PurchaseDetailsMetrics,
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
    uiState: PurchaseDetailsUiState,
    metrics: PurchaseDetailsMetrics,
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
                height = metrics.changeButtonHeight,
                fontSize = metrics.discountFontSize,
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
private fun PayNowButton(
    metrics: PurchaseDetailsMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.payButtonHeight)
            .clip(RoundedCornerShape(metrics.payButtonCorner))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pay Now",
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
private fun rememberPurchaseDetailsMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PurchaseDetailsMetrics {
    return remember(maxWidth, maxHeight) {
        val compactHeight = maxHeight < 760.dp
        val widthScale = min(1f, maxWidth.value / 430f)
        val heightScale = min(1f, maxHeight.value / 860f)
        val scale = min(widthScale, heightScale)

        PurchaseDetailsMetrics(
            horizontalPadding = (28 * widthScale).dp.coerceAtLeast(20.dp),
            topSpacing = if (compactHeight) 12.dp else 26.dp,
            backButtonSize = (50 * scale).dp.coerceAtLeast(40.dp),
            backIconSize = (30 * scale).dp.coerceAtLeast(24.dp),
            coverTopSpacing = if (compactHeight) 4.dp else 0.dp,
            coverWidth = (195 * scale).dp.coerceIn(158.dp, 214.dp),
            coverHeight = (270 * scale).dp.coerceIn(218.dp, 292.dp),
            coverCorner = (22 * scale).dp.coerceAtLeast(18.dp),
            titleTopSpacing = (15 * heightScale).dp.coerceAtLeast(16.dp),
            titleFontSize = (20 * scale).sp,
            authorTopSpacing = 6.dp,
            authorFontSize = (14 * scale).sp,
            summaryTopSpacing = if (compactHeight) 36.dp else 30.dp,
            summaryCorner = (30 * scale).dp.coerceAtLeast(24.dp),
            summaryPadding = (24 * widthScale).dp.coerceAtLeast(18.dp),
            rowSpacing = (22 * heightScale).dp.coerceAtLeast(14.dp),
            labelFontSize = (17 * scale).sp,
            valueFontSize = (17 * scale).sp,
            discountButtonWidth = (150 * widthScale).dp.coerceAtLeast(128.dp),
            discountButtonHeight = (29 * scale).dp.coerceAtLeast(26.dp),
            discountFontSize = (10 * scale).sp,
            paymentTopSpacing = (15 * heightScale).dp.coerceAtLeast(14.dp),
            paymentHeight = (74 * heightScale).dp.coerceAtLeast(68.dp),
            paymentCorner = (28 * scale).dp.coerceAtLeast(24.dp),
            cardCircleSize = (36 * scale).dp.coerceAtLeast(30.dp),
            changeButtonWidth = (76 * widthScale).dp.coerceAtLeast(68.dp),
            changeButtonHeight = (25 * scale).dp.coerceAtLeast(26.dp),
            buttonTopSpacing = (78 * heightScale).dp.coerceAtLeast(34.dp),
            payButtonHeight = (60 * heightScale).dp.coerceAtLeast(62.dp),
            payButtonCorner = (22 * scale).dp.coerceAtLeast(18.dp),
            bottomSpacing = (20 * heightScale).dp.coerceAtLeast(22.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun PurchaseDetailsScreenPreview() {
    DZTheme(darkTheme = false) {
        PurchaseDetailsScreen()
    }
}
