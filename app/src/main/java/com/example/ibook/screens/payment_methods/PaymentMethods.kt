package com.example.ibook.screens.payment_methods

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibook.R
import com.example.ibook.ui.theme.IBookTheme

enum class PaymentBrand {
    Mastercard,
    Paypal,
    Visa
}

data class PaymentMethodItem(
    val id: String,
    val brand: PaymentBrand,
    val label: String
)

data class PaymentMethodsUiState(
    val methods: List<PaymentMethodItem> = listOf(
        PaymentMethodItem(
            id = "mastercard",
            brand = PaymentBrand.Mastercard,
            label = "**035"
        ),
        PaymentMethodItem(
            id = "paypal",
            brand = PaymentBrand.Paypal,
            label = "**ner96@gmail.com"
        ),
        PaymentMethodItem(
            id = "visa",
            brand = PaymentBrand.Visa,
            label = "**320"
        )
    ),
    val selectedMethodId: String = "mastercard"
)

private data class PaymentMethodsMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backButtonSize: Dp,
    val backIconSize: Dp,
    val titleTopSpacing: Dp,
    val titleFontSize: TextUnit,
    val cardTopSpacing: Dp,
    val cardCorner: Dp,
    val cardHorizontalPadding: Dp,
    val cardTopPadding: Dp,
    val cardBottomPadding: Dp,
    val rowHeight: Dp,
    val rowGap: Dp,
    val logoWidth: Dp,
    val logoHeight: Dp,
    val labelFontSize: TextUnit,
    val selectorSize: Dp,
    val addButtonWidth: Dp,
    val addButtonHeight: Dp,
    val addButtonFontSize: TextUnit,
    val confirmButtonHeight: Dp,
    val confirmButtonCorner: Dp,
    val confirmFontSize: TextUnit,
    val bottomSpacing: Dp
)

@Composable
fun PaymentMethodsScreen(
    modifier: Modifier = Modifier,
    uiState: PaymentMethodsUiState = PaymentMethodsUiState(),
    onBackClick: () -> Unit = {},
    onPaymentMethodSelected: (PaymentMethodItem) -> Unit = {},
    onAddPaymentMethodClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val metrics = rememberPaymentMethodsMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Box(modifier = Modifier.fillMaxSize()) {
            PaymentMethodsBackground()

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
                    PaymentMethodsBackButton(
                        size = metrics.backButtonSize,
                        iconSize = metrics.backIconSize,
                        onClick = onBackClick
                    )
                }

                Spacer(modifier = Modifier.height(metrics.titleTopSpacing))

                Text(
                    text = "Payment Methods",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = metrics.titleFontSize,
                        lineHeight = metrics.titleFontSize * 1.1f,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )
                )

                Spacer(modifier = Modifier.height(metrics.cardTopSpacing))

                PaymentMethodsCard(
                    uiState = uiState,
                    metrics = metrics,
                    onPaymentMethodSelected = onPaymentMethodSelected,
                    onAddPaymentMethodClick = onAddPaymentMethodClick
                )

                Spacer(modifier = Modifier.weight(1f))

                ConfirmButton(
                    metrics = metrics,
                    onClick = onConfirmClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(metrics.bottomSpacing))
            }
        }
    }
}

@Composable
fun PaymentMethods(
    modifier: Modifier = Modifier,
    uiState: PaymentMethodsUiState = PaymentMethodsUiState(),
    onBackClick: () -> Unit = {},
    onPaymentMethodSelected: (PaymentMethodItem) -> Unit = {},
    onAddPaymentMethodClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    PaymentMethodsScreen(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onPaymentMethodSelected = onPaymentMethodSelected,
        onAddPaymentMethodClick = onAddPaymentMethodClick,
        onConfirmClick = onConfirmClick
    )
}

