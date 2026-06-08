package com.example.dz.screens.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dz.theme.DZTheme
import dz.shared.generated.resources.Res
import dz.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.launch

data class BackButtonMetrics(
    val size: Dp,
    val borderWidth: Dp,
    val iconSize: Dp
)

private data class ForgotPasswordMetrics(
    val horizontalPadding: Dp,
    val topSpacing: Dp,
    val backTitleSpacing: Dp,
    val titleSubtitleGap: Dp,
    val titleOptionSpacing: Dp,
    val optionGap: Dp,
    val buttonTopSpacing: Dp,
    val resendTopSpacing: Dp,
    val bottomSpacing: Dp,
    val optionCorner: Dp,
    val optionBorderWidth: Dp,
    val optionPadding: Dp,
    val optionIconGap: Dp,
    val optionIconSize: Dp,
    val optionTextGap: Dp,
    val optionTitleFontSize: TextUnit,
    val optionDescriptionFontSize: TextUnit,
    val optionDescriptionLineHeight: TextUnit,
    val sendButtonHeight: Dp,
    val sendButtonCorner: Dp,
    val sendButtonElevation: Dp,
    val progressSize: Dp,
    val progressStrokeWidth: Dp,
    val sendButtonTextSize: TextUnit,
    val resendTextSize: TextUnit,
    val backButtonMetrics: BackButtonMetrics
)

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit = {},
    onSendLink: suspend (method: ResetMethod) -> Unit = {}
) {
    var selectedMethod by remember { mutableStateOf<ResetMethod>(ResetMethod.EMAIL) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val metrics = rememberForgotPasswordMetrics(maxWidth = maxWidth, maxHeight = maxHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = metrics.horizontalPadding)
        ) {

            Spacer(modifier = Modifier.height(metrics.topSpacing))

            BackButton(
                onClick = onBack,
                metrics = metrics.backButtonMetrics
            )

            Spacer(modifier = Modifier.height(metrics.backTitleSpacing))

            TitleSection(metrics = metrics)

            Spacer(modifier = Modifier.height(metrics.titleOptionSpacing))

            OptionCard(
                method = ResetMethod.EMAIL,
                isSelected = selectedMethod == ResetMethod.EMAIL,
                metrics = metrics,
                onClick = { selectedMethod = ResetMethod.EMAIL }
            )

            Spacer(modifier = Modifier.height(metrics.optionGap))

            OptionCard(
                method = ResetMethod.WHATSAPP,
                isSelected = selectedMethod == ResetMethod.WHATSAPP,
                metrics = metrics,
                onClick = { selectedMethod = ResetMethod.WHATSAPP }
            )

            Spacer(modifier = Modifier.height(metrics.buttonTopSpacing))
            SendLinkButton(
                isLoading = isLoading,
                enabled = !isLoading,
                metrics = metrics,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        try {
                            onSendLink(selectedMethod)
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(metrics.resendTopSpacing))

            ResendLinkSection(metrics = metrics)

            Spacer(modifier = Modifier.height(metrics.bottomSpacing))
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    metrics: BackButtonMetrics
) {
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    Box(
        modifier = Modifier
            .size(metrics.size)
            .clip(CircleShape)
            .border(
                width = metrics.borderWidth,
                color = borderColor,
                shape = CircleShape
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_back),
            contentDescription = "Go back",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(metrics.iconSize)
        )
    }
}

@Composable
private fun TitleSection(metrics: ForgotPasswordMetrics) {
    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = primaryText.copy(alpha = 0.7f)
    Column {
        Text(
            text = stringResource(Res.string.title_forgot_password),
            style = MaterialTheme.typography.headlineLarge,
            color = primaryText,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(metrics.titleSubtitleGap))

        Text(
            text = stringResource(Res.string.select_option),
            style = MaterialTheme.typography.titleSmall,
            color = secondaryText,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun OptionCard(
    method: ResetMethod,
    isSelected: Boolean,
    metrics: ForgotPasswordMetrics,
    onClick: () -> Unit
) {
    val primaryText = MaterialTheme.colorScheme.onSurface
    val secondaryText = primaryText.copy(alpha = 0.7f)
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(metrics.optionCorner))
            .border(
                width = metrics.optionBorderWidth,
                color = if (isSelected) MaterialTheme.colorScheme.primary else borderColor,
                shape = RoundedCornerShape(metrics.optionCorner)
            )
            .clickable(onClick = onClick)
            .padding(metrics.optionPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(metrics.optionIconGap)
        ) {

            Icon(
                painter = when (method) {
                    ResetMethod.EMAIL -> painterResource(Res.drawable.ic_email_filled)
                    ResetMethod.WHATSAPP -> painterResource(Res.drawable.ic_whatsapp)
                },
                contentDescription = null,
                tint = when (isSelected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> primaryText
                },
                modifier = Modifier.size(metrics.optionIconSize)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Top)
            ) {
                Text(
                    text = when (method) {
                        ResetMethod.EMAIL -> "Send to your email"
                        ResetMethod.WHATSAPP -> "Send to your whatsapp"
                    },
                    fontSize = metrics.optionTitleFontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryText
                )

                Spacer(modifier = Modifier.height(metrics.optionTextGap))

                Text(
                    text = when (method) {
                        ResetMethod.EMAIL -> "Link reset will be send to your email address registered"
                        ResetMethod.WHATSAPP -> "Link reset will be send to your whatsapp account"
                    },
                    fontSize = metrics.optionDescriptionFontSize,
                    color = secondaryText,
                    lineHeight = metrics.optionDescriptionLineHeight
                )
            }
        }
    }
}

@Composable
private fun SendLinkButton(
    isLoading: Boolean,
    enabled: Boolean,
    metrics: ForgotPasswordMetrics,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(metrics.sendButtonHeight),
        shape = RoundedCornerShape(metrics.sendButtonCorner),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = metrics.sendButtonElevation,
            pressedElevation = 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(metrics.progressSize),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = metrics.progressStrokeWidth
            )
        } else {
            Text(
                text = "Send Link",
                fontSize = metrics.sendButtonTextSize,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun ResendLinkSection(metrics: ForgotPasswordMetrics) {
    val primaryText = MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Didn't receive link? ",
            fontSize = metrics.resendTextSize,
            color = primaryText.copy(alpha = 0.7f)
        )

        Text(
            text = "Resend Link",
            fontSize = metrics.resendTextSize,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { /* Handle resend */ }
        )
    }
}

