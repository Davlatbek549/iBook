package com.example.dz.presentation.payment.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.designsystem.components.icons.InkIcons
import com.example.dz.designsystem.components.organic.*
import com.example.dz.designsystem.theme.*
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/** Shared geometry from the Organic handoff: 24dp gutters, 28dp cards, 58dp pill actions. */
@Composable
internal fun CheckoutPage(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    footer: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    OrganicScreen(modifier) {
        Column(Modifier.fillMaxSize().navigationBarsPadding()) {
            OrganicScreenHeader(
                title, fontSize = 28.sp,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 18.dp),
                leading = {
                    OrganicCircleIconButton(InkIcons.Back, onBack, stringResource(Res.string.author_details_back), size = 44.dp)
                },
            )
            Column(
                Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp).padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                when {
                    isLoading -> CheckoutLoading()
                    error != null -> CheckoutNotice(error, isError = true)
                    else -> content()
                }
            }
            if (!isLoading && error == null) {
                Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 26.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp), content = footer)
            }
        }
    }
}

@Composable
internal fun CheckoutText(
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 14,
    color: Color = OrganicColors.neutral700,
    bold: Boolean = false,
    centered: Boolean = false,
) {
    Text(text, modifier, color = color, fontFamily = organicBodyFontFamily(),
        fontSize = size.sp, lineHeight = (size * 1.5f).sp,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        textAlign = if (centered) TextAlign.Center else TextAlign.Start)
}

@Composable
internal fun CheckoutAction(
    text: String,
    onClick: () -> Unit,
    secondary: Boolean = false,
    enabled: Boolean = true,
    busy: Boolean = false,
) {
    Row(
        Modifier.fillMaxWidth().heightIn(min = 58.dp).clip(CircleShape)
            .background(if (!enabled || busy) OrganicColors.neutral300 else if (secondary) OrganicColors.neutral200 else OrganicColors.accent)
            .clickable(enabled = enabled && !busy, role = Role.Button, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (busy) CircularProgressIndicator(Modifier.size(18.dp), color = OrganicColors.neutral700, strokeWidth = 2.dp)
        CheckoutText(text, size = 16, bold = true, centered = true,
            color = if (secondary || !enabled || busy) OrganicColors.neutral800 else Color.White)
    }
}

@Composable
internal fun CheckoutNotice(text: String, isError: Boolean = false) {
    OrganicCard(background = if (isError) OrganicColors.accent100 else OrganicColors.accent2_100,
        modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(18.dp)) {
        CheckoutText(text, color = if (isError) OrganicColors.accent900 else OrganicColors.accent2_900)
    }
}

@Composable
internal fun CheckoutLoading() {
    Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = OrganicColors.accent)
    }
}

@Composable
internal fun CheckoutBook(
    title: String, author: String, coverUrl: String? = null,
    compact: Boolean = false, trailing: String? = null,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        OrganicBookCover(title, coverUrl = coverUrl, width = if (compact) 46.dp else 72.dp,
            height = if (compact) 68.dp else 104.dp, cornerRadius = if (compact) 9.dp else 12.dp, elevated = true)
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (compact) CheckoutText(title, size = 15, bold = true, color = OrganicColors.text)
            else OrganicTitle(title, fontSize = 20.sp)
            if (author.isNotBlank()) CheckoutText(author, size = 12)
            if (!compact) {
                Box(Modifier.clip(CircleShape).background(OrganicColors.accent2_100).padding(horizontal = 10.dp, vertical = 4.dp)) {
                    CheckoutText(stringResource(Res.string.checkout_ebook), size = 11, color = OrganicColors.accent2_900)
                }
            }
        }
        trailing?.let { CheckoutText(it, size = 15, color = OrganicColors.text) }
    }
}

@Composable
internal fun CheckoutAmount(label: String, amount: String, total: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
        if (total) {
            OrganicTitle(label, Modifier.weight(1f), fontSize = 20.sp)
            Text(amount, fontFamily = organicHeadingFontFamily(), fontSize = 26.sp, color = OrganicColors.accent800)
        } else {
            CheckoutText(label, Modifier.weight(1f))
            CheckoutText(amount, color = OrganicColors.text)
        }
    }
}

@Composable
internal fun CheckoutBreakdown(title: String, price: String, discount: String, tax: String, total: String) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CheckoutAmount(title, price)
        CheckoutAmount(stringResource(Res.string.checkout_discount), discount)
        CheckoutAmount(stringResource(Res.string.checkout_tax), tax)
        Spacer(Modifier.height(2.dp))
        CheckoutAmount(stringResource(Res.string.checkout_total), total, total = true)
    }
}

@Composable
internal fun CheckoutBrand(label: String, background: Color = OrganicColors.neutral900) {
    Box(Modifier.size(40.dp, 27.dp).clip(RoundedCornerShape(6.dp)).background(background), contentAlignment = Alignment.Center) {
        Text(label, fontFamily = organicBodyFontFamily(), fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White)
    }
}

@Composable
internal fun CheckoutMethod(label: String, onChange: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OrganicKicker(stringResource(Res.string.checkout_paying_with), OrganicColors.neutral600, fontSize = 12.sp)
        OrganicCard(Modifier.fillMaxWidth(), onClick = onChange, contentPadding = PaddingValues(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Icon(InkIcons.Card, null, tint = OrganicColors.neutral800, modifier = Modifier.size(24.dp))
                CheckoutText(label, Modifier.weight(1f), size = 15, bold = true, color = OrganicColors.text)
                CheckoutText(stringResource(Res.string.checkout_change), size = 13, bold = true, color = OrganicColors.accent700)
            }
        }
    }
}

/** Result screens keep actions anchored; the central content scrolls on small/large-text devices. */
@Composable
internal fun CheckoutResult(
    success: Boolean, title: String, description: String, icon: ImageVector,
    onClose: () -> Unit, modifier: Modifier = Modifier,
    footer: @Composable ColumnScope.() -> Unit,
    detail: @Composable ColumnScope.() -> Unit,
) {
    OrganicScreen(modifier) {
        Column(Modifier.fillMaxSize().navigationBarsPadding().drawBehind {
            if (success) drawCircle(OrganicColors.accent2_100, 150.dp.toPx(), Offset(10.dp.toPx(), 210.dp.toPx()))
            drawCircle(OrganicColors.accent100, if (success) 95.dp.toPx() else 145.dp.toPx(),
                Offset(size.width + 25.dp.toPx(), if (success) 425.dp.toPx() else 230.dp.toPx()))
        }) {
            OrganicCircleIconButton(InkIcons.Close, onClose, stringResource(Res.string.checkout_close),
                Modifier.align(Alignment.End).padding(top = 8.dp, end = 24.dp), size = 44.dp)
            Box(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()), contentAlignment = Alignment.Center) {
                Column(Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(22.dp)) {
                    Box(Modifier.size(108.dp).clip(CircleShape).background(if (success) OrganicColors.accent2_700 else OrganicColors.accent900), contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = OrganicColors.bg, modifier = Modifier.size(44.dp))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(title, fontFamily = organicHeadingFontFamily(), fontSize = 32.sp, lineHeight = 36.sp,
                            textAlign = TextAlign.Center, color = OrganicColors.text)
                        CheckoutText(description, Modifier.widthIn(max = 280.dp), size = 16, centered = true)
                    }
                    detail()
                }
            }
            Column(Modifier.fillMaxWidth().padding(horizontal = 28.dp).padding(top = 16.dp, bottom = 26.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp), content = footer)
        }
    }
}