@Composable
private fun PaymentMethodsBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14272C))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = 1.22f
                    scaleY = 1.18f
                }
                .blur(52.dp)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3F1F23),
                        Color(0xFF1B4545),
                        Color(0xFF76797A),
                        Color(0xFF7D2418)
                    )
                )
            )
            drawCircle(
                color = Color(0xFF6F1718).copy(alpha = 0.7f),
                radius = size.width * 0.33f,
                center = Offset(size.width * 0.58f, size.height * 0.09f)
            )
            drawCircle(
                color = Color(0xFF0B5B58).copy(alpha = 0.58f),
                radius = size.width * 0.42f,
                center = Offset(size.width * 0.9f, size.height * 0.22f)
            )
            drawCircle(
                color = Color(0xFF8F2E1C).copy(alpha = 0.78f),
                radius = size.width * 0.48f,
                center = Offset(size.width * 0.78f, size.height * 0.93f)
            )
            drawRect(
                color = Color(0xFF9B9B9B).copy(alpha = 0.38f),
                topLeft = Offset(0f, size.height * 0.45f),
                size = Size(size.width, size.height * 0.4f)
            )
        }
    }
}

@Composable
private fun PaymentMethodsCard(
    uiState: PaymentMethodsUiState,
    metrics: PaymentMethodsMetrics,
    onPaymentMethodSelected: (PaymentMethodItem) -> Unit,
    onAddPaymentMethodClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(metrics.cardCorner),
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = metrics.cardHorizontalPadding,
                    top = metrics.cardTopPadding,
                    end = metrics.cardHorizontalPadding,
                    bottom = metrics.cardBottomPadding
                )
        ) {
            uiState.methods.forEachIndexed { index, item ->
                PaymentMethodRow(
                    item = item,
                    selected = item.id == uiState.selectedMethodId,
                    metrics = metrics,
                    onClick = { onPaymentMethodSelected(item) }
                )

                if (index != uiState.methods.lastIndex) {
                    Spacer(modifier = Modifier.height(metrics.rowGap))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AddPaymentMethodButton(
                    metrics = metrics,
                    onClick = onAddPaymentMethodClick
                )
            }
        }
    }
}

@Composable
private fun PaymentMethodRow(
    item: PaymentMethodItem,
    selected: Boolean,
    metrics: PaymentMethodsMetrics,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.rowHeight)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PaymentBrandMark(
            brand = item.brand,
            modifier = Modifier.size(
                width = metrics.logoWidth,
                height = metrics.logoHeight
            )
        )

        Text(
            text = item.label,
            color = Color(0xFF707075),
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.labelFontSize,
                lineHeight = metrics.labelFontSize * 1.16f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        SelectionIndicator(
            selected = selected,
            size = metrics.selectorSize
        )
    }
}

@Composable
private fun PaymentBrandMark(
    brand: PaymentBrand,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        when (brand) {
            PaymentBrand.Mastercard -> MastercardMark()
            PaymentBrand.Paypal -> PaypalMark()
            PaymentBrand.Visa -> VisaMark()
        }
    }
}

@Composable
private fun MastercardMark() {
    Canvas(modifier = Modifier.size(width = 68.dp, height = 42.dp)) {
        val radius = size.height * 0.45f
        drawCircle(
            color = Color(0xFFFF0018),
            radius = radius,
            center = Offset(size.width * 0.36f, size.height / 2f)
        )
        drawCircle(
            color = Color(0xFFFFA114),
            radius = radius,
            center = Offset(size.width * 0.66f, size.height / 2f)
        )
    }
}

@Composable
private fun PaypalMark() {
    Box(
        modifier = Modifier.size(width = 68.dp, height = 42.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.paypal),
            contentDescription = "PayPal",
            modifier = Modifier
                .padding(start = 4.dp)
                .size(width = 36.dp, height = 42.dp)
        )
    }
}

