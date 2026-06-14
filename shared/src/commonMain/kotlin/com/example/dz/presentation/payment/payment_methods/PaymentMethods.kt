package com.example.dz.presentation.payment.payment_methods

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.ink.InkLabel
import com.example.dz.designsystem.components.ink.InkTopBar
import com.example.dz.designsystem.components.ink.inkCard
import com.example.dz.designsystem.theme.InkColors
import com.example.dz.designsystem.theme.InkShape
import com.example.dz.designsystem.theme.inkBodyFontFamily
import com.example.dz.designsystem.theme.inkColors
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.ic_apple
import dz.shared.generated.resources.paypal
import dz.shared.generated.resources.pm_add
import dz.shared.generated.resources.pm_apple_pay
import dz.shared.generated.resources.pm_device_wallet
import dz.shared.generated.resources.pm_secure_note
import dz.shared.generated.resources.pm_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class PaymentBrand { Paypal, Visa, ApplePay }

data class PaymentMethodItem(
    val id: String,
    val brand: PaymentBrand,
    val title: String,
    val subtitle: String
)

private val defaultMethods = listOf(
    PaymentMethodItem("paypal", PaymentBrand.Paypal, "PayPal", "amelia@hartwell.co"),
    PaymentMethodItem("visa", PaymentBrand.Visa, "Visa ·· 4129", "Expires 08/27"),
    PaymentMethodItem("apple", PaymentBrand.ApplePay, "Apple Pay", "Device wallet")
)

@Composable
fun PaymentMethodsScreen(
    modifier: Modifier = Modifier,
    methods: List<PaymentMethodItem> = defaultMethods,
    onBackClick: () -> Unit = {},
    onPaymentMethodSelected: (PaymentMethodItem) -> Unit = {},
    onAddPaymentMethodClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    val colors = inkColors()
    val bodyFont = inkBodyFontFamily()
    var selectedId by remember { mutableStateOf(methods.firstOrNull()?.id ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
    ) {
        InkTopBar(title = stringResource(Res.string.pm_title), onBackClick = onBackClick, colors = colors)

        Column(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            methods.forEach { method ->
                MethodRow(
                    method = method,
                    selected = method.id == selectedId,
                    onClick = {
                        selectedId = method.id
                        onPaymentMethodSelected(method)
                        onConfirmClick()
                    },
                    colors = colors
                )
            }

            // add new (dashed)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(InkShape.radius))
                    .clickable(onClick = onAddPaymentMethodClick)
                    .padding(horizontal = 16.dp, vertical = 15.dp)
            ) {
                val lineColor = colors.line
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawRoundRect(
                        color = lineColor,
                        style = Stroke(
                            width = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f))
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(InkShape.radius.toPx())
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(colors.accentSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(InkIcons.Plus, null, tint = colors.accent, modifier = Modifier.size(13.dp))
                    }
                    Text(
                        text = stringResource(Res.string.pm_add),
                        fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = colors.accent
                    )
                }
            }
        }

        // secure note
        Row(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(InkIcons.Lock, null, tint = colors.muted, modifier = Modifier.padding(top = 1.dp).size(13.dp))
            Text(
                text = stringResource(Res.string.pm_secure_note),
                fontFamily = bodyFont, fontSize = 11.5.sp, lineHeight = 19.sp, color = colors.muted
            )
        }
    }
}

@Composable
private fun MethodRow(
    method: PaymentMethodItem,
    selected: Boolean,
    onClick: () -> Unit,
    colors: InkColors,
) {
    val bodyFont = inkBodyFontFamily()
    val shape = RoundedCornerShape(InkShape.radius)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surface)
            .border(if (selected) 1.5.dp else 1.dp, if (selected) colors.accent else colors.line, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(InkShape.radiusSm))
                .background(colors.alt),
            contentAlignment = Alignment.Center
        ) {
            when (method.brand) {
                PaymentBrand.Paypal -> Image(painterResource(Res.drawable.paypal), null, modifier = Modifier.size(20.dp))
                PaymentBrand.Visa -> Icon(InkIcons.Card, null, tint = colors.ink, modifier = Modifier.size(20.dp))
                PaymentBrand.ApplePay -> Image(painterResource(Res.drawable.ic_apple), null, modifier = Modifier.size(19.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(method.title, fontFamily = bodyFont, fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp, color = colors.ink)
            Text(method.subtitle, modifier = Modifier.padding(top = 5.dp), fontFamily = bodyFont, fontSize = 11.5.sp, color = colors.muted)
        }
        // radio
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(2.dp, if (selected) colors.accent else colors.line, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(colors.accent))
            }
        }
    }
}

@Composable
fun PaymentMethods(
    modifier: Modifier = Modifier,
    methods: List<PaymentMethodItem> = defaultMethods,
    onBackClick: () -> Unit = {},
    onPaymentMethodSelected: (PaymentMethodItem) -> Unit = {},
    onAddPaymentMethodClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    PaymentMethodsScreen(modifier, methods, onBackClick, onPaymentMethodSelected, onAddPaymentMethodClick, onConfirmClick)
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
private fun PaymentMethodsScreenPreview() {
    PaymentMethodsScreen()
}