@Composable
private fun rememberForgotPasswordMetrics(
    maxWidth: Dp,
    maxHeight: Dp
): ForgotPasswordMetrics = remember(maxWidth, maxHeight) {
    val widthScale = (maxWidth / 390.dp).coerceIn(0.86f, 1.22f)
    val heightScale = (maxHeight / 844.dp).coerceIn(0.82f, 1.18f)
    val compactness = minOf(widthScale, heightScale)

    ForgotPasswordMetrics(
        horizontalPadding = (24.dp * widthScale).coerceIn(20.dp, 34.dp),
        topSpacing = (52.dp * heightScale).coerceIn(36.dp, 68.dp),
        backTitleSpacing = (32.dp * heightScale).coerceIn(24.dp, 40.dp),
        titleSubtitleGap = (8.dp * heightScale).coerceIn(6.dp, 12.dp),
        titleOptionSpacing = (32.dp * heightScale).coerceIn(22.dp, 40.dp),
        optionGap = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        buttonTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        resendTopSpacing = (24.dp * heightScale).coerceIn(18.dp, 30.dp),
        bottomSpacing = (24.dp * heightScale).coerceIn(18.dp, 34.dp),
        optionCorner = (16.dp * compactness).coerceIn(14.dp, 20.dp),
        optionBorderWidth = (1.dp * compactness).coerceIn(1.dp, 1.5.dp),
        optionPadding = (20.dp * widthScale).coerceIn(16.dp, 26.dp),
        optionIconGap = (16.dp * widthScale).coerceIn(12.dp, 20.dp),
        optionIconSize = (24.dp * compactness).coerceIn(20.dp, 28.dp),
        optionTextGap = (4.dp * heightScale).coerceIn(3.dp, 6.dp),
        optionTitleFontSize = (16f * compactness).coerceIn(14f, 18f).sp,
        optionDescriptionFontSize = (14f * compactness).coerceIn(12f, 16f).sp,
        optionDescriptionLineHeight = (20f * compactness).coerceIn(17f, 23f).sp,
        sendButtonHeight = (56.dp * compactness).coerceIn(50.dp, 62.dp),
        sendButtonCorner = (12.dp * compactness).coerceIn(10.dp, 16.dp),
        sendButtonElevation = (4.dp * compactness).coerceIn(2.dp, 6.dp),
        progressSize = (24.dp * compactness).coerceIn(20.dp, 28.dp),
        progressStrokeWidth = (2.dp * compactness).coerceIn(1.5.dp, 2.5.dp),
        sendButtonTextSize = (16f * compactness).coerceIn(14f, 18f).sp,
        resendTextSize = (14f * compactness).coerceIn(12f, 16f).sp,
        backButtonMetrics = BackButtonMetrics(
            size = (56.dp * compactness).coerceIn(48.dp, 62.dp),
            borderWidth = (1.dp * compactness).coerceIn(1.dp, 1.5.dp),
            iconSize = (24.dp * compactness).coerceIn(20.dp, 28.dp)
        )
    )
}

enum class ResetMethod {
    EMAIL,
    WHATSAPP
}

@Preview(showBackground = true, widthDp = 375, heightDp = 820)
@Composable
fun ForgotPasswordScreenPreview() {
    DZTheme {
        ForgotPasswordScreen()
    }
}