@Composable
private fun VisaMark() {
    Text(
        text = "VISA",
        color = Color(0xFF23398E),
        modifier = Modifier.width(68.dp),
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = 29.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            letterSpacing = 0.sp
        )
    )
}

@Composable
private fun SelectionIndicator(
    selected: Boolean,
    size: Dp
) {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (selected) primary else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.58f)
            )
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = primary,
                    radius = this.size.minDimension / 2f - 2.dp.toPx(),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 3.5.dp.toPx()
                    )
                )
            }
        }
    }
}

@Composable
private fun AddPaymentMethodButton(
    metrics: PaymentMethodsMetrics,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(metrics.addButtonWidth)
            .height(metrics.addButtonHeight)
            .clip(RoundedCornerShape(metrics.addButtonHeight / 2))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ADD PAYMENT METHOD",
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = metrics.addButtonFontSize,
                lineHeight = metrics.addButtonFontSize * 1.1f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun ConfirmButton(
    metrics: PaymentMethodsMetrics,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(metrics.confirmButtonHeight)
            .clip(RoundedCornerShape(metrics.confirmButtonCorner))
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Confirm",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = metrics.confirmFontSize,
                lineHeight = metrics.confirmFontSize * 1.1f,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )
        )
    }
}

@Composable
private fun PaymentMethodsBackButton(
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
private fun rememberPaymentMethodsMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): PaymentMethodsMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 375.dp).coerceIn(0.86f, 1.28f)
    val heightScale = (maxHeight / 812.dp).coerceIn(0.82f, 1.22f)
    val compactness = minOf(widthScale, heightScale)

    PaymentMethodsMetrics(
        horizontalPadding = (28.dp * widthScale).coerceIn(20.dp, 44.dp),
        topSpacing = (44.dp * heightScale).coerceIn(25.dp, 50.dp),
        backButtonSize = (46.dp * compactness).coerceIn(40.dp, 54.dp),
        backIconSize = (26.dp * compactness).coerceIn(22.dp, 30.dp),
        titleTopSpacing = (42.dp * heightScale).coerceIn(28.dp, 50.dp),
        titleFontSize = (30f * widthScale).coerceIn(31f, 48f).sp,
        cardTopSpacing = (36.dp * heightScale).coerceIn(24.dp, 46.dp),
        cardCorner = (30.dp * compactness).coerceIn(24.dp, 38.dp),
        cardHorizontalPadding = (20.dp * widthScale).coerceIn(16.dp, 28.dp),
        cardTopPadding = (25.dp * heightScale).coerceIn(22.dp, 34.dp),
        cardBottomPadding = (26.dp * heightScale).coerceIn(20.dp, 32.dp),
        rowHeight = (55.dp * heightScale).coerceIn(48.dp, 62.dp),
        rowGap = (12.dp * heightScale).coerceIn(10.dp, 20.dp),
        logoWidth = (70.dp * widthScale).coerceIn(66.dp, 88.dp),
        logoHeight = (40.dp * compactness).coerceIn(40.dp, 50.dp),
        labelFontSize = (15f * widthScale).coerceIn(16f, 22f).sp,
        selectorSize = (24.dp * compactness).coerceIn(22.dp, 30.dp),
        addButtonWidth = (158.dp * widthScale).coerceIn(148.dp, 190.dp),
        addButtonHeight = (24.dp * compactness).coerceIn(23.dp, 30.dp),
        addButtonFontSize = (11f * widthScale).coerceIn(10f, 13f).sp,
        confirmButtonHeight = (50.dp * heightScale).coerceIn(58.dp, 76.dp),
        confirmButtonCorner = (20.dp * compactness).coerceIn(18.dp, 26.dp),
        confirmFontSize = (17f * widthScale).coerceIn(16f, 21f).sp,
        bottomSpacing = (54.dp * heightScale).coerceIn(34.dp, 70.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PaymentMethodsScreenPreview() {
    IBookTheme {
        PaymentMethodsScreen()
    }
}
